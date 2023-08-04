package appli.model.domaine.administration.service.impl;

import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.SocieteBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.service.ISocieteService;
import appli.model.domaine.administration.validator.SocieteValidator;
import framework.model.beanContext.AbonnePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator = SocieteValidator.class)
@Named
public class SocieteService extends GenericJpaService<SocieteBean, Long> implements ISocieteService {
	@Override
	@Transactional
	public void delete(Long id) {
		try {
			super.delete(id);
		} catch (Exception e) {
			MessageService.addBannerMessage("Cette societé est liée à des enregistrements");
		}
	}
	
	@Override
	public void activerDesactiverElement(Long workIdLong) {
		SocietePersistant societeP = findById(SocietePersistant.class, workIdLong);
		societeP.setIs_disable(BooleanUtil.isTrue(societeP.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(societeP);
	}

	@Transactional
	@Override
	public void majLastConnectedEts(Long etsId) {
		AbonnePersistant abonneP = findById(EtablissementPersistant.class, etsId).getOpc_abonne();
		abonneP.setLast_ets(etsId);
		
		MessageService.getGlobalMap().put("GLOBAL_ABONNE", abonneP);
		
		getEntityManager().merge(abonneP);
	}
}