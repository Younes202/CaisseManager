package appli.controller.domaine.util_erp;

import java.util.Map;

import framework.controller.ActionUtil;
import framework.controller.ControllerUtil;
import framework.controller.FileUtilController;
import framework.controller.bean.action.IViewBean;
import framework.model.common.constante.ActionConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

public class ControllerDataUtil {
	@SuppressWarnings("unchecked")
	public static boolean isEspaceDisqueDisponible(ActionUtil httpUtil, String fieldName, String domaine, IViewBean viewBean, String tableName, String ... customActions){
		String action = httpUtil.getAction();
		//
		if(viewBean != null){
			boolean isActionUpload = (action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE));
			
			if(!isActionUpload && customActions != null){
				for(String customAct : customActions){
					if(action.equals(customAct)){
						isActionUpload = true;
						break;
					}
				}
			}
			
			// Purge de la session en cas de Create / Update
			if(isActionUpload && !httpUtil.isError()){
				// Controle de l'espace disque disponible ----------------------------------
				int espaceMax = 100;//ENUM_TARIF_DISQUE.valueOf(ContextRestaurent.getRestaurentBean().getEspace_disque()).getTaille();
				int tailleReelle = FileUtilController.getEspaceUtilise(ContextAppli.getEtablissementBean().getId().toString());
				
				if(espaceMax - tailleReelle <= 0){
					Map<String, byte[]> mapStream = (Map<String, byte[]>)ControllerUtil.getMenuAttribute(fieldName, httpUtil.getRequest());
					if(mapStream != null && mapStream.size() > 0){
						MessageService.addBannerMessage("L'espace disque de saturé. Vous ne pouvez plus importer des pièces jointes.");
						return false;
					}
				}
				//---------------------------------------------------------------------------
			}
		}
		return true;
	}
	
	/**
	 * @param httpUtil
	 * @param fieldName
	 * @param path
	 * @param viewBean
	 * @param tableName
	 * @param customActions
	 */
	public static void manageUploadedFiles(ActionUtil httpUtil, String fieldName, String path, IViewBean viewBean, String tableName, String ... customActions){
		// Pièces jointes
		String action = httpUtil.getAction();
		Object idObj = ReflectUtil.getObjectPropertieValue(viewBean, "id");
		if(viewBean != null/* && StringUtil.isNotEmpty(idObj)*/){
			Long id = StringUtil.isNotEmpty(idObj) ? Long.valueOf(""+idObj) : null;
			
			boolean isActionUpload = (action.equals(ActionConstante.CREATE) || action.equals(ActionConstante.UPDATE));
			
			if(!isActionUpload && customActions != null){
				for(String customAct : customActions){
					if(action.equals(customAct)){
						isActionUpload = true;
						break;
					}
				}
			}
			
			// Purge de la session en cas de Create / Update
			if(isActionUpload && !httpUtil.isError()){
//				if(!isEspaceDisqueDisponible(httpUtil, fieldName, path, viewBean, tableName, customActions)){
//					return;
//				}
				
				FileUtilController.uploadFilesToDir(httpUtil.getRequest(), fieldName, path, id);
				FileUtilController.removeUplodedStreamsFromSession(httpUtil.getRequest(), fieldName);
			}
			// Chargement pour affichage
			String[] piecesJointes = FileUtilController.getFilesNames(httpUtil.getRequest(), fieldName, path, id);
			ReflectUtil.setProperty(viewBean, "piecesJointes", piecesJointes);
		}
		
		// Purge de la session et du répértoire en cas de suppression
		if(action.equals(ActionConstante.DELETE) && !httpUtil.isError()){
			FileUtilController.removeUplodedStreamsFromSession(httpUtil.getRequest(), fieldName);
			FileUtilController.clearDir(path, httpUtil.getWorkIdLong());
		} else if(tableName != null && action.equals(ActionConstante.DELETE_ROWS) && !httpUtil.isError()){
			Long[] listOrdre = httpUtil.getCheckedElementsLong(tableName);
			for (Long ordreId : listOrdre) {
				FileUtilController.removeUplodedStreamsFromSession(httpUtil.getRequest(), fieldName);
				FileUtilController.clearDir(path, ordreId);
			}
		}
	}
}
