package appli.controller.util_ctrl;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.controller.ControllerUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.synchro.ISynchroniseService;
import framework.model.util.synchro.SynchroniseFileService;

@WebServlet(urlPatterns="/synchroCtrl")
/**
 * Point d'entrée de la synchronisation.
 * Le principe : 
 * Synchronisation entre CLOUD et LOCAL. Elle peut être mono direction ou bi direction pour un meilleur temps réel. 
 * @author T.K
 *
 */
public class SynchroniseServlet extends HttpServlet{ 
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ISynchroniseService synchroService = (ISynchroniseService) ServiceUtil.getBusinessBean(ISynchroniseService.class);
		
		String methode =  request.getParameter("mt");
		String etsCodeAuth = request.getParameter("ets");
		
		if(StringUtil.isEmpty(methode)) {
			return;
		}
		
		switch (methode) {
			case "load_opc": {
				String codeFunc = request.getParameter("cfunc");
				String className = request.getParameter("cls");
				
				Object entityOpc = synchroService.getOpcByCodeFunc(className, codeFunc);
				
				if(entityOpc == null){
					response.getWriter().write("");
				} else{
					response.getWriter().write(ControllerUtil.getJSonDataAnnotStartegy(entityOpc));
				}
			}; break;
			
			case "sync_out": {// Poster la data du CLOUD à LOCAL ou l'inverse
				String requestData = request.getReader().lines().collect(Collectors.joining());
				//
				String ids = synchroService.synchroniser(requestData, etsCodeAuth);
				System.out.println("RETOUR dyncOut ==> "+ids);
				
				response.getWriter().write(ids);// Ids traités
			}; break;
			
			//--------------------------------------------
			case "sync_in": {// Le CLOUD ou LOCAL demande la data disponible
				String json = synchroService.getJsonDataToSynchronise(etsCodeAuth);

				if(StringUtil.isEmpty(json)){
					response.getWriter().write("OK");
				} else{
					response.getWriter().write(json);
				}
			}; break;
			case "synch_ids": {// Le slave poste au master la data traitées
				String requestData = request.getReader().lines().collect(Collectors.joining());
				String[] responseIds = StringUtil.getArrayFromStringDelim(requestData, "|");
				
				if(StringUtil.isNotEmpty(responseIds)){
					synchroService.deleteSynchronisedIds(responseIds);
				}
				response.getWriter().write("OK");
			}; break;
			// Files-------------------------------------
			case "synch_files_in": {//TODO : à finir // Le slave demande au master les fichiers disponible => Code du master
				SynchroniseFileService.writeResponseFile(response, "", "");
			}; break;
			case "synch_files_out": {// Fichier posté depuis le slave
				SynchroniseFileService.savePostedFile(request);
			}; break;

		}
	}
}