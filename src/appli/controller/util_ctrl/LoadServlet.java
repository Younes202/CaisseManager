package appli.controller.util_ctrl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.service.IParametrageService;
import framework.model.beanContext.AbonnePersistant;
import framework.model.common.service.IhmMappingService;
import framework.model.common.service.MessageService;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.util.MenuMappingService;
import framework.model.util.audit.ReplicationGenerationEventListener;

@SuppressWarnings("serial")
public class LoadServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// Load properties
		System.out.println("************************* Chargement des libellés *************************");
		new StrimUtil();
		
		// Load menu mapping and set servlet context for ihm mapping
		System.out.println("************************* Chargement mapping *************************");
		MenuMappingService.servletContext = config.getServletContext();
		// Load actions mapping
		new IhmMappingService();

		// Injections
		if(!ContextAppli.IS_CLOUD_MASTER() && !ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE ){
			String env = StrimUtil.getGlobalConfigPropertie("context.soft");
			if(!SOFT_ENVS.syndic.toString().equals(env) && !SOFT_ENVS.agri.toString().equals(env)) {
				IParametrageService paramService = (IParametrageService) ServiceUtil.getBusinessBean(IParametrageService.class);
				
				paramService.executerInitScript(null);
				
				paramService.addDataRequired();
			
				AbonnePersistant value = (AbonnePersistant) paramService.getEntityManager()
					.createQuery("from AbonnePersistant")
					.getResultList().get(0);
				MessageService.getGlobalMap().put("GLOBAL_ABONNE", ReflectUtil.cloneBean(value));
			}
		}
	}
}
