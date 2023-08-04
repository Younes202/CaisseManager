package appli.controller.domaine.caisse.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import appli.controller.domaine.caisse.bean.TicketCaisseConfBean;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.caisse.service.ITicketCaisseService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.vente.persistant.TicketCaisseConfDetailPersistant;
import appli.model.domaine.vente.persistant.TicketCaisseConfPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.controller.ActionBase;
import framework.controller.ActionUtil;
import framework.controller.annotation.WorkController;
import framework.model.common.service.MessageService;
import framework.model.common.util.ServiceUtil;

@WorkController(nameSpace = "caisse", bean=TicketCaisseConfBean.class, jspRootPath = "/domaine/caisse/back-office/")
public class TicketCaisseConfAction extends ActionBase {
	@Inject
	private ITicketCaisseService ticketCaisseService;
	@Inject
	private IFamilleService familleService;
	
	/**
	 * @param httpUtil
	 */
	public void work_init(ActionUtil httpUtil) {
		Map<String, String> mapFields = new LinkedHashMap<>();
		
		mapFields.put("ref", "Référence");
//		mapFields.put("code_barre", "Code barre");
		mapFields.put("Type", "Type");
		mapFields.put("mtt_cmd", "Montant commande");
		mapFields.put("nom_client", "Nom client");
		mapFields.put("adresse_client", "Adresse client");
		mapFields.put("livreur", "Livreur");
		mapFields.put("serveur", "Serveur");
		mapFields.put("caissier", "Caissier");
		mapFields.put("date_vente", "Date");
		mapFields.put("famille", "Familles article");
		mapFields.put("date_courante", "Date courante");
		mapFields.put("montant_net", "Montant net");
		mapFields.put("montasnt_reduction", "Montasnt de reduction");
		mapFields.put("code_wifi", "Code Wifi");
		mapFields.put("nom_restaurant", "Nom établissement");
		mapFields.put("ref_table", "Numéro de table");
		mapFields.put("libre", "Saisie libre");
		
		List<ValTypeEnumPersistant> caisseFieldsList = new ArrayList<>();
		for(String key : mapFields.keySet()){
			ValTypeEnumPersistant vt = new ValTypeEnumPersistant();
			vt.setCode(key);
			vt.setLibelle(mapFields.get(key));
			//
			caisseFieldsList.add(vt);
		}
		
		httpUtil.setRequestAttribute("listFamille", familleService.getListeFamille("CU", true, false));
		
		httpUtil.setRequestAttribute("fontWeightArray", new String[][]{{"B", "Gras"}, {"N", "Normal"}});
		httpUtil.setRequestAttribute("correspandanceFieldsList", caisseFieldsList);
	}

	@Override
	public void work_edit(ActionUtil httpUtil) {
		httpUtil.setViewBean(ServiceUtil.persistantToBean(TicketCaisseConfBean.class, ticketCaisseService.findById(TicketCaisseConfPersistant.class, httpUtil.getWorkIdLong())));
		
		httpUtil.setDynamicUrl("/domaine/caisse/back-office/ticketCaisseConf_edit.jsp");
	}
	
	@Override
	public void work_init_update(ActionUtil httpUtil) {
		work_edit(httpUtil);
	}
	
	@Override
	public void work_find(ActionUtil httpUtil) {
		// Recenser les ticketCaisses utilisée et annulée
		RequestTableBean cplxTable = getTableBean(httpUtil, "list_ticketCaisseConf");
		//
		List<TicketCaisseConfPersistant> listData = (List<TicketCaisseConfPersistant>) ticketCaisseService.findByCriteriaByQueryId(cplxTable, "ticketCaisseConf_find");
	   	httpUtil.setRequestAttribute("list_ticketCaisseConf", listData);
	   	
		httpUtil.setDynamicUrl("/domaine/caisse/back-office/ticketCaisseConf_list.jsp");
	}
	
	@Override
	public void work_merge(ActionUtil httpUtil) {
		TicketCaisseConfBean ticketCaisseConfB = (TicketCaisseConfBean) httpUtil.getViewBean();
		
		TicketCaisseConfPersistant attestConfBP = new TicketCaisseConfPersistant();
		attestConfBP.setLibelle(ticketCaisseConfB.getLibelle());
		attestConfBP.setFont_size(ticketCaisseConfB.getFont_size());
		attestConfBP.setBack_pos(ticketCaisseConfB.getBack_pos());
		attestConfBP.setFont_weight(ticketCaisseConfB.getFont_weight());
		attestConfBP.setVertical_space(ticketCaisseConfB.getVertical_space());
		
		attestConfBP.setId(httpUtil.getWorkIdLong());
//		attestConfBP.setOpc_enum_type(ticketCaisseConfB.getOpc_enum_type());
		
		List<TicketCaisseConfDetailPersistant> listDetailIhm = (List<TicketCaisseConfDetailPersistant>) httpUtil.buildListBeanFromMap("correspondance", TicketCaisseConfDetailPersistant.class, "eaiid", "idxIhm",
																"valeur_defaut", "correspondance", "posX", "posY", "famille");
		
		if(listDetailIhm == null || listDetailIhm.size() == 0){
			MessageService.addBannerMessage("Veuillez ajouter au moins un détail.");
			return;
		}
		
		for (TicketCaisseConfDetailPersistant attestDetP : listDetailIhm) {
			if(attestDetP.getPosX() == null){
				MessageService.addBannerMessage("La position X est obligatoire. ligne "+(attestDetP.getIdxIhm()-1));
				return;
			}
			if(attestDetP.getPosY() == null){
				MessageService.addBannerMessage("La position Y est obligatoire. ligne "+(attestDetP.getIdxIhm()-1));
				return;
			}
//			attestDetP.setOpc_agence(ContextAppli.getAgenceBean());
//			attestDetP.setOpc_societe(ContextAppli.getEtablissementBean()); 
		}
		
		// Trier par ihmIDX
		Collections.sort(listDetailIhm, new SortByIhmIdx());
		
		List<TicketCaisseConfDetailPersistant> listDetail = new ArrayList<>();
		if (attestConfBP.getId() != null) {
			TicketCaisseConfPersistant mvmBean = (TicketCaisseConfPersistant) ticketCaisseService.findById(TicketCaisseConfPersistant.class, attestConfBP.getId());
			listDetail = mvmBean.getList_detail();
			listDetail.clear();
		}
		listDetail.addAll(listDetailIhm);
		
		attestConfBP.setList_detail(listDetail); 

		ticketCaisseService.mergeConf(attestConfBP);
		//
		work_find(httpUtil);
	}
	
	@Override
	public void work_delete(ActionUtil httpUtil) {
		ticketCaisseService.deleteConf(httpUtil.getWorkIdLong());
		//
		work_find(httpUtil);
	}
}

class SortByIhmIdx implements Comparator<TicketCaisseConfDetailPersistant>{
	@Override
	public int compare(TicketCaisseConfDetailPersistant o1, TicketCaisseConfDetailPersistant o2) {
		int returnVal = 0;

		if(o1 == null || o2 == null){
			return 0;
		}
		
	    if(o1.getPosY() != null && o2.getPosY()!=null && o1.getPosY().compareTo(o2.getPosY())<0){
	        returnVal =  -1;
	    }else if(o1.getPosY() != null && o2.getPosY()!=null && o1.getPosY().compareTo(o2.getPosY())>0){
	        returnVal =  1;
	    }
	    return returnVal;
	}
}