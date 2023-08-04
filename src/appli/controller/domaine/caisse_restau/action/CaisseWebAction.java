package appli.controller.domaine.caisse_restau.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import appli.controller.domaine.administration.bean.UserBean;
import appli.controller.domaine.caisse.ContextAppliCaisse;
import appli.controller.domaine.caisse.action.CaisseWebBaseAction;
import appli.controller.domaine.caisse.bean.CaisseBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_LIGNE_COMMANDE;
import appli.model.domaine.administration.persistant.AgencementPersistant;
import appli.model.domaine.administration.persistant.UserPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.caisse.persistant.ArticleBalancePersistant;
import appli.model.domaine.caisse.service.IArticle2Service;
import appli.model.domaine.caisse.service.ICaisseMouvementService;
import appli.model.domaine.caisse.service.ICaisseService;
import appli.model.domaine.caisse.service.ICaisseWebService;
import appli.model.domaine.caisse.service.IFamille2Service;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.caisse.service.IMenuCompositionService;
import appli.model.domaine.caisse.service.PrintCuisineSuiteCmdUtil;
import appli.model.domaine.fidelite.dao.IPortefeuille2Service;
import appli.model.domaine.fidelite.persistant.CarteFideliteClientPersistant;
import appli.model.domaine.fidelite.service.ICarteFideliteClientService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.personnel.service.IPlanningService;
import appli.model.domaine.stock.persistant.ArticleClientPrixPersistant;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.service.IArticleService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.util_srv.printCom.ticket.PrintCodeBarreBalanceUtil;
import appli.model.domaine.util_srv.printCom.ticket.PrintTicketUtil;
import appli.model.domaine.vente.persistant.CaisseMouvementArticlePersistant;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import appli.model.domaine.vente.persistant.JourneePersistant;
import appli.model.domaine.vente.persistant.ListChoixDetailPersistant;
import appli.model.domaine.vente.persistant.ListChoixPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionDetailPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ActionUtil;
import framework.controller.ContextGloabalAppli;
import framework.controller.ControllerUtil;
import framework.controller.annotation.WorkController;
import framework.controller.bean.PagerBean;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante.MSG_TYPE;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.IGenericJpaDao;

@WorkController(nameSpace = "caisse-web", bean = CaisseBean.class, jspRootPath = "/domaine/caisse/normal/")
public class CaisseWebAction extends CaisseWebBaseAction {	
	private final static Logger LOGGER = Logger.getLogger(CaisseWebAction.class);
	@Inject
	private IFamille2Service familleService2;
	@Inject
	private IArticleService articleService;
	@Inject
	private IArticle2Service articleService2;
	@Inject
	private IPlanningService planningService;
	@Inject
	private ICarteFideliteClientService carteFideliteClientService;
	@Inject
	private ICaisseMouvementService caisseMvmService;
	@Inject
	private ICaisseWebService caisseWebService;
	@Inject
	private IPortefeuille2Service portefeuilleService2;
	@Inject
	private ICaisseService caisseService;
	@Inject
	private IFamilleService familleService;
	@Inject
	private IUserService userService;
	@Inject
	private IJourneeService journeeService;
	@Inject
	private IMenuCompositionService menuCompositionService;
	@Inject
	private IMenuCompositionService menuService;
	
	/**
	 * @param httpUtil
	 */
	public void finaliserMenuCmdStep(ActionUtil httpUtil) {
		
		// Purge infos steps
		httpUtil.removeUserAttribute("STEP_MNU");
		httpUtil.removeUserAttribute("LIST_SOUS_MENU");
		
		// Données de la session
		manageDataSession(httpUtil);
		//
		httpUtil.removeUserAttribute("CURRENT_MENU_COMPOSITION");
		httpUtil.removeUserAttribute("CURRENT_MENU_NUM");

		httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
	}
	
	/**
	 * @param httpUtil
	 */
	public void selectTable(ActionUtil httpUtil) { 
		CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
		if(CURRENT_COMMANDE.getId() != null && CURRENT_COMMANDE.getLast_statut().equals(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString())) {
			MessageService.addGrowlMessage("Mise à jour impossible", "Cette commande est marquée comme livrée. <br/>Elle ne peut plus être modifiée.");
			return;
		}
		
		String refTable = httpUtil.getParameter("ref_tab");
		httpUtil.setUserAttribute("CURRENT_TABLE_REF", refTable);
		
		httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
	}
	
  
	   /** ------------------------------------------------ historique ----------------------------------------*/ 
		public void getClientsTable(ActionUtil httpUtil) {
			String tab = httpUtil.getParameter("curr_tab");
			CaisseMouvementPersistant cmd = caisseWebService.getMouvementByTable(ContextAppliCaisse.getJourneeBean().getId(), tab, STATUT_CAISSE_MOUVEMENT_ENUM.TEMP);
			
			if(cmd == null){
				CaisseMouvementPersistant currCmd = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
				for(CaisseMouvementArticlePersistant detArt : currCmd.getList_article()){
					if(detArt.getRef_table() != null && detArt.getRef_table().equals(tab)){
						cmd = currCmd;
						break;
					}
				}
			}
			List<Integer> listIdxCli = new ArrayList<>();
			if(cmd != null){
				for(CaisseMouvementArticlePersistant detArt : cmd.getList_article()){
					if(detArt.getIdx_client() != null && !listIdxCli.contains(detArt.getIdx_client())){
						listIdxCli.add(detArt.getIdx_client());
					}
				}
			}
			Collections.sort(listIdxCli);
			String data = "";
			//
			for(int i : listIdxCli){
				data = data + "<option value='"+i+"'>CLIENT "+i+"</option>";
			}
			if(StringUtil.isEmpty(data)){// Min 1 client
				data = data + "<option value='1'>CLIENT 1</option>";	
			}
			httpUtil.writeResponse(data);
		}
		
		private boolean checkUser(ActionUtil httpUtil) {
			String badge = (String)ControllerUtil.getParam(httpUtil.getRequest(), "qte.tkn");
			   
		   boolean isBadge = StringUtil.isNotEmpty(badge);
			UserBean userBean = null;
			//
			if(isBadge){
				userBean = userService.getUserByBadge(badge.trim());
				//
				if(userBean == null){
					MessageService.addBannerMessage("Ce badge n'a pas été encore enregistré.");
					return false;
				}
			} else {
				if(StringUtil.isEmpty(httpUtil.getParameter("unlockQte.password"))) {
					MessageService.addFieldMessage("unlockQte.password", "Le mot depasse est obligatoire.");
					return false;
				}
				if(StringUtil.isEmpty(httpUtil.getParameter("unlockQte.login"))) {
					MessageService.addFieldMessage("unlockQte.login", "Le login est obligatoire.");
					return false;
				}
				
				Long userId = Long.valueOf(httpUtil.getParameter("unlockQte.login"));
				String pw = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey()).encrypt(httpUtil.getParameter("unlockQte.password"));
				userBean = userService.findById(userId);
				
				if(!pw.equals(userBean.getPassword())) {
					MessageService.addBannerMessage("Le mot de passe est erroné.");
					return false;
				}
			}
			
			return true;
		}
		
		/**
		 * @param httpUtil
		 */
		public void transfertArtMnu(ActionUtil httpUtil) {
			String clientTraget = httpUtil.getParameter("target_client");
			
			if(StringUtil.isEmpty(clientTraget)) {
				MessageService.addBannerMessage("Veuillez sélectionner un client");
				return;
			}
			
			boolean isConfirmTransfert = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_TRANSFERT_ART"));
			if(isConfirmTransfert && !checkUser(httpUtil)) {
				return;
			}
			boolean isForceEmptyTable = StringUtil.isTrue(httpUtil.getParameter("target_emptyTable"));
			String refTabeTraget = httpUtil.getParameter("target_tbl");
			boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
			boolean isPortefeuille =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("PORTEFEUILLE"));			
			List<CaisseMouvementArticlePersistant> listParents = new ArrayList<>();
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			CaisseMouvementPersistant caiseMvmDb = familleService.findById(CaisseMouvementPersistant.class, CURRENT_COMMANDE.getId());
			BigDecimal mttTotalNet = BigDecimal.ZERO;

			if(caiseMvmDb != null) {
				CaisseMouvementArticlePersistant cmvP = (CaisseMouvementArticlePersistant) httpUtil.getUserAttribute("CURR_ART_TRANS");
				if(cmvP != null ) {
					for( CaisseMouvementArticlePersistant art : caiseMvmDb.getList_article()) {
						if(art.getId().equals(cmvP.getId())) {
							art.setMtt_total(BigDecimal.ZERO);
						}
						mttTotalNet = mttTotalNet.add(art.getMtt_total());
					}
				}
				caiseMvmDb.setMtt_commande(mttTotalNet);
				caiseMvmDb.setMtt_commande_net(mttTotalNet);
				 
				caisseMvmService.mergeEntity(caiseMvmDb);
			}
//			   if(CURRENT_COMMANDE != null) {
//				   if(CURRENT_COMMANDE.getList_article().size() > 0 ) {
//					   var CURRENT_COMMANDE.getList_article()
//				   }
//			   }
			
			STATUT_CAISSE_MOUVEMENT_ENUM statut = STATUT_CAISSE_MOUVEMENT_ENUM.TEMP;
			BigDecimal qteTransfert = BigDecimalUtil.get(httpUtil.getParameter("quantiteTrans_cust"));
			qteTransfert = (BigDecimalUtil.isZero(qteTransfert) ? BigDecimalUtil.get(1) : qteTransfert);
			
			if(StringUtil.isEmpty(clientTraget) && StringUtil.isEmpty(refTabeTraget)){
				httpUtil.addJavaScript("$('#close_modal').trigger('click');");
				httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
				return;
			}
			
			CaisseMouvementArticlePersistant currMvmDet = (CaisseMouvementArticlePersistant)httpUtil.getUserAttribute("CURR_ART_TRANS");
			CaisseMouvementPersistant TARGET_COMMANDE = null;
			
			if(StringUtil.isNotEmpty(refTabeTraget)){
				// On regarde d'abord dans la commande en cours
				for(CaisseMouvementArticlePersistant arts : CURRENT_COMMANDE.getList_article()){
					if(refTabeTraget.equals(arts.getRef_table())){
						TARGET_COMMANDE = CURRENT_COMMANDE;
						break;
					}
				}
				
				// Si non historique
				if(TARGET_COMMANDE == null && !isForceEmptyTable){
					TARGET_COMMANDE = caisseWebService.getMouvementByTable(
							ContextAppliCaisse.getJourneeBean().getId(), 
							refTabeTraget,
							STATUT_CAISSE_MOUVEMENT_ENUM.TEMP, STATUT_CAISSE_MOUVEMENT_ENUM.PREP, STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE 
						);
					
					if(TARGET_COMMANDE != null && StringUtil.isNotEmpty(TARGET_COMMANDE.getLast_statut())) {
						statut = STATUT_CAISSE_MOUVEMENT_ENUM.valueOf(TARGET_COMMANDE.getLast_statut());
					}
				}
				if(TARGET_COMMANDE == null){// Table destination ne correspond à aucune commande
					CaisseMouvementPersistant newCmd = new CaisseMouvementPersistant();
			        newCmd.setRef_commande(""+System.currentTimeMillis());
			        newCmd.setType_commande(CURRENT_COMMANDE.getType_commande());//même type que l'origine
			        newCmd.setDate_vente(new Date());
			        newCmd.setOpc_caisse_journee(ContextAppliCaisse.getJourneeCaisseBean());
			        //
			        newCmd.setList_article(new ArrayList<>());
			        newCmd.setList_offre(new ArrayList<>());
					newCmd.setMax_idx_client(1);
					newCmd.setOpc_user(ContextAppli.getUserBean());
					
					String modeTravail =  ContextGloabalAppli.getGlobalConfig("MODE_TRAVAIL_CUISINE");
					modeTravail = (StringUtil.isEmpty(modeTravail) ? "PO" : modeTravail);
					boolean isPrintOnly = "PO".equals(modeTravail) || "PE".equals(modeTravail);// PrintOnly or PrintEcran
					
					if(isPrintOnly) {
						statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
					} else {
						statut = STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE;					
					}
					newCmd.setLast_statut(statut.toString());
					
					TARGET_COMMANDE = caisseWebService.createMouvementCaisse(newCmd, statut, isPoints);
					if(TARGET_COMMANDE.getOpc_client() != null){
		            	portefeuilleService2.majSoldePortefeuilleMvm(TARGET_COMMANDE.getOpc_client().getId(), "CLI");
		            }
				}
			} else {
				TARGET_COMMANDE = CURRENT_COMMANDE;
			}

			boolean isCmdExterne = !TARGET_COMMANDE.getRef_commande().equals(CURRENT_COMMANDE.getRef_commande());
			boolean isQteDiminue = (currMvmDet.getQuantite()!=null?currMvmDet.getQuantite().compareTo(qteTransfert) > 0 : false);
			BigDecimal prixUnitaire = BigDecimalUtil.divide(currMvmDet.getMtt_total(), currMvmDet.getQuantite());
			//
			if(currMvmDet.getType_ligne().equals(TYPE_LIGNE_COMMANDE.MENU.toString())){// Menu ------------
				if(isCmdExterne){//-------COMMANDE EXTERNE------------------
			        for (Iterator<CaisseMouvementArticlePersistant> iterator = CURRENT_COMMANDE.getList_article().iterator(); iterator.hasNext();) {
			        	CaisseMouvementArticlePersistant cmd = iterator.next();
						if(cmd.getMenu_idx() != null && cmd.getMenu_idx().equals(currMvmDet.getMenu_idx())){
							CaisseMouvementArticlePersistant duppCmd = (CaisseMouvementArticlePersistant) ReflectUtil.cloneBean(cmd);
							duppCmd.setId(null);
							duppCmd.setOpc_mouvement_caisse(TARGET_COMMANDE);
							
							if(StringUtil.isNotEmpty(refTabeTraget)) {
								duppCmd.setRef_table(refTabeTraget);
							}
							
							TARGET_COMMANDE.getList_article().add(duppCmd);
							
							iterator.remove();// Supprimer de la commande initiale	
						}
					}
					CaisseMouvementArticlePersistant duppCmd = (CaisseMouvementArticlePersistant) ReflectUtil.cloneBean(currMvmDet);
					duppCmd.setIdx_client(Integer.valueOf(clientTraget));
					duppCmd.setRef_table(refTabeTraget);
					TARGET_COMMANDE.getList_article().add(duppCmd);
				} else{//-------MEME COMMANDE-------------------
					for(CaisseMouvementArticlePersistant cmd : CURRENT_COMMANDE.getList_article()){
						if(cmd.getMenu_idx() != null && cmd.getMenu_idx().equals(currMvmDet.getMenu_idx())){
		            	   cmd.setIdx_client(Integer.valueOf(clientTraget));
		            	   cmd.setRef_table(refTabeTraget);
						}
					}
					currMvmDet.setIdx_client(Integer.valueOf(clientTraget));
					currMvmDet.setRef_table(refTabeTraget);
				}
	       } else if(currMvmDet.getType_ligne().equals(TYPE_LIGNE_COMMANDE.ART.toString())){// Article --------------
	    	   // AJouter les parents
	    	   listParents = getListParents(clientTraget, CURRENT_COMMANDE, TARGET_COMMANDE, listParents, currMvmDet);
	    	   // Inverser l'ordre
	    	   Collections.reverse(listParents);
	    	   //
	    	   for(CaisseMouvementArticlePersistant parentDet : listParents){
	    		   CaisseMouvementArticlePersistant clonedLine = (CaisseMouvementArticlePersistant) ReflectUtil.cloneBean(parentDet);
	    		   clonedLine.setIdx_client(Integer.valueOf(clientTraget));
	    		   clonedLine.setRef_table(refTabeTraget);
	    		   clonedLine.setOpc_mouvement_caisse(TARGET_COMMANDE);
	    		   clonedLine.setId(null);
	    		   
	    		   TARGET_COMMANDE.getList_article().add(clonedLine);
	    	   }
	    	   
	    	   // Affecter le client destination
	    	   CaisseMouvementArticlePersistant existedDet = null;
	    	   CaisseMouvementPersistant TAR_COMMANDE = (isCmdExterne ? TARGET_COMMANDE : CURRENT_COMMANDE);
    		   for(CaisseMouvementArticlePersistant cmd2 : TAR_COMMANDE.getList_article()){
    	 		   if((currMvmDet.getOpc_menu()==null || (cmd2.getOpc_menu() != null && currMvmDet.getOpc_menu().getId().equals(cmd2.getOpc_menu().getId())))
    	 				   && currMvmDet.getCode().equals(cmd2.getCode()) 
    	 				   && clientTraget.equals(""+cmd2.getIdx_client())){
    	 			  existedDet = cmd2;
    	 			  break;
    	 		   }
    		   }
    		   if(existedDet == null){// Detail non existant ------------------
	    			// Ajouter l'article à la fin
    			   if(isCmdExterne || isQteDiminue){//------Cmd externe------------------
    				    CaisseMouvementArticlePersistant duppCmd = (CaisseMouvementArticlePersistant) ReflectUtil.cloneBean(currMvmDet);
						duppCmd.setIdx_client(Integer.valueOf(clientTraget));
						if(StringUtil.isNotEmpty(refTabeTraget)){
							duppCmd.setRef_table(refTabeTraget);
						}
						duppCmd.setQuantite(qteTransfert);
						duppCmd.setMtt_total(BigDecimalUtil.multiply(prixUnitaire, qteTransfert));
						duppCmd.setOpc_mouvement_caisse(TARGET_COMMANDE);
						duppCmd.setId(null);
						TARGET_COMMANDE.getList_article().add(duppCmd);
						
						if(isQteDiminue) {// Changer qte origine
		    			    currMvmDet.setQuantite(BigDecimalUtil.substract(currMvmDet.getQuantite(), qteTransfert));
		    			    currMvmDet.setMtt_total(BigDecimalUtil.multiply(prixUnitaire, currMvmDet.getQuantite()));
						}
    			   } else{//-------Cmd interne---------------------------
    					currMvmDet.setIdx_client(Integer.valueOf(clientTraget));
	    			    if(StringUtil.isNotEmpty(refTabeTraget)){
	    			    	currMvmDet.setRef_table(refTabeTraget); 
	    			    }
	    			    currMvmDet.setQuantite(qteTransfert);
	    			    currMvmDet.setMtt_total(BigDecimalUtil.multiply(prixUnitaire, qteTransfert));
	    			    // Mettre en fin de liste pour avoir le tri correctement
	    			    TARGET_COMMANDE.getList_article().remove(currMvmDet);
	    			    TARGET_COMMANDE.getList_article().add(currMvmDet);
    			   }
    		   } else{// Existant -------------------
    			   existedDet.setQuantite(BigDecimalUtil.add(existedDet.getQuantite(), qteTransfert));
    			   existedDet.setMtt_total(BigDecimalUtil.multiply(prixUnitaire, existedDet.getQuantite()));
    			   // Supprimer le détail d'origine
    			   if(isQteDiminue) {
    				   currMvmDet.setQuantite(BigDecimalUtil.substract(currMvmDet.getQuantite(), qteTransfert));
    				   currMvmDet.setMtt_total(BigDecimalUtil.multiply(prixUnitaire, currMvmDet.getQuantite()));
    			   } else {
	    			   deleteRow(httpUtil, CURRENT_COMMANDE, 
	    					   currMvmDet.getCode(), 
	    					   currMvmDet.getType_ligne(), 
	    					   currMvmDet.getElementId(), 
	    					   currMvmDet.getParent_code(), 
	    					   currMvmDet.getMenu_idx(), 
	    					   currMvmDet.getIdx_client(),
	    					   currMvmDet.getRef_table(),
	    					   true);
    			   }
    		   }
    	   }
			
			if(isCmdExterne){
				if(isQteDiminue) {
//					currMvmDet.setQuantite(BigDecimalUtil.substract(currMvmDet.getQuantite(), qteTransfert));
//					currMvmDet.setMtt_total(BigDecimalUtil.multiply(prixUnitaire, currMvmDet.getQuantite()));
				} else {
					deleteRow(httpUtil, CURRENT_COMMANDE, 
							currMvmDet.getCode(), 
							currMvmDet.getType_ligne(), 
							currMvmDet.getElementId(), 
							currMvmDet.getParent_code(), 
							currMvmDet.getMenu_idx(), 
							currMvmDet.getIdx_client(),
							currMvmDet.getRef_table(),
							true);
				}
			}
			
			sortAndAddCommandeLigne(httpUtil, CURRENT_COMMANDE);
			if(isCmdExterne){
				caisseWebService.createMouvementCaisse(TARGET_COMMANDE, statut, isPoints);
				if(TARGET_COMMANDE.getOpc_client() != null){
					portefeuilleService2.majSoldePortefeuilleMvm(TARGET_COMMANDE.getOpc_client().getId(), "CLI");
	            }
				sortAndAddCommandeLigne(httpUtil, TARGET_COMMANDE);
				
				majTotalMontantCommande(httpUtil, TARGET_COMMANDE);
			}
			
			majTotalMontantCommande(httpUtil, CURRENT_COMMANDE);
			
			MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "", "L'élément est trasféré avec succès.");
			
			httpUtil.addJavaScript("$('#close_modal').trigger('click');");
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		private List<CaisseMouvementArticlePersistant> getListParents(String idxClientTarget, 
				CaisseMouvementPersistant CURRENT_COMMANDE, 
				CaisseMouvementPersistant TARGET_COMMANDE,
				List<CaisseMouvementArticlePersistant> listParents, 
				CaisseMouvementArticlePersistant currCmd){
			
			for(CaisseMouvementArticlePersistant cmd : CURRENT_COMMANDE.getList_article()){
	 		   if((currCmd.getOpc_menu()==null || (cmd.getOpc_menu() != null && currCmd.getOpc_menu().getId().equals(cmd.getOpc_menu().getId())))
	 				   && cmd.getCode().equals(currCmd.getParent_code())){
	 			  
	 			   // Vérifier non existance
	    		   boolean isExist = false;
	    		   for(CaisseMouvementArticlePersistant cmd2 : TARGET_COMMANDE.getList_article()){
	    	 		   if((cmd.getOpc_menu()==null || (cmd2.getOpc_menu() != null && cmd.getOpc_menu().getId().equals(cmd2.getOpc_menu().getId())))
	    	 				   && cmd.getCode().equals(cmd2.getCode()) 
	    	 				   && idxClientTarget.equals(""+cmd2.getIdx_client())){
	    	 			  isExist = true;
	    	 			  break;
	    	 		   }
	    		   }
	    		   if(!isExist){
	    			   listParents.add(cmd);
	    		   }
	    		   getListParents(idxClientTarget, CURRENT_COMMANDE, TARGET_COMMANDE, listParents, cmd);
	 		   }
	 	   }
			return listParents;
		}
		
		public void initDupMnu(ActionUtil httpUtil) {
	       CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
	       if(CURRENT_COMMANDE.getId() != null && CURRENT_COMMANDE.getLast_statut().equals(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString())) {
	    	    MessageService.addGrowlMessage("Mise à jour impossible", "Cette commande est marquée comme livrée. <br/>Elle ne peut plus être modifiée.");
				return;
			}
	       
	       Long elementId = httpUtil.getLongParameter("elm");
	       String parentCode = httpUtil.getParameter("par");
	       String menuIdx = httpUtil.getParameter("mnu");
	       String clientIdx = httpUtil.getParameter("cli");
	       
	       if(httpUtil.getParameter("sub") == null){
	    	   httpUtil.setRequestAttribute("params", "elm="+elementId+"&par="+parentCode+"&mnu="+menuIdx+"&cli="+clientIdx);
	    	   httpUtil.setDynamicUrl("/domaine/caisse_restau/normal/dup-menu.jsp");
	    	   return;
	       }
	       
	       String qteStr = httpUtil.getParameter("quantite_custom");
	       int qte = StringUtil.isEmpty(qteStr) ? 1 : Integer.valueOf(qteStr);
		
	       List<CaisseMouvementArticlePersistant> listDetail = CURRENT_COMMANDE.getList_article();
	       
	       List<CaisseMouvementArticlePersistant> listDetMnuOri = new ArrayList<>();
	       for (CaisseMouvementArticlePersistant cmd : listDetail) {
               if(cmd.getIdx_client().toString().equals(clientIdx) && cmd.getMenu_idx() != null && cmd.getMenu_idx().equals(menuIdx)){
            	   listDetMnuOri.add(cmd);
               }
           }
	       
	       for(int i=0; i<qte; i++){
	    	   String refMnu = DateUtil.dateToString(new Date(), "HHSSS")+i;
	    	   refMnu = refMnu.substring(refMnu.length()-4);
				
		       for (CaisseMouvementArticlePersistant cmd : listDetMnuOri) {
		    	   CaisseMouvementArticlePersistant clonedCmd = (CaisseMouvementArticlePersistant) ReflectUtil.cloneBean(cmd);
		    	   clonedCmd.setMenu_idx(refMnu);
		    	   
		    	   CURRENT_COMMANDE.getList_article().add(clonedCmd);
	           }
	       }
	       sortAndAddCommandeLigne(httpUtil, CURRENT_COMMANDE);
	       majTotalMontantCommande(httpUtil, CURRENT_COMMANDE);
	       
	       httpUtil.setDynamicUrl(httpUtil.getUserAttribute("PATH_JSP_CAISSE")+"/plan-list.jsp");
	       
	       httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 */
		public void initPlan(ActionUtil httpUtil) {
			httpUtil.setFormReadOnly(false);
			
			//
			List<?> listAgencement = familleService.findAll(AgencementPersistant.class, Order.asc("emplacement"));
			httpUtil.setRequestAttribute("listAgencement", listAgencement);
			boolean isServeurProfil = "SERVEUR".equals(ContextAppli.getUserBean().getOpc_profile().getCode());
			//
			if(StringUtil.isTrue(httpUtil.getParameter("isrp"))) {
				httpUtil.setUserAttribute("PLAN_MODE", "REP");
			} else if(httpUtil.getParameter("iscli") != null){
				httpUtil.setUserAttribute("PLAN_MODE", "TRA");
				httpUtil.setUserAttribute("CURR_TRA_CLI", httpUtil.getParameter("idx_cli"));
			} else if(httpUtil.getParameter("isrp") != null){
				httpUtil.setUserAttribute("PLAN_MODE", "STD");
			} else if(httpUtil.getParameter("istab") != null){
				httpUtil.setUserAttribute("PLAN_MODE", "CHNG_TAB");
				httpUtil.setUserAttribute("CURR_CHANG_TAB", httpUtil.getParameter("ref_tab"));
			}
			 
			boolean isReprise = "REP".equals(httpUtil.getUserAttribute("PLAN_MODE"));
			if(isReprise){
				httpUtil.setRequestAttribute("listCaisse", caisseService.getListCaisseActive(ContextAppli.TYPE_CAISSE_ENUM.CAISSE.toString(), true));
				httpUtil.setRequestAttribute("listServeur", userService.getListUserActifsByProfile("SERVEUR"));
			}
			
			Long caisseId = null;
			Long serveurId = null;
			//
			if(isReprise){
				String serveur = httpUtil.getParameter("serveur");
				if(StringUtil.isNotEmpty(serveur)) { 
					serveurId = Long.valueOf(serveur);
					httpUtil.setRequestAttribute("currServeurId", serveurId);
				}
				//
				String caisseIdSt = httpUtil.getParameter("caisse.id");
				if(StringUtil.isNotEmpty(caisseIdSt)) { 
					String decrypted  = EncryptionUtil.decrypt(caisseIdSt);
					if(NumericUtil.isLong(decrypted)) {// Car n'est pas decrypé correctement dans la pagination
						caisseId = Long.valueOf(decrypted);
					} else {
						caisseId = Long.valueOf(caisseIdSt);
					}
				} else if(isServeurProfil) {
					caisseId = ContextAppliCaisse.getCaisseBean().getId();
				}
				httpUtil.setRequestAttribute("currCaisseId", caisseId);
			}
			
			Long emplacementId = httpUtil.getLongParameter("emplacement");
			AgencementPersistant agenementP = null;
			if(emplacementId != null) {
				agenementP = (AgencementPersistant)familleService.findById(AgencementPersistant.class, emplacementId);
			} else {
				agenementP = (AgencementPersistant)(listAgencement.isEmpty() ? null : listAgencement.get(0));
			}
			httpUtil.setRequestAttribute("agencement", agenementP);
			List<String> list1 = caisseWebService.getListTableOccupee(
					ContextAppliCaisse.getJourneeBean().getId(), 
					caisseId, 
					serveurId);
			
			
			Calendar calendar1 = Calendar.getInstance();
	        calendar1.add(Calendar.HOUR_OF_DAY, 0);
	        Calendar calendar2 = Calendar.getInstance();
	        calendar2.add(Calendar.HOUR_OF_DAY, 6);
			List<String> list2 = planningService.checkTableReserved(calendar1.getTime(), calendar2.getTime());
			//combine list1 + list 2
			for (String det : list2) {
				if(!list1.contains(det) && det != null){
					list1.add(det);
				}
			}
			httpUtil.setRequestAttribute("listTableTemp", list1);
			
			boolean isConfirmTransfert = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_TRANSFERT_ART"));
			if(isConfirmTransfert) {
				   List<UserPersistant> finalListUsers = new ArrayList<>();
				   List<UserPersistant> listUsers = userService.findAllUser(true);
				   for (UserPersistant userPersistant : listUsers) {
						if(userPersistant.isInProfile("MANAGER")
								|| userPersistant.isInProfile("ADMIN")) {
						finalListUsers.add(userPersistant);
						}
					}
				   httpUtil.setRequestAttribute("listUser", finalListUsers);
			}
			//
			httpUtil.setDynamicUrl(httpUtil.getUserAttribute("PATH_JSP_CAISSE")+"/plan-list.jsp");
		}
		/**
		 * @param httpUtil
		 */
		public void selectPlan(ActionUtil httpUtil) {
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			boolean isReprise = "REP".equals(httpUtil.getUserAttribute("PLAN_MODE"));
			boolean isTransfert = "TRA".equals(httpUtil.getUserAttribute("PLAN_MODE"));
			boolean isChangementTable = "CHNG_TAB".equals(httpUtil.getUserAttribute("PLAN_MODE"));
			boolean isConfirmTransfert = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SECURE_TRANSFERT_ART"));
			boolean isMultiTable = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("TABLE_MULTIPLE"));
			boolean isFromCalc = (httpUtil.getParameter("is_calc") != null);
			String tableRef = httpUtil.getParameter("ref");			
			
			if(isFromCalc) {
				List<CaisseMouvementPersistant> listMvmTemp = caisseWebService.getListMouvementTemp(ContextAppliCaisse.getJourneeBean().getId(), null, null);
				//
				CaisseMouvementPersistant cmdTable = null;
				for(CaisseMouvementPersistant cmd : listMvmTemp){
					for(CaisseMouvementArticlePersistant det : cmd.getList_article()){
			            if(StringUtil.isNotEmpty(det.getRef_table()) 
			            		&& det.getRef_table() != null
			            		&& det.getRef_table().trim().toUpperCase().equals(tableRef)){
			            	cmdTable = cmd;
			            	break;
			            }
					}
					if(cmdTable != null){
						break;
					}
				}
				isReprise = (cmdTable == null ? false : true);
			}
			
			if(isConfirmTransfert && isTransfert && !checkUser(httpUtil)) {
				return;
			}
			
			if(!isMultiTable && CURRENT_COMMANDE != null) {
				String currTbl = null;
				for(CaisseMouvementArticlePersistant det : CURRENT_COMMANDE.getList_article()) {
					if(StringUtil.isNotEmpty(det.getRef_table()) && !BooleanUtil.isTrue(det.getIs_annule())) {
						currTbl = det.getRef_table();
						break;
					}
				}
				if(currTbl != null && !currTbl.equals(tableRef)) {
					if(isFromCalc) {
						MessageService.addGrowlMessage("", "<h3>Une table est déjà sélectionnée pour cette commande.</h3>");
					} else {
						MessageService.addBannerMessage("Une table est déjà sélectionnée pour cette commande.");						
					}
					return;					
				}
			}	
			
			if(StringUtil.isEmpty(tableRef) && StringUtil.isNotEmpty(httpUtil.getParameter("is_saisie"))) {// Si saisie dans champs text
				tableRef = httpUtil.getParameter("table-sasie");
				if(StringUtil.isEmpty(tableRef)) {
					if(isFromCalc) {
						MessageService.addGrowlMessage("", "<h3>Veuillez saisir un numéro de table.</h3>");
					} else {
						MessageService.addBannerMessage("Veuillez saisir un numéro de table.");
					}
					return;
				} else {
					tableRef = tableRef.trim().toUpperCase();
				}
			}
			
			// Interdir d'attribuer la  meme table à plusieurs commandes
			if(!isReprise && !isTransfert){
				
				Calendar calendar1 = Calendar.getInstance();
		        calendar1.add(Calendar.HOUR_OF_DAY, 0);
		        Calendar calendar2 = Calendar.getInstance();
		        calendar2.add(Calendar.HOUR_OF_DAY, 6);
				List<String> list2 = planningService.checkTableReserved(calendar1.getTime(), calendar2.getTime());
				if(list2.size() > 0 && tableRef != null && list2.contains(tableRef)){
						MessageService.addGrowlMessage("", "<h3>Cette table est déjà utilisée pour la commande ** <b>"+CURRENT_COMMANDE.getRef_commande()+"</b> **</h3>");
                        return;
					
				}
				
				CaisseMouvementPersistant cmd = caisseWebService.getMouvementByTable(
									ContextAppliCaisse.getJourneeBean().getId(), 
									tableRef, 
									STATUT_CAISSE_MOUVEMENT_ENUM.TEMP);
				if(cmd != null && cmd.getRef_commande() != null && !cmd.getRef_commande().equals(CURRENT_COMMANDE.getRef_commande())){
					MessageService.addGrowlMessage("", "<h3>Cette table est déjà utilisée pour la commande ** <b>"+CURRENT_COMMANDE.getRef_commande()+"</b> **</h3>");
                    return;
				}
			}
			
			// Transfert d'un client vers une table ------------------------------------------
			if(isTransfert){
				Integer idxCli = Integer.valueOf(""+httpUtil.getUserAttribute("CURR_TRA_CLI"));
				String refTableTarget = httpUtil.getParameter("ref");
				
				// Selectionner la table de ce client
				if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getList_article() != null){
					for(CaisseMouvementArticlePersistant caisseMvmP : CURRENT_COMMANDE.getList_article()){
						if(idxCli.equals(caisseMvmP.getIdx_client())){
							caisseMvmP.setRef_table(refTableTarget);
						}
					}
				}
				httpUtil.addJavaScript("$('#close_modal').trigger('click');");
				httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
				return;
			} else if(isChangementTable){
				String reftable = (String) httpUtil.getUserAttribute("CURR_CHANG_TAB");
				String refTableTarget = httpUtil.getParameter("ref");
				
				// Selectionner la table de ce client
				if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getList_article() != null){
					for(CaisseMouvementArticlePersistant caisseMvmP : CURRENT_COMMANDE.getList_article()){
						if(reftable.equals(caisseMvmP.getRef_table())){
							caisseMvmP.setRef_table(refTableTarget);
						}
					}
				}
				httpUtil.addJavaScript("$('#close_modal').trigger('click');");
				httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
				return;
			}
			
			
			Long caisseId = null;
			String caisseIdSt = httpUtil.getParameter("cais");
			if(StringUtil.isNotEmpty(caisseIdSt)) { 
				String decrypted  = EncryptionUtil.decrypt(caisseIdSt);
				if(NumericUtil.isLong(decrypted)) {// Car n'est pas decrypé correctement dans la pagination
					caisseId = Long.valueOf(decrypted);
				} else {
					caisseId = Long.valueOf(caisseIdSt);
				}
			} else if(caisseIdSt == null){// Savoir si on vient d'un submit ou pas
				//caisseId = ContextAppliCaisse.getCaisseBean().getId();
			}
			List<CaisseMouvementPersistant> listMvmTemp = caisseWebService.getListMouvementTemp(ContextAppliCaisse.getJourneeBean().getId(), caisseId, null);
			//
			CaisseMouvementPersistant cmdTable = null;
			for(CaisseMouvementPersistant cmd : listMvmTemp){
				for(CaisseMouvementArticlePersistant det : cmd.getList_article()){
		            if(StringUtil.isNotEmpty(det.getRef_table()) 
		            		&& det.getRef_table() != null
		            		&& det.getRef_table().trim().toUpperCase().equals(tableRef)){
		            	cmdTable = cmd;
		            	break;
		            }
				}
				if(cmdTable != null){
					break;
				}
			}
			
			if(!isReprise){
//				List<String> listTable = caisseWebService.getListTableOccupee(ContextRestaurant.getJourneeBean().getId(), caisseId, null);
//	            if(listTable.contains(tableRef)){
//	                MessageService.addGrowlMessage("Table occupée", "Cette table est déjà utilisée dans une autre commande.");
//	                return;
//	            }
	            httpUtil.setUserAttribute("CURRENT_TABLE_REF", tableRef);// Index client
	        } else{
	        	if(StringUtil.isEmpty(tableRef) || cmdTable == null) {
					MessageService.addGrowlMessage("", "<h3>Cet emplacement ne correspond à aucune commande.<h3>");
					return;
				}
	        	
	        	if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getList_article().size() > 0) {
	        		MessageService.addGrowlMessage("Commande en cours", "<h3>Vous devez d'abord annuler ou mettre en attente la commande en cours.</h3>");
	        		return;
	        	}
	        	
	        	if(!isTransfert && cmdTable != null) {
					// Vérifier chariot ---------------------------------
			    	CaisseMouvementPersistant caisseMvm = (CaisseMouvementPersistant) familleService.findById(CaisseMouvementPersistant.class, cmdTable.getId());
			    	
			    	if(caisseMvm != null) {//-----------------LOCK----------------------
						UserPersistant userLock = caisseMvm.getOpc_user_lock();
			    		if(userLock != null) {
			    			MessageService.addGrowlMessage("", "<h3>Cette commande est déjà reprise par ** "+userLock.getLogin()+" **.</h3>");
			    			return;
			    		}
			    		if(ContextAppli.getUserBean() != null && ContextAppli.getUserBean().getId() != null) {
			    			caisseMvm.setOpc_user_lock(userService.findById(UserPersistant.class, ContextAppli.getUserBean().getId()));
			    		}
			    		caisseService.mergeEntity(caisseMvm);
			    	}
			    	//---------------------------------------------------------------------
				}
	        	
	        	
	            // Reset infos
	            resetInfosSession(httpUtil);
	        	httpUtil.setUserAttribute("CURRENT_COMMANDE", cmdTable);
	        	
	        	boolean isCaisseFrom = ContextAppli.APPLI_ENV.cais.toString().equals(httpUtil.getUserAttribute("CURRENT_ENV"));
				if(isCaisseFrom){
	        		httpUtil.addJavaScript("resetDetailCmd();");
	        	}
	        }
			
			if(!isReprise){
				// Ajouter automatiquement un nouveau client dans le cas d'ajout de table
				Integer max_idx_client = ((CURRENT_COMMANDE.getMax_idx_client()==null || CURRENT_COMMANDE.getList_article().size()==0)
						? 1 : CURRENT_COMMANDE.getMax_idx_client());//+1);
				httpUtil.setUserAttribute("CURRENT_IDX_CLIENT", max_idx_client);
				CURRENT_COMMANDE.setMax_idx_client(max_idx_client);
				// Affecter les article sans client à cette table ---------------------------------------------------
				if(CURRENT_COMMANDE != null && CURRENT_COMMANDE.getList_article() != null){
					for(CaisseMouvementArticlePersistant caisseMvmP : CURRENT_COMMANDE.getList_article()){
						if(httpUtil.getRequestAttribute("isFromCalender") != null) {
							if(StringUtil.isEmpty(caisseMvmP.getRef_table())){
								caisseMvmP.setRef_table(tableRef);
							}
							return;
						}
						if(StringUtil.isEmpty(caisseMvmP.getRef_table())){
							caisseMvmP.setRef_table(tableRef);
						}
						// Affecter les article sans client à ce client
						if(caisseMvmP.getIdx_client() == null){
							caisseMvmP.setIdx_client(max_idx_client); 
						}
					}
				}
			} else{
				// Restituer la table + client
		    	Integer idxClient = null;
		    	String refTable = null;
		    	for(CaisseMouvementArticlePersistant caisseMvmP : cmdTable.getList_article()){
		    		if(BooleanUtil.isTrue(caisseMvmP.getIs_annule())){
		    			continue;
		    		}
		    		if(StringUtil.isNotEmpty(caisseMvmP.getIdx_client()) && tableRef.equals(caisseMvmP.getRef_table().trim().toUpperCase())){
		    			idxClient = caisseMvmP.getIdx_client();
		    			refTable = caisseMvmP.getRef_table();
		    		}
		    	}
		    	httpUtil.setUserAttribute("CURRENT_TABLE_REF", refTable);
		    	httpUtil.setUserAttribute("CURRENT_IDX_CLIENT", idxClient);
			}
			httpUtil.addJavaScript("$('#close_modal').trigger('click');");
			httpUtil.addJavaScript("$('#home_lnk').trigger('click');");// trigger home
			
	    	
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		/**
		 * @param httpUtil
		 */
		public void addMouvementGroupLevelsCmd (ActionUtil httpUtil) {	
			IGenericJpaDao genriqueDao = familleService.getGenriqueDao();
			List<String> listNav = (List<String>)httpUtil.getUserAttribute("HISTORIQUE_NAV");
			
			String parentCode = null;
			Integer parenLevel = null;
			for (String navId : listNav) {
				if(navId == null) {
					continue;
				}
				
				Long menuId = null;
				
				if(navId.startsWith("MEN_COMP_")) {
					menuId = Long.valueOf(navId.substring(9));
					MenuCompositionDetailPersistant mnuDP = (MenuCompositionDetailPersistant) genriqueDao.findById(MenuCompositionDetailPersistant.class, menuId);
					MenuCompositionPersistant mnuP = mnuDP.getOpc_menu();
					String tmpParentCode = null;
					String code = null, libelle = null;
					Long elmntId = null;
					int level = mnuP.getLevel();
					if(mnuDP.getOpc_famille() != null) {
						code = "F_"+mnuDP.getOpc_famille().getCode();
						libelle = mnuDP.getOpc_famille().getLibelle();
						elmntId = mnuDP.getOpc_famille().getId();
						level = level +1;
						tmpParentCode = "F_"+mnuDP.getOpc_famille().getCode();
					} else if(mnuDP.getOpc_list_choix() != null) {
						code = "LC_"+mnuDP.getOpc_list_choix().getCode();
						libelle = mnuDP.getOpc_list_choix().getLibelle();
						elmntId = mnuDP.getOpc_list_choix().getId();
						level = level +1;
						tmpParentCode = "LC_"+mnuDP.getOpc_list_choix().getCode();
					} else {
						tmpParentCode = "M_"+mnuP.getCode();
					}
					//
					parenLevel = level;
					addMouvementGroupCmd(httpUtil, elmntId, code, libelle, mnuDP.getPrix(), 1, TYPE_LIGNE_COMMANDE.GROUPE_MENU, parentCode, level, false);
					parentCode = tmpParentCode;
				} else if(navId.startsWith("MEN_CHOIX_")) {
					String choixIdDet = navId.substring(10);
					String[] idsSt = StringUtil.getArrayFromStringDelim(choixIdDet, "-");
					Long choixId = Long.valueOf(idsSt[0]);
					ListChoixDetailPersistant mnuP = (ListChoixDetailPersistant) genriqueDao.findById(ListChoixDetailPersistant.class, choixId);
					
					String code = null, libelle = null;
					int level = parenLevel;
					
					if( mnuP.getOpc_famille() != null) {
						code = "F_"+mnuP.getOpc_famille().getCode();
						libelle = mnuP.getOpc_famille().getLibelle();
						level = level +1;
					} else if(mnuP.getOpc_list_choix() != null) {
						code = "LC_"+mnuP.getOpc_list_choix().getCode();
						libelle = mnuP.getOpc_list_choix().getLibelle();
						level = level +1;
					}
					Long elmntId = mnuP.getId();
					parenLevel = level;
					addMouvementGroupCmd(httpUtil, elmntId, code, libelle, null, 1, TYPE_LIGNE_COMMANDE.GROUPE_MENU, parentCode, level, false);
					parentCode = mnuP.getOpc_list_choix() != null ? "LC_"+mnuP.getOpc_list_choix().getCode() :  "F_"+mnuP.getOpc_famille().getCode();
				} else if(navId.startsWith("MEN_FAM_")) {
					menuId = Long.valueOf(navId.substring(8));
					FamilleCuisinePersistant mnuP = (FamilleCuisinePersistant) genriqueDao.findById(FamilleCuisinePersistant.class, menuId);
					addMouvementGroupCmd(httpUtil, mnuP.getId(), "F_"+mnuP.getCode(), mnuP.getLibelle(), null, 1, TYPE_LIGNE_COMMANDE.GROUPE_MENU, parentCode, mnuP.getLevel(), false);
					parenLevel = mnuP.getLevel();
					parentCode = "F_"+mnuP.getCode();
				} else if(navId.startsWith("FAM_")) {
					menuId = Long.valueOf(navId.substring(4));
					FamilleCuisinePersistant mnuP = (FamilleCuisinePersistant) genriqueDao.findById(FamilleCuisinePersistant.class, menuId);
					addMouvementGroupCmd(httpUtil, mnuP.getId(), "F_"+mnuP.getCode(), mnuP.getLibelle(), null, 1, TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE, parentCode, mnuP.getLevel(), false);
					parenLevel = mnuP.getLevel();
					parentCode = "F_"+mnuP.getCode();
				} else {//MEN_
					menuId = Long.valueOf(navId.substring(4));
					MenuCompositionPersistant mnuP = (MenuCompositionPersistant) genriqueDao.findById(MenuCompositionPersistant.class, menuId);
					
					TYPE_LIGNE_COMMANDE type = mnuP.getLevel()<=2 ? TYPE_LIGNE_COMMANDE.MENU : TYPE_LIGNE_COMMANDE.GROUPE_MENU;
					addMouvementGroupCmd(httpUtil, mnuP.getId(), "M_"+mnuP.getCode(), mnuP.getLibelle(), mnuP.getMtt_prix(), 1, type, parentCode, mnuP.getLevel(), mnuP.getIs_menu());
					parenLevel = mnuP.getLevel();
					parentCode = "M_"+mnuP.getCode();
				}
			}
		}
		
		/**
		 * @param elementId
		 * @param code
		 * @param libelle
		 * @param prix
		 * @param qte
		 * @param typeLigne
		 * @param parentCode
		 */
		private void addMouvementGroupCmd(ActionUtil httpUtil, Long elementId, String code, String libelle, BigDecimal prix, Integer qte, TYPE_LIGNE_COMMANDE typeLigne, String parentCode, Integer level, Boolean isMenu) {
			Integer currentIdxClient = (Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT");
			if(currentIdxClient == null){
				httpUtil.setUserAttribute("CURRENT_IDX_CLIENT", 1);
				currentIdxClient = 1;
			}
			
			MenuCompositionPersistant CURRENT_MENU_COMPOSITION = (MenuCompositionPersistant) httpUtil.getUserAttribute("CURRENT_MENU_COMPOSITION");
			String CURRENT_MENU_NUM = (String) httpUtil.getUserAttribute("CURRENT_MENU_NUM");
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			List<CaisseMouvementArticlePersistant> listDetaimMvm = CURRENT_COMMANDE.getList_article();
			if(listDetaimMvm == null) {
				listDetaimMvm = new ArrayList<>();
			}
			CaisseMouvementArticlePersistant cmdP = null;
			boolean isGroupExist = false;

			for (CaisseMouvementArticlePersistant cmd : listDetaimMvm) {
				if (cmd == null || BooleanUtil.isTrue(cmd.getIs_annule())) {
					continue;
				}
				String currCode = cmd.getCode();
				String currType = cmd.getType_ligne();
				Long currElementId = cmd.getElementId();
				String currMenuIdx = cmd.getMenu_idx();

				if (currMenuIdx == null) {// Hors mebu --------------------------------------------
					if (currType.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString()) || currType.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {// N.B : On ajoute menu car la racine BURGER, PIZZA et de type menu mais sans index 
						if (currCode.equals(code) && currElementId.equals(elementId) && cmd.getIdx_client().equals(currentIdxClient)) {
							isGroupExist = true;
							break;
						}
					}
				} else if(currMenuIdx.equals(CURRENT_MENU_NUM)) {// Menu ---------------------------
					if (currType.equals(TYPE_LIGNE_COMMANDE.GROUPE_FAMILLE.toString()) 
							|| currType.equals(TYPE_LIGNE_COMMANDE.GROUPE_MENU.toString())
							|| currType.equals(TYPE_LIGNE_COMMANDE.MENU.toString())) {
						if (currCode.equals(code) && currElementId.equals(elementId) && cmd.getIdx_client().equals(currentIdxClient)) {
							isGroupExist = true;
							break;
						}
					}
				}
			}

			if (!isGroupExist) {
				cmdP = new CaisseMouvementArticlePersistant();

				cmdP.setCode(code);
				cmdP.setLibelle(libelle);
				cmdP.setType_ligne(typeLigne.toString());
				cmdP.setElementId(elementId);
				cmdP.setLevel(level);
				cmdP.setIs_menu(isMenu);
				//-------------------------------
				cmdP.setIdx_client((Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT"));
				if(cmdP.getRef_table() == null){
					cmdP.setRef_table((String) httpUtil.getUserAttribute("CURRENT_TABLE_REF"));
				}
				// Couverts table
				if(cmdP.getRef_table() != null){
					Map<String, Integer> mapCouvertsTable = (Map<String, Integer>)httpUtil.getMenuAttribute("COUVERTS_TABLE");
					if(mapCouvertsTable != null){
						cmdP.setNbr_couvert(mapCouvertsTable.get(cmdP.getRef_table()));
					}
				}
				
				if(level == null || level != 1) {
					cmdP.setParent_code(parentCode);
		
					cmdP.setOpc_mouvement_caisse(CURRENT_COMMANDE);
		
					cmdP.setOpc_menu(CURRENT_MENU_COMPOSITION);
					cmdP.setMenu_idx(CURRENT_MENU_NUM);
				}
				
				if (qte != null) {
					cmdP.setMtt_total(BigDecimalUtil.multiply(prix, BigDecimalUtil.get(qte)));
					cmdP.setQuantite(BigDecimalUtil.get(qte));
				}
				
				//
				listDetaimMvm.add(cmdP);
			}
		}
		
		
		/**
		 * Ajouter l'article à la commande
		 *
		 * @param articleId
		 */
		@SuppressWarnings({"unchecked" })
		private void manageAddArticleMenuNav(ActionUtil httpUtil, MenuCompositionPersistant menuCompositionParent, MenuCompositionDetailPersistant menuCompoDetail, ListChoixDetailPersistant choixDetailP, ArticlePersistant articleP, Integer qteEcrant) {
			if(menuCompositionParent == null) {
				return;
			}
			
			Integer currentIdxClient = (Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT");
			String CURRENT_MENU_NUM = (String) httpUtil.getUserAttribute("CURRENT_MENU_NUM");
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			List<String> listNav = (List<String>)httpUtil.getUserAttribute("HISTORIQUE_NAV");
			
			// Contrôle nombre éléments
			int maxNbrGroup = (menuCompositionParent.getNombre_max() == null) ? 0 : menuCompositionParent.getNombre_max();
			int maxNbrSousGroup = (menuCompoDetail == null || menuCompoDetail.getNombre() == null) ? 0 : menuCompoDetail.getNombre();
			int maxChoix = (choixDetailP == null || choixDetailP.getNombre() == null) ? 0 : choixDetailP.getNombre();

			int nbrArtInGroupe = 0;
			int nbrArtInSousGroupe = 0;

			if (maxNbrGroup > 0 || maxNbrSousGroup > 0 || maxChoix > 0) {
				List<CaisseMouvementArticlePersistant> listArtCmd = CURRENT_COMMANDE.getList_article();
				for (CaisseMouvementArticlePersistant caisseMvmArticleP : listArtCmd) {
					if (BooleanUtil.isTrue(caisseMvmArticleP.getIs_annule())) {
						continue;
					}
					if (caisseMvmArticleP.getIdx_client().equals(currentIdxClient) && caisseMvmArticleP.getMenu_idx() != null
							&& caisseMvmArticleP.getMenu_idx().equals(CURRENT_MENU_NUM)) {
						if (caisseMvmArticleP.getType_ligne().equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
							if (caisseMvmArticleP.getParent_code().endsWith("_"+menuCompositionParent.getCode())) {
								nbrArtInGroupe = nbrArtInGroupe + caisseMvmArticleP.getQuantite().intValue();
							}
							if (menuCompoDetail != null && caisseMvmArticleP.getComposition_detail_id() != null && caisseMvmArticleP
									.getComposition_detail_id().equals(menuCompoDetail.getId().toString())) {
								nbrArtInSousGroupe = nbrArtInSousGroupe + caisseMvmArticleP.getQuantite().intValue();
							}
						}
					}
				}
			}

			if ((maxNbrGroup > 0 && (nbrArtInGroupe >= maxNbrGroup || nbrArtInSousGroupe >= maxNbrGroup))
					|| (maxNbrSousGroup > 0 && nbrArtInSousGroupe >= maxNbrSousGroup)
					|| (maxChoix > 0 && nbrArtInSousGroupe >= maxChoix)) {
				int max = 0;
				if (maxNbrGroup > 0 && nbrArtInGroupe >= maxNbrGroup) {
					max = maxNbrGroup;
				} else if (maxNbrSousGroup > 0 && nbrArtInSousGroupe >= maxNbrSousGroup) {
					max = maxNbrSousGroup;
				} else if (maxChoix > 0 && nbrArtInSousGroupe >= maxChoix) {
					max = maxChoix;
				}
				MessageService.addGrowlMessage("Seuil dépassé",	"Vous avez dépasser le nombre autorisé (<b>" + max + "</b>) pour cet article.");
				return;
			}
			int qte = BigDecimalUtil.multiply(BigDecimalUtil.get(qteEcrant==null?1:qteEcrant), (menuCompoDetail==null?BigDecimalUtil.get(1):menuCompoDetail.getQuantite())).intValue();
			BigDecimal prix = null;

			if(menuCompoDetail == null){
				if(articleP == null){
					prix = menuCompositionParent.getMtt_prix();
				}
			} else if (articleP != null && (menuCompoDetail.getOpc_list_choix() == null && menuCompoDetail.getOpc_famille() == null)) {
				prix = menuCompoDetail.getPrix();
			}

			//
			addMouvementArticleMenuCmd(httpUtil, menuCompositionParent, menuCompoDetail, choixDetailP, articleP, prix, qte, (menuCompoDetail!=null?menuCompoDetail.getId().toString():null));

			if (maxNbrGroup > 0 && (nbrArtInGroupe == (maxNbrGroup - 1) || nbrArtInSousGroupe == (maxNbrGroup - 1))) {// Retour racine
//				httpUtil.setRequestAttribute("UP_MNU", "MEN_" + menuCompositionParent.getId());
				httpUtil.setRequestAttribute("NXT_STEP", true);
				httpUtil.setRequestAttribute("UP_MNU", listNav.get(listNav.size() - 1));
			} else if (maxNbrSousGroup > 0 && nbrArtInSousGroupe == (maxNbrSousGroup - 1)) {// Monter d'un niveau
				httpUtil.setRequestAttribute("UP_MNU", listNav.get(listNav.size() - 1));
			} else if (maxChoix > 0 && nbrArtInSousGroupe == (maxChoix - 1)) {// Monter d'un niveau
				httpUtil.setRequestAttribute("UP_MNU", listNav.get(listNav.size() - 1));
			}
			
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 */
		public void addArticleMenuCmd(ActionUtil httpUtil) {
			
			if(httpUtil.getParameter("disStck") != null) {
				MessageService.addGrowlMessage("", "Stock insuffisant pour cet article.");
				return;
			}
			
			// Selectionner la table de ce client
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			if(CURRENT_COMMANDE.getId() != null && STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString().equals(CURRENT_COMMANDE.getLast_statut())) {
				MessageService.addGrowlMessage("Mise à jour impossible", "Cette commande est marquée comme livrée. <br/>Elle ne peut plus être modifiée.");
				return;
			}
					
			List<String> listNav = ((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV"));
			if(listNav == null) {
				listNav = new ArrayList<>();
			}
			Long artId = httpUtil.getWorkIdLong();
			
			if(artId == null){
				httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
				return;
			}
			
			ArticlePersistant artP = (ArticlePersistant) familleService.findById(ArticlePersistant.class, artId);
			Long lastChoixDetId = null;
			Long lastMenuId = null;
			//
			for(String nav : listNav) {
				if(nav.startsWith("MEN_COMP_")) {
				
				} else if(nav.startsWith("MEN_CHOIX_")) {
					String choixIdDet = nav.substring(10);
					String[] idsSt = StringUtil.getArrayFromStringDelim(choixIdDet, "-");
					lastChoixDetId = Long.valueOf(idsSt[0]);
				} else {
					lastMenuId = Long.valueOf(nav.substring(4));
				}
			}
			
			ListChoixDetailPersistant listChoixDetP = lastChoixDetId==null?null:(ListChoixDetailPersistant) familleService.findById(ListChoixDetailPersistant.class, lastChoixDetId);
			MenuCompositionPersistant menuCompoP = lastMenuId==null?null:(MenuCompositionPersistant) familleService.findById(MenuCompositionPersistant.class, lastMenuId);
			
			String detId = httpUtil.getParameter("mnu");
			Long detailId = Long.valueOf(detId.substring(detId.indexOf(":")+1, detId.indexOf(";")));
			MenuCompositionDetailPersistant menuCompoDetP = (MenuCompositionDetailPersistant) familleService.findById(MenuCompositionDetailPersistant.class, detailId);
			
//			String calvVal = httpUtil.getParameter("qte_calc");
			int qte = 1;//StringUtil.isEmpty(calvVal) ? 1 : Integer.valueOf(calvVal);
			
			manageAddArticleMenuNav(httpUtil, menuCompoP, menuCompoDetP, listChoixDetP, artP, qte);
			
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 * @param artP
		 * @param qte
		 */
		@SuppressWarnings("unchecked")
		private void addMouvementArticleMenuCmd(ActionUtil httpUtil, MenuCompositionPersistant parentMenuComposition, MenuCompositionDetailPersistant menuCompoDetail, ListChoixDetailPersistant choixDetailP, ArticlePersistant artP, BigDecimal prix, Integer qte, String detailId) {
			Integer currentIdxClient = (Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT");
			List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
			String modeTravail =  ContextGloabalAppli.getGlobalConfig("MODE_TRAVAIL_CUISINE");
			modeTravail = (StringUtil.isEmpty(modeTravail) ? "EO" : modeTravail);
			
			boolean isPrintOnly = "PO".equals(modeTravail) || "PE".equals(modeTravail);// PrintOnly or PrintEcran
			
			String parentCode = null;
			if(choixDetailP != null) {
				if(choixDetailP.getOpc_list_choix() != null) {
					parentCode = "LC_"+choixDetailP.getOpc_list_choix().getCode();
				} else {
					parentCode = "F_"+choixDetailP.getOpc_famille().getCode();
				}
			} else {
				if(menuCompoDetail != null) {
					if(listNav.get(listNav.size()-1).startsWith("MEN_COMP_")) {
						if(menuCompoDetail.getOpc_famille() != null) {
							parentCode = "F_"+menuCompoDetail.getOpc_famille().getCode();
						} else if(menuCompoDetail.getOpc_list_choix() != null) {
							parentCode = "LC_"+menuCompoDetail.getOpc_list_choix().getCode();
						}
					} else {
						parentCode = "M_"+menuCompoDetail.getOpc_menu().getCode();
					}
				} else {
					parentCode = "M_"+parentMenuComposition.getCode();
				}
			}
			
			String CURRENT_MENU_NUM = (String) httpUtil.getUserAttribute("CURRENT_MENU_NUM");
			String CURRENT_TABLE_REF = (String) httpUtil.getUserAttribute("CURRENT_TABLE_REF");
			
			// Marquer la dernière ligne ajoutée
			String currId = currentIdxClient
								+ "-" + artP.getId() 
								+ "-" + TYPE_LIGNE_COMMANDE.ART_MENU.toString() 
								+ "-" + parentMenuComposition.getCode()
								+ "-" + CURRENT_MENU_NUM
								+ (StringUtil.isNotEmpty(CURRENT_TABLE_REF) ? "-"+CURRENT_TABLE_REF : "");
			httpUtil.setUserAttribute("CURRENT_ITEM_ADDED", currId);
			// Pour le traceur
			httpUtil.setUserAttribute("CURRENT_ART_TRACK", artP.getLibelle()+"|"+qte+"|"+BigDecimalUtil.formatNumber(prix));
			
			MenuCompositionPersistant CURRENT_MENU_COMPOSITION = (MenuCompositionPersistant) httpUtil.getUserAttribute("CURRENT_MENU_COMPOSITION");
			
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			List<CaisseMouvementArticlePersistant> listDetaimMvm = CURRENT_COMMANDE.getList_article();

			// Maj commande
			CaisseMouvementArticlePersistant cmdP = null;
			boolean isArticleExist = false;

			for (CaisseMouvementArticlePersistant cmd : listDetaimMvm) {
				if (BooleanUtil.isTrue(cmd.getIs_annule())) {
					continue;
				}

				String currType = cmd.getType_ligne();
				String currParentCode = cmd.getParent_code();
				String currMenuIdx = cmd.getMenu_idx();

				if (CURRENT_MENU_COMPOSITION != null) {
					if (currMenuIdx != null && currMenuIdx.equals(CURRENT_MENU_NUM)) {
						if (currType.equals(TYPE_LIGNE_COMMANDE.ART_MENU.toString())) {
							Long currElementId = cmd.getOpc_article().getId();
							if (currParentCode.equals(parentCode) && currElementId.equals(artP.getId()) && cmd.getIdx_client().equals(currentIdxClient)) {
								isArticleExist = true;
							}
						}
					}
				}
				if (isArticleExist) {
					cmdP = cmd;
					break;
				}
			}
			
			if (cmdP == null) {
				// Ajouter le groupe
				addMouvementGroupLevelsCmd(httpUtil);
				
				//
				cmdP = new CaisseMouvementArticlePersistant();
				cmdP.setType_opr(Integer.valueOf(1));

				cmdP.setCode(artP.getCode());
				cmdP.setLibelle(artP.getLibelle());
				cmdP.setType_ligne(TYPE_LIGNE_COMMANDE.ART_MENU.toString());
				cmdP.setParent_code(parentCode);

				cmdP.setOpc_article(artP);

				cmdP.setOpc_mouvement_caisse(CURRENT_COMMANDE);

				cmdP.setOpc_menu(CURRENT_MENU_COMPOSITION);
				cmdP.setMenu_idx(CURRENT_MENU_NUM);
				cmdP.setElementId(artP.getId());
				cmdP.setComposition_detail_id(detailId);
				//-------------------------------
				cmdP.setIdx_client((Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT"));
				if(cmdP.getRef_table() == null){
					cmdP.setRef_table((String) httpUtil.getUserAttribute("CURRENT_TABLE_REF"));
				}
				// Couverts table
				if(cmdP.getRef_table() != null){
					Map<String, Integer> mapCouvertsTable = (Map<String, Integer>)httpUtil.getMenuAttribute("COUVERTS_TABLE");
					if(mapCouvertsTable != null){
						cmdP.setNbr_couvert(mapCouvertsTable.get(cmdP.getRef_table()));
					}
				}
				listDetaimMvm.add(cmdP);
			}
			if (qte != null) {
				BigDecimal newQte = BigDecimalUtil.add(cmdP.getQuantite(), BigDecimalUtil.get(qte));

				cmdP.setMtt_total(BigDecimalUtil.multiply(prix, newQte));
				if(CURRENT_COMMANDE.getId() != null) {
					if(cmdP.getOld_qte_line() == null) {
						cmdP.setOld_qte_line(cmdP.getQuantite());
					}
				}
				cmdP.setQuantite(newQte);
				if(CURRENT_COMMANDE.getId() != null) {
					cmdP.setType_opr(Integer.valueOf(2));
				}
				
				
				// Maj statut pour les écrans cuisine
//				if(isQteChange){
					String last_statut = "";
					if(isPrintOnly) {
						last_statut = ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString();
						cmdP.setLast_statut(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());
					} else {
						boolean isAutoCmdPrep = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_AUTO"));// Si passage à prête directement
						last_statut = (isAutoCmdPrep ? ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString() : ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());
						cmdP.setLast_statut(last_statut);
					}
					
					boolean isMenu = BooleanUtil.isTrue(cmdP.getIs_menu());
					boolean isArticle = cmdP.getType_ligne().equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString()) 
									&& cmdP.getMenu_idx() == null 
									&& "C".equals(cmdP.getOpc_article().getDestination());
					if(isMenu && isArticle){
						cmdP.setLast_statut(last_statut);
					}
//				}
			}
			sortAndAddCommandeLigne(httpUtil, CURRENT_COMMANDE);

			// Maj total de la commande
			majTotalMontantCommande(httpUtil, CURRENT_COMMANDE);

			// Vérifier si on doit activer ou désactiver une offre ----------------------
			manageOffreTarif(httpUtil);
			manageModeLivraison(httpUtil, false);
		}
		
		/** ------------------------------------------------ Autres ----------------------------------------*/
		public void miseEnAttente(ActionUtil httpUtil) {
			if(!checkJournee()){ 
				return;
			}
			CaisseMouvementPersistant currCmd = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			
			// temporaire ou non payée (cas restau ou caisse autonome)
			if(currCmd.getId() != null){
				CaisseMouvementPersistant caiseMvmDb = menuService.findById(CaisseMouvementPersistant.class, currCmd.getId());
			
				if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(caiseMvmDb.getLast_statut())
							|| caiseMvmDb.getMode_paiement() != null
					){
						initNewCommande(httpUtil);
						MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Commande déjà traitée", "Cette commande est déjà traitée.");
						httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
						return;
				}
			}
			
			if(StringUtil.isEmpty(httpUtil.getParameter("cuis")) && StringUtil.isEmpty(currCmd.getType_commande())){
				httpUtil.setDynamicUrl("/domaine/caisse_restau/"+httpUtil.getUserAttribute("PATH_JSP_CM")+"/mise_en_attente_popup.jsp");
				return;
			}
			
			if(currCmd.getList_article().size() == 0) {
				MessageService.addGrowlMessage("Commande vide", "<h4>La commande en cours ne contient aucun article.</h4>");
				return;
			} else if(BigDecimalUtil.isZero(currCmd.getMtt_commande_net())) {
				boolean isArtExist = false;
				for(CaisseMouvementArticlePersistant det : currCmd.getList_article()) {
					if(BooleanUtil.isTrue(det.getIs_annule())) {
						continue;
					}
					if(TYPE_LIGNE_COMMANDE.ART.toString().equals(det.getType_ligne()) 
							|| TYPE_LIGNE_COMMANDE.ART_MENU.toString().equals(det.getType_ligne()) 
							|| TYPE_LIGNE_COMMANDE.MENU.toString().equals(det.getType_ligne())) {
						isArtExist = true;
						break;
					}
				}
				if(!isArtExist) {
					MessageService.addGrowlMessage("Commande vide", "<h4>La commande en cours ne contient aucun article.</h4>");
					return;
				}
			}
			
			// Interdir d'attribuer la  meme table à plusieurs commandes------------------------------
			if(!checkTable(httpUtil)) {
				MessageService.addGrowlMessage("", "<h4>Cette table est déjà utilisée pour la commande ** <b>"+currCmd.getRef_commande()+"</b> **</h4>");
				return;
			}

			boolean isCuisine = StringUtil.isTrue(httpUtil.getParameter("cuis"))// S'elle doit être envoyé vers la cuisine
					&& !BooleanUtil.isTrue(ContextAppliCaisse.getCaisseBean().getIs_notprint_cuis());
			String typeCmd = httpUtil.getParameter("type_cmd");
			boolean isTableRequired =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("SELECT_TABLE_REQUIRED"));
			
			if(isTableRequired 
					&& isCuisine
					&& ContextAppli.TYPE_COMMANDE.P.toString().equals(typeCmd)
					&& StringUtil.isEmpty(currCmd.getRefTablesDetail())){
				MessageService.addGrowlMessage("", "<h2>Veuillez sélectionner une table.</h2>");
				return;				
			}

			boolean isCtrlStockCaisse = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_CAISSE"));
			boolean isCtrlStock =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CTRL_STOCK_MVM_CAISSE"));
		    if(isCtrlStockCaisse || (isCtrlStock && BooleanUtil.isTrue(ContextGloabalAppli.getAbonementBean().isOptStock()))){
		    	if(!caisseWebService.isEtatStockArticlesValide(currCmd)){
		    		return;
		    	}
		    }
		    
			String modeTravail =  ContextGloabalAppli.getGlobalConfig("MODE_TRAVAIL_CUISINE");
			modeTravail = (StringUtil.isEmpty(modeTravail) ? "PO" : modeTravail);
			boolean isParaCuisNull = httpUtil.getParameter("cuis") == null;
			boolean isPrintOnly = "PO".equals(modeTravail) || "PE".equals(modeTravail);// PrintOnly or PrintEcran
			STATUT_CAISSE_MOUVEMENT_ENUM statut = null;
			//
			if(StringUtil.isEmpty(currCmd.getType_commande())){// Premiére mise en attente
				if(isCuisine){
					currCmd.setType_commande(typeCmd);
					String ref_commande = caisseWebService.getNextRefCommande();
					currCmd.setRef_commande(ref_commande);
					
					// Impression ticket de caisse en même temps
					boolean isPrintTicketCaisseCuis =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("TICKET_CAISSE_CUIS"));
					if(isPrintTicketCaisseCuis) {
						PrintTicketUtil pu = new PrintTicketUtil(currCmd, null);
			        	boolean isAsync = printData(httpUtil, pu.getPrintPosBean(), false);
					}
				} else{
					currCmd.setType_commande(null);
				}
				
				//
				statut = isCuisine ? STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE : STATUT_CAISSE_MOUVEMENT_ENUM.TEMP; 
			} else{
				if(isPrintOnly) {
					statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
				} else {
					if(StringUtil.isNotEmpty(currCmd.getLast_statut())){
						statut = STATUT_CAISSE_MOUVEMENT_ENUM.valueOf(currCmd.getLast_statut());
					} else{
						statut = isCuisine ? STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE : STATUT_CAISSE_MOUVEMENT_ENUM.TEMP;					
					}
				}
				if(currCmd.getId() != null) {
					CaisseMouvementPersistant cmdBd = (CaisseMouvementPersistant) familleService.findById(CaisseMouvementPersistant.class, currCmd.getId());
					
					for (CaisseMouvementArticlePersistant caisseMvmArt : currCmd.getList_article()) {
						if(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.ANNUL.toString().equals(caisseMvmArt.getLast_statut())) {
							continue;
						}
						if(caisseMvmArt.getType_opr() != null && caisseMvmArt.getType_opr() == Integer.valueOf(2)) {
							for (CaisseMouvementArticlePersistant caisseMvmBd : cmdBd.getList_article()) {
								if(caisseMvmBd.getId().equals(caisseMvmArt.getId())) {
									caisseMvmArt.setOld_qte_line(caisseMvmBd.getQuantite());
									caisseMvmArt.setLast_statut(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());// Remettre un statut initial pour impression
									break; 
								}
							}
						}
					}
				}
			}
			
			boolean isAutoCmdPrep = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_AUTO"));// Si passage à prêparation directement
			
			if((isCuisine||isParaCuisNull) && (isPrintOnly || isAutoCmdPrep && statut.equals(STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE))){
				statut = STATUT_CAISSE_MOUVEMENT_ENUM.PREP;
			}
			
			// Mise à jour des statut détail afin de gérer les reprises et modification de commandes
			if(!STATUT_CAISSE_MOUVEMENT_ENUM.TEMP.equals(statut) && (!isPrintOnly || currCmd.getId() == null)){
				for (CaisseMouvementArticlePersistant caisseMvmArtP :  currCmd.getList_article()) {
					boolean isMenu = BooleanUtil.isTrue(caisseMvmArtP.getIs_menu());
					boolean isArticle = caisseMvmArtP.getType_ligne().equals(ContextAppli.TYPE_LIGNE_COMMANDE.ART.toString()) 
									&& caisseMvmArtP.getMenu_idx() == null 
									&& "C".equals(caisseMvmArtP.getOpc_article().getDestination());
					if(!isMenu && !isArticle){
						continue;
					}
					if(isPrintOnly || StringUtil.isEmpty(caisseMvmArtP.getLast_statut())){
						caisseMvmArtP.setLast_statut(((isAutoCmdPrep || isPrintOnly) ? ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString() : ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString()));
					}
				}
			}
			// ************************************************************************
			
			if(ContextAppli.getUserBean().isInProfile("SERVEUR")
					&& currCmd.getOpc_serveur() == null){
				currCmd.setOpc_serveur(ContextAppli.getUserBean());
			}
			if(currCmd.getOpc_user() == null) {
				currCmd.setOpc_user(ContextAppli.getUserBean());
			}
			boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
			final STATUT_CAISSE_MOUVEMENT_ENUM statutMvm = statut;

			if(currCmd.getOpc_caisse_journee() == null){
				currCmd.setOpc_caisse_journee(ContextAppliCaisse.getJourneeCaisseBean());
			}
			if(currCmd.getOpc_caisse_journee().getOpc_journee() == null){
	    		JourneePersistant journeePersistant = journeeService.getLastJournee();
	    		currCmd.getOpc_caisse_journee().setOpc_journee(journeePersistant);
	    	}
			
	  		EtablissementPersistant ets = ContextAppli.getEtablissementBean();  
	  		boolean isCoudMaster = "cloudMaster".equals(StrimUtil.getGlobalConfigPropertie("context.install"));
	  		Long userId = ContextAppli.getUserBean().getId();
	  		
			ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor)ServiceUtil.getBusinessBean("taskExecutor");
			taskExecutor.submit(new Callable() {
				public Object call() throws Exception {
					
					 if(isCoudMaster) {
						 MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", ets);
					 }
					 
					// Annulation stock
					if(currCmd.getId() != null){
						try{caisseWebService.clearMvmCaisseStock(currCmd);} catch(Exception e){LOGGER.error(e);};
					}
					try{
						// Création mouvement caisse
						CaisseMouvementPersistant currCmd2 = caisseWebService.createMouvementCaisse(currCmd, statutMvm, isPoints);
						
						// Libérer les commandes
						userService.unlockCommandes(currCmd.getId(), userId);
						if(currCmd.getOpc_user_lock() != null && currCmd.getOpc_user_lock().getId() == userId) {
							currCmd.setOpc_user_lock(null);
							currCmd2.setOpc_user_lock(null);
						}
						
						if(currCmd.getOpc_client() != null){
							portefeuilleService2.majSoldePortefeuilleMvm(currCmd.getOpc_client().getId(), "CLI");
			            }
						if((isCuisine||isParaCuisNull) && !StringUtil.isEmpty(currCmd2.getType_commande())){
							// Destockage
							new Thread(() -> {
								if(isCoudMaster) {
									 MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", ets);
								 }
								
			        			try {
			        				// Imprimante
			        				caisseService.gestionEcranImprimante(currCmd2);
			        			} catch (Exception ex) {
			        				throw new RuntimeException(ex);
			        			}
			        	    }).start();
						}
					} catch(Exception e){
						e.printStackTrace();
		            	LOGGER.error(e);
					}
					return null;
		        }
		    });
		    
			//
			initNewCommande(httpUtil);
			
			httpUtil.addJavaScript("$('#close_modal').trigger('click');");
			
			if(isCuisine){
				if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("AUTH_REQUIRED_CUIS_OUT"))){
					httpUtil.setDynamicUrl("commun.login.disconnect");
					return;
				} else if(StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("AUTH_REQUIRED_CUIS"))){
					MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Commande ajoutée", "<h3>La commande est envoyée en cuisine.</h3>");
					httpUtil.setUserAttribute("LOCK_MODE", true);
				}
			} else{
				MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Commande ajoutée", "<h3>La commande est mise en attente.</h3>");			
			}
			
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 */
		public void manageCouverts(ActionUtil httpUtil){
			String nbrCouverts = httpUtil.getParameter("nbr_couvert");
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			if(CURRENT_COMMANDE.getId() != null && CURRENT_COMMANDE.getLast_statut().equals(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString())) {
				MessageService.addGrowlMessage("Mise à jour impossible", "Cette commande est marquée comme livrée. <br/>Elle ne peut plus être modifiée.");
				return;
			}
			
			CaisseMouvementArticlePersistant cmdTable = null;
			for(CaisseMouvementArticlePersistant det : CURRENT_COMMANDE.getList_article()){
	            if(det.getRef_table().equals(httpUtil.getParameter("ref_tab"))){
	            	cmdTable = det;
	            	break;
				}
			}
			
			if(nbrCouverts == null){
				 String params = "istab"+httpUtil.getParameter("istab")
			 	 	+ "&ref_tab="+httpUtil.getParameter("ref_tab")
			 	 	+"&tp="+httpUtil.getParameter("tp");
				 
				 httpUtil.setRequestAttribute("params", params);
				 
				 String[][] data = new String[10][2];
				 for(int i=0; i<10; i++){
					 data[i][0] = ""+(i+1);
					 data[i][1] = ""+(i+1);
				 }
				 httpUtil.setRequestAttribute("dataCouvert", data); 
				 
				 // Ancienne valeurs
			     if(cmdTable != null){
			         httpUtil.setRequestAttribute("nbr_couvert", cmdTable.getNbr_couvert());
			     }
			     httpUtil.setDynamicUrl(httpUtil.getUserAttribute("PATH_JSP_CAISSE")+"/add-couvert.jsp");
			} else{
				 if(cmdTable != null){
					 cmdTable.setNbr_couvert(Integer.valueOf(nbrCouverts));
				 }
				 Map<String, Integer> mapCouvertsTable = (Map<String, Integer>)httpUtil.getMenuAttribute("COUVERTS_TABLE");
				 if(mapCouvertsTable == null){
					 mapCouvertsTable = new HashMap<String, Integer>();
					 httpUtil.setMenuAttribute("COUVERTS_TABLE", mapCouvertsTable);
				 }
				 mapCouvertsTable.put(httpUtil.getParameter("ref_tab"), Integer.valueOf(nbrCouverts));
			    httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
			}
		}
		
		public void init_home(ActionUtil httpUtil) {
			if(httpUtil.getUserAttribute("HISTORIQUE_NAV") != null){
				((List<String>)httpUtil.getUserAttribute("HISTORIQUE_NAV")).clear();
			}
			// Familles
			PagerBean pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "FAM");
			List<FamillePersistant> listFamille = caisseWebService.getListFamilleCaissePagination(ContextAppliCaisse.getCaisseBean().getId(), pagerBean);
			httpUtil.setRequestAttribute("listFamille", listFamille);
			
			// Fav
			init_fav(httpUtil);
			
			// Menus
			pagerBean = ControllerUtil.managePager(httpUtil.getRequest(), "MNU");
			List<MenuCompositionPersistant> listMenu = menuCompositionService.getListeMenuCaissePagination(ContextAppliCaisse.getCaisseBean().getId(), pagerBean);
			httpUtil.setRequestAttribute("listMenu", listMenu);
			
			httpUtil.setRequestAttribute("isLoadEvent", true);// Pour cacher bouton up dans mobile
			
			httpUtil.setDynamicUrl(getRight_bloc_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 */
		public void loadNextStep(ActionUtil httpUtil){
			Integer step = 0;
			if(httpUtil.getParameter("step") == null){
				step = Integer.valueOf(httpUtil.getParameter("stepr")) - 1;
			} else{
				step = Integer.valueOf(httpUtil.getParameter("step")) + 1;
			}
			httpUtil.setUserAttribute("STEP_MNU", step);
			httpUtil.setRequestAttribute("fromStep", true);
			//
			menuCompoEvent(httpUtil);
		}
		
		public void suiteCommande(ActionUtil httpUtil) {
			CaisseMouvementArticlePersistant currentLigne = getSelectedCommandeLigne(httpUtil);
			
			if(currentLigne != null) {
				currentLigne.setIs_suite_lock(true);
			}
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		public void suiteCommandeAnnule(ActionUtil httpUtil) {
			CaisseMouvementArticlePersistant currentLigne = getSelectedCommandeLigne(httpUtil);
			if(currentLigne != null) {
				currentLigne.setIs_suite_lock(false);
				currentLigne.setIs_suite_end(true);
			}
			
			CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			CaissePersistant caisse = null;
			
			if(StringUtil.isNotEmpty(CURRENT_COMMANDE.getCaisse_cuisine())) {
				String[] data = StringUtil.getArrayFromStringDelim(CURRENT_COMMANDE.getCaisse_cuisine(), ";");
				if(data != null) {
					for(String val : data) {
						if(val != null && val.indexOf(":"+currentLigne.getId()) != -1) {
							String[] det = StringUtil.getArrayFromStringDelim(val, ":");
							if(det != null) {
								caisse = caisseService.findById(CaissePersistant.class, Long.valueOf(det[0]));
								break;
							}
						}
					}
				}
			}
			if(caisse != null) {
				final CaissePersistant fCaisse = caisse;
				new Thread(() -> {
					new PrintCuisineSuiteCmdUtil(fCaisse, CURRENT_COMMANDE, currentLigne).print();
				}).start();
			}
			
			httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 */
		public void upButtonEvent(ActionUtil httpUtil) {
			
			String upMnu = httpUtil.getParameter("up");
			Map listData = new LinkedHashMap();
			List<String> listNav = (List<String>)httpUtil.getUserAttribute("HISTORIQUE_NAV");
			//
			
			if(listNav == null){
				httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
				return;
			}
			
			boolean isToHome = false;
			boolean isMobileFrom = ContextAppli.APPLI_ENV.cais_mob.toString().equals(httpUtil.getUserAttribute("CURRENT_ENV"))
							|| "mobile-client".equals(httpUtil.getUserAttribute("PATH_JSP_CM"))
							|| "mobile-livreur".equals(httpUtil.getUserAttribute("PATH_JSP_CM"));
			
			if(isMobileFrom && listNav.size() == 1){
				init_home(httpUtil);
				isToHome = true;
			}
			
			if(listNav != null && StringUtil.isEmpty(upMnu) && listNav.size() > 0) {
				upMnu = listNav.get(listNav.size() - 1);
			}
			
//			if(upMnu.length()>=4 && NumericUtil.isLong(upMnu.substring(4))) {
//				Long elementId = Long.valueOf(upMnu.substring(4));
//				FamillePersistant parentFamille = familleService.getFamilleParent(elementId);
//				if (parentFamille == null || parentFamille.getLevel() == 0) {
//					init_home(httpUtil);
//					isToHome = true;
//				}
//			}
			
			// 
			if(StringUtil.isNotEmpty(upMnu)){
				manageUpButton(httpUtil, upMnu, listData);
				
				int idxMnu = listNav.indexOf(upMnu);
				int size = listNav.size();
				
				if(size > 0 && idxMnu > 0){
					String[] toRemove = new String[size];
					int idx = 0;
					for(int i=idxMnu; i<size; i++) {
						toRemove[idx] = listNav.get(i);
						idx++;
					}
					for(String st : toRemove) {
						listNav.remove(st);
					}
				}
			}
			// Pour le mobile		
			if(listNav.size() == 1 || isToHome) {
				httpUtil.setRequestAttribute("isLoadEvent", true);
			}
			
			if(!isToHome){
				httpUtil.setRequestAttribute("listMenu", listData);
				httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
			}
		}
		
		/**
		 * @param httpUtil
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void loadMenu(ActionUtil httpUtil) {
			
			httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
			
			Long menuId = httpUtil.getWorkIdLong();
			if (menuId == null) {
				return;
			}
			MenuCompositionPersistant menuPersistant = (MenuCompositionPersistant) familleService.getGenriqueDao().findById(MenuCompositionPersistant.class, menuId);
			if (menuPersistant == null) {
				return;
			}
			
			// Purge infos steps
			httpUtil.removeUserAttribute("STEP_MNU");
			httpUtil.removeUserAttribute("LIST_SOUS_MENU");

			// Données de la session
			manageDataSession(httpUtil);
			//
			httpUtil.removeUserAttribute("CURRENT_MENU_COMPOSITION");
			httpUtil.removeUserAttribute("CURRENT_MENU_NUM");

			((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("MEN_" + menuId);

			Map listData = new LinkedHashMap();
			populateSousMenuCompositionPanel(httpUtil, menuPersistant, false, listData);

			httpUtil.setRequestAttribute("listMenu", listData);

			// Pour le mobile
			httpUtil.setRequestAttribute("isLoadEvent", true);
			
			httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
		}

		/**
		 * @param httpUtil
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void menuCompoDetailEvent(ActionUtil httpUtil) {
			
			Long detailId = httpUtil.getWorkIdLong();
			((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("MEN_COMP_" + detailId);
			
			IGenericJpaDao genriqueDao = familleService.getGenriqueDao();
			MenuCompositionDetailPersistant compositionPersistant = (MenuCompositionDetailPersistant) genriqueDao.findById(MenuCompositionDetailPersistant.class, detailId);

			Map listData = new LinkedHashMap<>();

			if (compositionPersistant.getOpc_list_choix() != null) {
				populateMenuListChoix(httpUtil, null, compositionPersistant, compositionPersistant.getOpc_list_choix(), listData);
			} else if (compositionPersistant.getOpc_famille() != null) {
				populateSousFamilleMenuPanel(httpUtil, null, compositionPersistant, compositionPersistant.getOpc_famille(), listData);
			}
			httpUtil.setRequestAttribute("listMenu", listData);

			httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
		}
		
		/**
		 * @param httpUtil
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void menuCompoChoixEvent(ActionUtil httpUtil) {
			
			Long detailId = httpUtil.getWorkIdLong();
			List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
			
			IGenericJpaDao genriqueDao = familleService.getGenriqueDao();
			
			String lastId = listNav.get(listNav.size() - 1);
			String lastIdSt = lastId.substring(lastId.lastIndexOf("_")+1);
			
			Long currMenuCompId = null;
			if(lastIdSt.indexOf("-") != -1) {
				String[] idsSt = StringUtil.getArrayFromStringDelim(lastIdSt, "-");
				currMenuCompId = Long.valueOf(idsSt[1]);
			} else {
				currMenuCompId = Long.valueOf(lastIdSt);
			}
			MenuCompositionPersistant menuParent = null;
			MenuCompositionDetailPersistant menuDetail = (MenuCompositionDetailPersistant) this.articleService.findById(MenuCompositionDetailPersistant.class, currMenuCompId);
			if(menuDetail == null){
				menuParent = (MenuCompositionPersistant) this.articleService.findById(MenuCompositionPersistant.class, currMenuCompId);
			}
			
			ListChoixDetailPersistant listChoixPersistant = (ListChoixDetailPersistant) genriqueDao.findById(ListChoixDetailPersistant.class, detailId);
			
			Map listData = new LinkedHashMap<>();

			if (listChoixPersistant.getOpc_famille() != null) {
				((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("MEN_CHOIX_"+detailId+"-"+(menuDetail!=null?menuDetail.getId():menuParent.getId()));
				populateSousFamilleMenuPanel(httpUtil, menuParent, menuDetail, listChoixPersistant.getOpc_famille(), listData);
			} else if (listChoixPersistant.getOpc_list_choix() != null) {
				((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("MEN_CHOIX_"+detailId+"-"+(menuDetail!=null?menuDetail.getId():menuParent.getId()));
				populateMenuListChoix(httpUtil, menuParent, menuDetail, listChoixPersistant.getOpc_list_choix(), listData);
			}

			httpUtil.setRequestAttribute("listMenu", listData);

			httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
		}
		
		 /**
		    * @param httpUtil
		    */
		   public void selectLigneMenu(ActionUtil httpUtil) {
			   CaisseMouvementPersistant currMvm = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
			   
			   if(currMvm == null){
				   MessageService.addGrowlMessage("Aucune commande", "Aucune commande trouvée.");
				   return;
			   }
			   
//		   	   String code = httpUtil.getParameter("cd");
//			   String typeLigne = httpUtil.getParameter("tp");
		       Long elementId = httpUtil.getLongParameter("elm");
		       String parentCode = httpUtil.getParameter("par");
		       String menuIdx = httpUtil.getParameter("mnu");
		       
		       httpUtil.setUserAttribute("CURRENT_MENU_NUM", menuIdx);
		    
		       // 
		       manageDataSession(httpUtil);
		       
		       if(currMvm.getId() != null && currMvm.getLast_statut().equals(STATUT_CAISSE_MOUVEMENT_ENUM.LIVRE.toString())) {
		    	    MessageService.addGrowlMessage("Mise à jour impossible", "Cette commande est marquée comme livrée. <br/>Elle ne peut plus être modifiée.");
					return;
				}
		       
		       MenuCompositionPersistant parentMenuComp = (MenuCompositionPersistant) familleService.findById(MenuCompositionPersistant.class, elementId);
		       List<CaisseMouvementArticlePersistant> listArticle = currMvm.getList_article();
		       
		       MenuCompositionPersistant parentRootMenu = null;
		       for (CaisseMouvementArticlePersistant caisseMvtArtP : listArticle) {
		      	 if(caisseMvtArtP.getCode().equals(parentCode)){
		      		parentRootMenu = (MenuCompositionPersistant) familleService.findById(MenuCompositionPersistant.class, caisseMvtArtP.getElementId());
		      		break;
		      	 }
		       }
		       
		       List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
		       if(listNav == null){
		    	   manageDataSession(httpUtil);
		       }
		       listNav.add("MEN_" + parentRootMenu.getId());
		       listNav.add("MEN_" + parentMenuComp.getId());
		       //
		       httpUtil.setUserAttribute("CURRENT_MENU_COMPOSITION", parentMenuComp);
		       
		       // Reafficher le wizard --------------------------------------------------------
		       List<MenuCompositionPersistant> listSousMenu = menuCompositionService.getListeMenuEnfants(elementId, ContextAppliCaisse.getCaisseBean().getId(), true);
				httpUtil.setUserAttribute("STEP_MNU", 0);
				httpUtil.setUserAttribute("LIST_SOUS_MENU", listSousMenu);
		       
				// Ajouter une étape
				MenuCompositionPersistant menuPersistant = listSousMenu.get(0);
				((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("MEN_" + menuPersistant.getId());
				// -------------------------------------------------------------------------
				
		       //
		       Map listData = new LinkedHashMap();
		       populateSousMenuCompositionPanel(httpUtil, menuPersistant, false, listData);
		       httpUtil.setRequestAttribute("listMenu", listData);
		       
		       httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
		   }
		   
		   /**
			 * @param httpUtil
			 * @param menuComposition
			 * @param isUpAction
			 */
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void populateSousMenuCompositionPanel(ActionUtil httpUtil, MenuCompositionPersistant menuComposition, boolean isUpAction, Map listData) {
				if(menuComposition == null){
					return;
				}
				
				CaissePersistant caisseBean = ContextAppliCaisse.getCaisseBean();
				List<MenuCompositionPersistant> listSousMenu = menuCompositionService.getListeMenuEnfants(menuComposition.getId(), (caisseBean!=null?caisseBean.getId():null), true);
				List<MenuCompositionDetailPersistant> listCompositionDetail = menuComposition.getList_composition();
				
				boolean isOneChoice = (listCompositionDetail != null && listCompositionDetail.size() == 1
						&& (listSousMenu == null || listSousMenu.isEmpty()));// Si un seul élément on rentre directement
				boolean isOneMenu = ((listCompositionDetail == null || listCompositionDetail.isEmpty())
						&& (listSousMenu != null && listSousMenu.size() == 1 && !BooleanUtil.isTrue(listSousMenu.get(0).getIs_menu())));// Si un seul élément on rentre directement

				// Cas menu avec un seul niveau ----------------------------------------
				if (listSousMenu == null) {
					if(menuComposition.getLevel() == 1) {
						httpUtil.setUserAttribute("CURRENT_MENU_COMPOSITION", menuComposition);
						httpUtil.setUserAttribute("CURRENT_MENU_NUM", DateUtil.dateToString(new Date(), "MMss"));
					} // ---------------------------------------------------------------------
				// Sous menu
				} else {
					for (MenuCompositionPersistant menuPersistant : listSousMenu) {
						// Un seul choix dans le menu , on rentre directement
						if (isOneMenu) {
							((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("MEN_" + menuPersistant.getId());//??? Avoir s' il faut ajoter ce niveau
							// Si un prix est indiqué alors ajout dans la commande
							if (menuPersistant.getMtt_prix() != null && !isUpAction) {
								if (BooleanUtil.isTrue(menuPersistant.getIs_menu())) {
									httpUtil.setUserAttribute("CURRENT_MENU_COMPOSITION", menuPersistant);
									httpUtil.setUserAttribute("CURRENT_MENU_NUM", DateUtil.dateToString(new Date(), "MMss"));
								}
							}
							//
							populateSousMenuCompositionPanel(httpUtil, menuPersistant, isUpAction, listData);
						} else {
							listData.put(""+System.nanoTime(), menuPersistant);
						}
					}
				}

				// Ajout des familles ---------------------------------------------------------
				if (listCompositionDetail != null) {
					for (MenuCompositionDetailPersistant compositionDetPersistant : listCompositionDetail) {
						FamilleCuisinePersistant famillePersistant = compositionDetPersistant.getOpc_famille();
						if (famillePersistant == null) {
							continue;
						}
						//
						if (isOneChoice) {// Si un seul choix on détail directement tout en ajoutant le groupe dans le tableau
							populateSousFamilleMenuPanel(httpUtil, null, compositionDetPersistant, compositionDetPersistant.getOpc_famille(), listData);
						} else {
							listData.put("DET:"+compositionDetPersistant.getId()+";"+System.nanoTime(), compositionDetPersistant);
						}
					}
				}

				// Ajout des listes de choix
				// ---------------------------------------------------------
				if (listCompositionDetail != null) {
					for (MenuCompositionDetailPersistant compositionDetPersistant : listCompositionDetail) {
						ListChoixPersistant choixPersistant = compositionDetPersistant.getOpc_list_choix();
						if (choixPersistant == null) {
							continue;
						}
						if (isOneChoice) {// Si un seul choix on détail directement
							populateMenuListChoix(httpUtil, null, compositionDetPersistant, choixPersistant, listData);
						} else {
							listData.put("DET:"+compositionDetPersistant.getId()+";"+System.nanoTime(), compositionDetPersistant);
						}
					}

					// Ajout des articles
					// ----------------------------------------------------------------
					for (MenuCompositionDetailPersistant compositionDetPersistant : listCompositionDetail) {
						ArticlePersistant articlePersistant = compositionDetPersistant.getOpc_article();
						if (articlePersistant == null) {
							continue;
						}
						listData.put("DET:"+compositionDetPersistant.getId()+";"+System.nanoTime(), compositionDetPersistant);
					}
				}
			}

			/**
			 * @param choixP
			 */
			@SuppressWarnings({ "rawtypes", "unchecked" })
			private void populateMenuListChoix(ActionUtil httpUtil, MenuCompositionPersistant menuParent, MenuCompositionDetailPersistant menuCompoDetail, ListChoixPersistant listChoixPersistant, Map listData) {
				List<ListChoixDetailPersistant> listDetail = listChoixPersistant.getList_choix_detail();
				boolean isOneMenu = listDetail.size() == 1;

				if(menuParent == null){
					menuParent = new MenuCompositionPersistant();
					menuParent.setId(Long.valueOf(-1));
				}
				
				if(listData == null){
					listData = new LinkedHashMap();
				}
				
				// Les articles
				for (ListChoixDetailPersistant choixDetailP : listDetail) {
					if (choixDetailP.getOpc_article() == null) {
						continue;
					}
					listData.put("DET:"+(menuCompoDetail!=null?menuCompoDetail.getId():menuParent.getId())+";"+System.nanoTime(), choixDetailP);
				}

				// Les familles
				for (ListChoixDetailPersistant choixArticleP : listDetail) {
					FamilleCuisinePersistant famillePersistant = choixArticleP.getOpc_famille();
					if (famillePersistant == null) {
						continue;
					}

					// Si un seul choix rentrer directement
					if (isOneMenu) {
						populateSousFamilleMenuPanel(httpUtil, menuParent, menuCompoDetail, famillePersistant, listData);
					} else {
						listData.put("DET:"+(menuCompoDetail!=null?menuCompoDetail.getId():menuParent.getId())+";"+System.nanoTime(), choixArticleP);
					}
				}

				// Les listes de composition
				for (ListChoixDetailPersistant choixArticleP : listDetail) {
					ListChoixPersistant listChoixFilleP = choixArticleP.getOpc_list_choix();
					if (listChoixFilleP == null || choixArticleP.getOpc_famille() != null) {
						continue;
					}

					// Si un seul choix rentrer directement
					if (isOneMenu) {
						populateMenuListChoix(httpUtil, menuParent, menuCompoDetail, listChoixFilleP, listData);
					} else {
						listData.put("DET:"+(menuCompoDetail!=null?menuCompoDetail.getId():menuParent.getId())+";"+System.nanoTime(), choixArticleP);
					}
				}
			}
			
			/**
			 * @param listSousFamille
			 */
			@SuppressWarnings({ "rawtypes", "unchecked" })
			private void populateSousFamilleMenuPanel(ActionUtil httpUtil, MenuCompositionPersistant menuParent, MenuCompositionDetailPersistant menuCompoDetail,/*, ListChoixDetailPersistant choixArticleP,*/  FamilleCuisinePersistant parentFamille, Map listData) {
				List<FamillePersistant> listSousFamille = familleService.getFamilleEnfants("CU", parentFamille.getId(), true);
				String code = "DET:"+(menuCompoDetail!=null?menuCompoDetail.getId():menuParent.getId());
				
				// Sous famille
				if (listSousFamille != null) {
					for (FamillePersistant famillePersistant : listSousFamille) {
						listData.put(code+";"+System.nanoTime(), famillePersistant);
					}
				}

				// Ajouter les articles
				List<ArticlePersistant> listArticle = articleService.getListArticleCuisineActif(parentFamille.getId());
				for (ArticlePersistant articlePersistant : listArticle) {
					listData.put(code+";"+System.nanoTime(), articlePersistant);
				}
			}
			
			/**
			 * @param name
			 */
			@SuppressWarnings({ "rawtypes" })
			private void manageUpButton(ActionUtil httpUtil, String name, Map listData) {
				if (name.startsWith("MEN")) {
					if (name.startsWith("MEN_FAM")) {// Sous famille menu
														// --------------------------------------------------------
						Long familleId = Long.valueOf(name.substring(8));// Retirer le prefix MEN_FAM_
						String[] idsSt = StringUtil.getArrayFromStringDelim(name.substring(name.indexOf("-") + 1), "-");
						Long parentMenuId = Long.valueOf(idsSt[0]);
						Long menuDetailId = Long.valueOf(idsSt[1]);

						MenuCompositionPersistant parentMenuComp = menuCompositionService.findById(parentMenuId);
						MenuCompositionDetailPersistant menuDetail = (MenuCompositionDetailPersistant) menuCompositionService.findById(MenuCompositionDetailPersistant.class, menuDetailId);
						// Quand on remante, le maximum qu'on peut atteindre en remontant les niveau des
						// familles au sein d'un menu
						Integer maxLevel = menuDetail.getOpc_famille().getLevel();

						FamilleCuisinePersistant parentFamille = (FamilleCuisinePersistant) familleService.getFamilleParent(familleId);

						if (parentFamille == null || parentFamille.getLevel() < maxLevel) {
							MenuCompositionPersistant parentMenu = menuCompositionService.getMenuParent(parentMenuComp.getId());
							if (parentMenu != null && parentMenu.getLevel() > 0) {
								populateSousMenuCompositionPanel(httpUtil, parentMenu, true, listData);
							}
							return;
						}

						//
						populateSousFamilleMenuPanel(httpUtil, null, menuDetail, parentFamille, listData);
					} else if (name.startsWith("MEN_COMP")) {// Sous menu list choix ou famille
						Long elementId = Long.valueOf(name.substring(9));// Retirer le prefixi MEN_
						MenuCompositionDetailPersistant parentMenuDetComp = (MenuCompositionDetailPersistant)menuCompositionService.findById(MenuCompositionDetailPersistant.class, elementId);// Sans remonter
						MenuCompositionPersistant parentMenuComp = parentMenuDetComp.getOpc_menu();// Sans remonter
						// Alimenter le panel des sous menus
						populateSousMenuCompositionPanel(httpUtil, parentMenuComp, true, listData);
					} else if (name.startsWith("MEN_CHOIX")) {// List choix ou famille
						String choixIdDet = name.substring(10);
						String[] idsSt = StringUtil.getArrayFromStringDelim(choixIdDet, "-");
						Long choixId = Long.valueOf(idsSt[0]);
						Long menuCompDetailId = Long.valueOf(idsSt[1]);

						ListChoixDetailPersistant choixP = (ListChoixDetailPersistant) menuCompositionService.findById(ListChoixDetailPersistant.class, choixId);
						MenuCompositionDetailPersistant menuCompoDetailParent = (MenuCompositionDetailPersistant) menuCompositionService.findById(MenuCompositionDetailPersistant.class, menuCompDetailId);
						//
						populateMenuListChoix(httpUtil, null, menuCompoDetailParent, choixP.getOpc_choix_parent(), listData);
					} else {// Sous menu --------------------------------------------------------
						Long elementId = Long.valueOf(name.substring(4));// Retirer le prefixi MEN_
						MenuCompositionPersistant parentMenuComp = menuCompositionService.getMenuParent(elementId);
						if (parentMenuComp != null && parentMenuComp.getLevel() == 0) {
							return;
						}
						// Alimenter le panel des sous menus
						if(parentMenuComp != null) {
							populateSousMenuCompositionPanel(httpUtil, parentMenuComp, true, listData);
						}
					}
				} else {// Sous famille --------------------------------------------------------
					Long elementId = Long.valueOf(name.substring(4));// Retirer le prefix FAM_
					FamillePersistant parentFamille = familleService.getFamilleParent(elementId);
					if (parentFamille == null || parentFamille.getLevel() == 0) {
						return;
					}
					//List<FamillePersistant> listSousFamille = familleService.getFamilleEnfants("CU", parentFamille.getId(), true);
					
					List<FamillePersistant> listSousFamille = familleService2.getFamilleEnfants(parentFamille.getId(), ContextAppliCaisse.getCaisseBean().getId());
					
					// Alimenter le panel des sous menus
					populateSousFamillePanel(httpUtil, parentFamille.getId(), listSousFamille);
				}
			} 
			
			/**
			 * @param listSousFamille
			 */
			@SuppressWarnings("unchecked")
			private void populateSousFamillePanel(ActionUtil httpUtil, Long familleId, List<FamillePersistant> listSousFamille) {
				boolean isMobile = ContextAppli.APPLI_ENV.cais_mob.toString().equals(httpUtil.getUserAttribute("CURRENT_ENV"));
				FamilleCuisinePersistant parentfamille = (FamilleCuisinePersistant) familleService.getGenriqueDao().findById(familleId);
				httpUtil.setRequestAttribute("listFamille", listSousFamille);

				List<ArticlePersistant> listArticleActifs = articleService.getListArticleActifs(parentfamille.getId(), isMobile);
				httpUtil.setRequestAttribute("listArticle", listArticleActifs);
				
				// AJouter controle stock si il est paramétré
				addCtrlStock(httpUtil, listArticleActifs);
				
				httpUtil.setDynamicUrl("/domaine/"+httpUtil.getUserAttribute("PATH_JSP_CAISSE")+"/detail-choix.jsp");
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void menuCompoEvent(ActionUtil httpUtil) {
				
				MenuCompositionPersistant menuPersistant = null;
				// Si action step
				List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
				if(listNav == null){
					listNav = new ArrayList<>();
					httpUtil.setUserAttribute("HISTORIQUE_NAV", listNav);
				}
				if(httpUtil.getRequestAttribute("fromStep") != null){
					List<MenuCompositionPersistant> listSousMenu = (List<MenuCompositionPersistant>)httpUtil.getUserAttribute("LIST_SOUS_MENU");
					Integer step = (Integer)httpUtil.getUserAttribute("STEP_MNU");
					if(listSousMenu != null && listSousMenu.size() > step && step >= 0){
						menuPersistant = listSousMenu.get(step);
						menuPersistant = (MenuCompositionPersistant)menuCompositionService.findById(MenuCompositionPersistant.class, menuPersistant.getId());
						
						// Supprimer les niveaux d'avant afin de ne pas enchainer les niveaux sans raison
						if(listNav.size() >=2){
							String val1 = listNav.get(0), val2 = listNav.get(1);
							listNav.clear();
							listNav.add(val1);
							listNav.add(val2);
						}
						listNav.add("MEN_" + menuPersistant.getId());	
					}
				} else{
					Long currMenuId = httpUtil.getWorkIdLong();
					listNav.add("MEN_" + currMenuId);

					IGenericJpaDao genriqueDao = familleService.getGenriqueDao();
					if(currMenuId != null){
						menuPersistant = (MenuCompositionPersistant) genriqueDao.findById(MenuCompositionPersistant.class, currMenuId);
					}
					
					// Etapes du menus
					if(BooleanUtil.isTrue(menuPersistant.getIs_menu())){ 
						MenuCompositionPersistant menuOrigine = menuPersistant;
						CaissePersistant caisseBean = ContextAppliCaisse.getCaisseBean();
						List<MenuCompositionPersistant> listSousMenu = menuCompositionService.getListeMenuEnfants(currMenuId, (caisseBean!=null?caisseBean.getId():null), true);
						httpUtil.setUserAttribute("STEP_MNU", 0);
						httpUtil.setUserAttribute("LIST_SOUS_MENU", listSousMenu);
						
						httpUtil.setUserAttribute("CURRENT_MENU_COMPOSITION", menuPersistant);
						httpUtil.setUserAttribute("CURRENT_MENU_NUM", DateUtil.dateToString(new Date(), "MMss"));
						
						// Ajouter une étape
						if(listSousMenu != null && listSousMenu.size() > 0){
							menuPersistant = listSousMenu.get(0);
							listNav.add("MEN_" + menuPersistant.getId());
						}
						// Ajouter les articles inclus
						for(MenuCompositionDetailPersistant det : menuOrigine.getList_composition()){
							if(det.getOpc_article_inc() != null){
								manageAddArticleMenuNav(httpUtil, menuOrigine, det, null, det.getOpc_article_inc(), null);
							} 
						}
					}
				}
				
				//
				Map listData = new LinkedHashMap<>();
				populateSousMenuCompositionPanel(httpUtil, menuPersistant, false, listData);

				httpUtil.setRequestAttribute("listMenu", listData);

				httpUtil.setDynamicUrl(getDetail_choix_path(httpUtil));
			}
			
				/**
				 * Ajouter l'article à la commande
				 *
				 * @param articleId
				 */
				public void addArticleFamilleCmd(ActionUtil httpUtil) {
					if(httpUtil.getParameter("disStck") != null) {
						MessageService.addGrowlMessage("", "Stock insuffisant pour cet article.");
						return;
					}
					
					ArticlePersistant articleP = null;
					String codeBarre = httpUtil.getParameter("cb");
					String codePese = httpUtil.getParameter("pese");
					
					if(StringUtil.isNotEmpty(codeBarre)) {
						codeBarre = codeBarre.trim().replaceAll("", "");			
					}
					
					Map<String, String>  codesBarStart  = (Map<String, String>) MessageService.getGlobalMap().get("LIST_CODE_BALANCE");
					boolean isArtBalance = (codesBarStart != null && codeBarre != null && codeBarre.length() > 10 && codesBarStart.get(codeBarre.substring(0, 2)) != null);
					String codeBarreOri = codeBarre;
					String calvVal = httpUtil.getParameter("qte_calc");
					BigDecimal qte = StringUtil.isEmpty(calvVal) ? BigDecimalUtil.get(1) : BigDecimalUtil.get(calvVal);
					
					BigDecimal poids = null;
					if(StringUtil.isNotEmpty(codeBarre)) {
						codeBarre = codeBarre.trim().replaceAll("", "");
						manageDataSession(httpUtil);
						
						if(isArtBalance){
							String modeCompo = ContextGloabalAppli.getGlobalConfig("CODE_BARRE_BALANCE_COMPO");
					    	String[] composition = PrintCodeBarreBalanceUtil.getInfosCdeBarreBalance(modeCompo, codeBarreOri);
					    	String prefix = (composition.length>0 ? composition[0] : "");
					    	String barre = prefix + (composition.length>1 ? composition[1] : "");
					    	String pds = (composition.length>2 ? composition[2] : "");
					    	//String suffix = (composition.length>3 ? composition[3] : "");
					    	
							codeBarre = barre;
							poids = BigDecimalUtil.get(pds);
							articleP = articleService.getArticleByCodeBarre(codeBarre, true);
						} else{
							articleP = articleService.getArticleByCodeBarre(codeBarre, true);
						}
						
						if(articleP == null){
							boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
							
							// Carte fidélité
							CarteFideliteClientPersistant carteFideiteClientPersistant = isPoints ? carteFideliteClientService.getClientCarteByCodeBarre(codeBarre) : null;
							if(carteFideiteClientPersistant != null){
								CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
						        ClientPersistant opcClient = carteFideiteClientPersistant.getOpc_client();
						        CURRENT_COMMANDE.setOpc_client(opcClient);
								//
						        manageOffreTarif(httpUtil);
						        manageModeLivraison(httpUtil, false);
						        //				
								httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
								return;
							} else{
								MessageService.addGrowlMessage("Code barre non reconnu", "Ce code barre n'est associé à aucun élément.");
								return;
							}
						}
					} else {
						Long articleId = httpUtil.getWorkIdLong();
						if(articleId != null) {
							articleP = articleService.findById(articleId);
						}
					}

					if(articleP == null && StringUtil.isNotEmpty(codePese)){
						ArticleBalancePersistant artPeseP = (ArticleBalancePersistant) articleService2.getArticlesBalanceByCode(codePese);
						if(artPeseP != null){
							articleP = artPeseP.getOpc_article();
							poids = artPeseP.getPoids();
						}
					} else if(articleP != null 
							&& qte.compareTo(BigDecimalUtil.get(1)) > 0 
							&& StringUtil.isNotEmpty(articleP.getCode_barre())
							&& StringUtil.isEmpty(codeBarre) 
							&& StringUtil.isEmpty(codePese)){// Cas poids tappé dans la caisse
						isArtBalance = codesBarStart != null && codesBarStart.get(articleP.getCode_barre().substring(0, 2)) != null;
						if(isArtBalance){
							ValTypeEnumPersistant uniteVenteP = articleP.getOpc_unite_vente_enum();
							String uniteVente = "KG";// Par defaut
							if(uniteVenteP != null){
								uniteVente = articleP.getOpc_unite_vente_enum().getCode();
							}
							poids = qte;
							if(uniteVente.equalsIgnoreCase("KG") || uniteVente.equalsIgnoreCase("L")){
								poids = BigDecimalUtil.multiply(poids, BigDecimalUtil.get(1000));
							}
							qte = BigDecimalUtil.get(1);
						}
					}
					
					//
					if(articleP == null) {
						MessageService.addGrowlMessage("Article non reconnu", "Ce code barre n'est associé à aucun produit.");
						return;
					}
					
					List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
					if(listNav == null){
						listNav = new ArrayList<>();
						httpUtil.setUserAttribute("HISTORIQUE_NAV", listNav);
					}
					
					List<FamillePersistant> listParent = familleService.getFamilleParent("CU", articleP.getOpc_famille_cuisine().getId());
					for (FamillePersistant famillePersistant : listParent) {
						((List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV")).add("FAM_" + famillePersistant.getId());
					}
					

					for (FamillePersistant famillePersistant : listParent) {
						listNav.add("FAM_" + famillePersistant.getId());
					}
					ValTypeEnumPersistant uniteVenteP = articleP.getOpc_unite_vente_enum();
					// Gestion de la pese
					if(poids != null){
						String uniteVente = "KG";// Par defaut
						
						if(uniteVenteP != null){
							uniteVente = articleP.getOpc_unite_vente_enum().getCode();
						}
						BigDecimal prixVente = BigDecimalUtil.ZERO;
						//
						if(uniteVente.equalsIgnoreCase("KG") || uniteVente.equalsIgnoreCase("L")){
							BigDecimal poidsKg = BigDecimalUtil.divide(poids, BigDecimalUtil.get(1000));
							prixVente = BigDecimalUtil.multiply(articleP.getPrix_vente(), poidsKg);
						} else if(uniteVente.equalsIgnoreCase("G") || uniteVente.equalsIgnoreCase("ML")){
							prixVente = BigDecimalUtil.multiply(articleP.getPrix_vente(), poids);
						} else{// Piece, boite, ....
							prixVente = BigDecimalUtil.multiply(articleP.getPrix_vente(), poids);
						}
							
						articleP.setPrix_vente_tmp(prixVente);
						
						BigDecimal ration = poids;
						if(uniteVente.equalsIgnoreCase("KG") || uniteVente.equalsIgnoreCase("L")){ 
							ration = BigDecimalUtil.divide(ration, BigDecimalUtil.get(1000));
						}
						articleP.setLibelle_compl(" ["+BigDecimalUtil.formatNumber(ration)+ "" + uniteVente.toUpperCase()+"]");
					} else{
						articleP.setPrix_vente_tmp(articleP.getPrix_vente());
					}
					
					// Contrôler la cohérence de la quantité
					if(BigDecimalUtil.isZero(qte)) {
						MessageService.addGrowlMessage("Quantité incohérente", "La quantité saisie n'est pas valide.");
					} else  if(uniteVenteP != null){
						if("P".equals(uniteVenteP.getCode()) && !BigDecimalUtil.isZero(qte.remainder(BigDecimalUtil.get(1)))){
							MessageService.addGrowlMessage("Quantité incohérente", "La quantité saisie n'est pas valide (Article PIECE).");
							return;
						}
					}
					
					// Ajouter l'article
					addMouvementArticleFamilleCmd(httpUtil, articleP, qte, isArtBalance);
					
					httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
				}
				
			   /**
				 * @param httpUtil
				 */
				public void manageLecteursCarteBarre(ActionUtil httpUtil) {
					CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
					String codeBarre = httpUtil.getParameter("cb");
					boolean isFounded = false;
					if(StringUtil.isNotEmpty(codeBarre)) {
						codeBarre = codeBarre.trim().replaceAll("", "");
						
						Map<String, String> codesBarStart = (Map<String, String>) MessageService.getGlobalMap().get("LIST_CODE_BALANCE");
						boolean isArtBalance = (codesBarStart != null && codeBarre != null && codeBarre.length() > 10 && codesBarStart.get(codeBarre.substring(0, 2)) != null);
						
						if(isArtBalance){
							addArticleFamilleCmd(httpUtil);
							return;
						}
						
						// On test si c'est un article en premier
						ArticlePersistant articleP = articleService.getArticleByCodeBarre(codeBarre, true);
						CaisseMouvementPersistant commandeP = codeBarre.length()>12 ? caisseMvmService.getCommandeByCodeBarre(codeBarre.substring(0, 12)) : null;
						
						if(articleP != null){
							isFounded = true;
							manageDataSession(httpUtil);
							List<FamillePersistant> listParent = familleService.getFamilleParent("CU", articleP.getOpc_famille_cuisine().getId());
							List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
							if(listNav == null){
								listNav = new ArrayList<>();
								httpUtil.setUserAttribute("HISTORIQUE_NAV", listNav);
							}
							
							for (FamillePersistant famillePersistant : listParent) {
								listNav.add("FAM_" + famillePersistant.getId());
							}
							String calvVal = httpUtil.getParameter("qte_calc");
							BigDecimal qte = StringUtil.isEmpty(calvVal) ? BigDecimalUtil.get(1) : BigDecimalUtil.get(calvVal);

							// Ajouter l'article
							addMouvementArticleFamilleCmd(httpUtil, articleP, qte, false);
						} else if(commandeP != null) {
							if(!ContextAppliCaisse.getJourneeBean().getId().equals(commandeP.getOpc_caisse_journee().getOpc_journee().getId())) {
								MessageService.addGrowlMessage("Code barre non reconnu", "Cette commande n'est pas effectuée aujourd'hui.<br>Veuillez enregistrer le retour dans le back-office.");
								return;
							}
							
							httpUtil.setUserAttribute("CURRENT_COMMANDE", commandeP);
							isFounded = true;
						} else {
							boolean isPoints =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("POINTS"));
							CarteFideliteClientPersistant carteFideiteClientPersistant = null;
							if(isPoints){
								carteFideiteClientPersistant = carteFideliteClientService.getClientCarteByCodeBarre(codeBarre);
								if(carteFideiteClientPersistant != null){
									isFounded = true;
							        ClientPersistant opcClient = carteFideiteClientPersistant.getOpc_client();
							        CURRENT_COMMANDE.setOpc_client(opcClient);
									//
							        manageOffreTarif(httpUtil);
							        manageModeLivraison(httpUtil, false);
								}
							}
							
							if(carteFideiteClientPersistant == null){
								UserBean userBean = userService.getUserByBadge(codeBarre);
								if(userBean != null){
									boolean isServeur = userBean.isInProfile("SERVEUR");
									boolean isLivreur = userBean.isInProfile("LIVREUR");
									
									if(isServeur){
										isFounded = true;
										if(CURRENT_COMMANDE.getOpc_serveur() == null) {
											CURRENT_COMMANDE.setOpc_serveur(userBean);
										}
									}
									if(isLivreur && userBean.getOpc_employe() != null){
										isFounded = true;
										if(CURRENT_COMMANDE.getOpc_livreurU() == null) {
											CURRENT_COMMANDE.setOpc_livreurU(userBean);
										}
									} else{
										isFounded = true;
										if(CURRENT_COMMANDE.getOpc_employe() == null) {
											CURRENT_COMMANDE.setOpc_employe(userBean.getOpc_employe());
										}
									}
								}
							}
						}
						
						if(isFounded){
							httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
						} else{
							if(codeBarre == null || codeBarre.length() == 10){
								MessageService.addGrowlMessage("Badge ou code non reconnu", "Ce badge/code n'est associé à aucun élément.");	
							} else{
								MessageService.addGrowlMessage("Code barre non reconnu", "Ce code barre n'est associé à aucun élément.");								
							}
						}
					}
				}
				
				public void merge_depense(ActionUtil httpUtil) {
					if(StringUtil.isEmpty(httpUtil.getParameter("chargeDivers.libelle")) 
							&& StringUtil.isEmpty(httpUtil.getParameter("chargeDivers.libelle"))) {
						MessageService.addBannerMessage("Veuillez saisir ou sélectionner un libellé.");
						return;
					}
					
					ArticlePersistant articleP = super.getArticleGeneric(httpUtil); 
					
					addMouvementArticleFamilleCmd(httpUtil, articleP, BigDecimalUtil.get(httpUtil.getParameter("quantite_custom")), false);
					httpUtil.setDynamicUrl(getCommande_detail_path(httpUtil));
				}
				
			   /**
				 * @param elementId
				 * @param code
				 * @param libelle
				 * @param artP
				 * @param prix
				 * @param qte
				 * @param typeLigne
				 * @param parentCode
				 * @param compositionDetailId
				 */
				@SuppressWarnings("unchecked")
				private void addMouvementArticleFamilleCmd(ActionUtil httpUtil, ArticlePersistant artP, BigDecimal qte, boolean isArtBalance) {
					Integer currentIdxClient = (Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT");
					List<String> listNav = (List<String>) httpUtil.getUserAttribute("HISTORIQUE_NAV");
					boolean isFav = httpUtil.getParameter("is_fav") != null;
					String modeTravail =  ContextGloabalAppli.getGlobalConfig("MODE_TRAVAIL_CUISINE");
					modeTravail = (StringUtil.isEmpty(modeTravail) ? "EO" : modeTravail);
					
					boolean isPrintOnly = "PO".equals(modeTravail) || "PE".equals(modeTravail);// PrintOnly or PrintEcran
					
					if(isFav){
						if(listNav == null){
							listNav = new ArrayList<>();
							httpUtil.setUserAttribute("HISTORIQUE_NAV", listNav);
						} else{
							listNav.clear();
						}
						List<FamillePersistant> listParent = familleService.getFamilleParent("CU", artP.getOpc_famille_cuisine().getId());
						for (FamillePersistant famillePersistant : listParent) {
							listNav.add("FAM_" + famillePersistant.getId()); 
						}
					}
					if(listNav == null){
						listNav = new ArrayList<>();
						httpUtil.setUserAttribute("HISTORIQUE_NAV", listNav);
					}
					if(listNav.size() == 0){
						List<FamillePersistant> listParent = familleService.getFamilleParent("CU", artP.getOpc_famille_cuisine().getId());
						for (FamillePersistant famillePersistant : listParent) {
							listNav.add("FAM_" + famillePersistant.getId()); 
						}
					}
					String currTable = (String)httpUtil.getUserAttribute("CURRENT_TABLE_REF");
					Long parentFamilleId = Long.valueOf(listNav.get(listNav.size() - 1).substring(4));
					FamillePersistant parentFamille = (FamillePersistant) familleService.getGenriqueDao().findById(parentFamilleId);
					String CURRENT_TABLE_REF = (String)httpUtil.getUserAttribute("CURRENT_TABLE_REF");
					Integer CURRENT_IDX_CLIENT = (Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT");
					
					// Marquer la dernière ligne ajoutée
					String currId = currentIdxClient
										+ "-" + artP.getId() 
										+ "-" + TYPE_LIGNE_COMMANDE.ART.toString()
										+ "-" + parentFamille.getCode()
										+ (StringUtil.isNotEmpty(CURRENT_TABLE_REF) ? "-"+CURRENT_TABLE_REF : "");
					httpUtil.setUserAttribute("CURRENT_ITEM_ADDED", currId);
					// Pour le traceur
					httpUtil.setUserAttribute("CURRENT_ART_TRACK", artP.getLibelle()+"|"+qte+"|"+BigDecimalUtil.formatNumber(artP.getPrix_vente()));
					
					CaisseMouvementPersistant CURRENT_COMMANDE = (CaisseMouvementPersistant) httpUtil.getUserAttribute("CURRENT_COMMANDE");
					List<CaisseMouvementArticlePersistant> listDetaimMvm = CURRENT_COMMANDE.getList_article();

					// Maj commande
					CaisseMouvementArticlePersistant cmdP = null;
					boolean isArticleExist = false;

					if(!isArtBalance){
						for (CaisseMouvementArticlePersistant cmd : listDetaimMvm) {
							if (cmd == null || BooleanUtil.isTrue(cmd.getIs_annule())) {
								continue;
							}
	
							String currType = cmd.getType_ligne();
							String currParentCode = cmd.getParent_code();
							String currMenuIdx = cmd.getMenu_idx();
							
							if (currMenuIdx == null) {
								if (currType.equals(TYPE_LIGNE_COMMANDE.ART.toString())) {
									Long currElementId = cmd.getOpc_article().getId();
									if (currParentCode.equals("F_"+parentFamille.getCode()) 
											&& currElementId.equals(artP.getId())
											&& (""+cmd.getRef_table()).equals(""+currTable)
											&& !BooleanUtil.isTrue(cmd.getIs_annule())
											&& !BooleanUtil.isTrue(cmd.getIs_offert())
											&& !artP.getCode().startsWith("GEN_")
											&& cmd.getIdx_client().equals(currentIdxClient)) {
										isArticleExist = true;
									}
								}
							}
							if (isArticleExist) {
								cmdP = cmd;
								break;
							}
						}
					}

					boolean isElementChange = true;
					if (cmdP == null) {
						isElementChange = false;
						
						// Ajouter le groupe
						addMouvementGroupLevelsCmd(httpUtil);
						//
						cmdP = new CaisseMouvementArticlePersistant();

						cmdP.setCode(artP.getCode());
						cmdP.setLibelle(artP.getLibelle() + StringUtil.getValueOrEmpty(artP.getLibelle_compl()));
						cmdP.setType_ligne(TYPE_LIGNE_COMMANDE.ART.toString());
						cmdP.setParent_code("F_"+parentFamille.getCode());

						cmdP.setOpc_article(artP);

						cmdP.setOpc_mouvement_caisse(CURRENT_COMMANDE);

						cmdP.setOpc_menu(null);
						cmdP.setMenu_idx(null);
						cmdP.setElementId(artP.getId());
						cmdP.setComposition_detail_id(null);
						//-------------------------------
						cmdP.setIdx_client((Integer)httpUtil.getUserAttribute("CURRENT_IDX_CLIENT")); 
						if(cmdP.getRef_table() == null){
							cmdP.setRef_table((String) httpUtil.getUserAttribute("CURRENT_TABLE_REF"));
						}
						// Couverts table
						if(cmdP.getRef_table() != null){
							Map<String, Integer> mapCouvertsTable = (Map<String, Integer>)httpUtil.getMenuAttribute("COUVERTS_TABLE");
							if(mapCouvertsTable != null){
								cmdP.setNbr_couvert(mapCouvertsTable.get(cmdP.getRef_table()));
							}
						}
						listDetaimMvm.add(cmdP);
						
						// Gestion de la garantie ---------------------------------
						ajouterGroupe(httpUtil);
						ajouterFraisGarantie(httpUtil);
					}
					if (qte != null) {
						BigDecimal newQte = BigDecimalUtil.add(cmdP.getQuantite(), qte);
						// Prendre en compte les prix spécifique à un client
						ArticleClientPrixPersistant ccP = null;
						if(CURRENT_COMMANDE.getOpc_client() != null){
							ccP = caisseWebService.getArticleClientPrix(CURRENT_COMMANDE.getOpc_client().getId(), artP.getId());
						}
						
						if(ccP != null){
							cmdP.setMtt_total(BigDecimalUtil.multiply(ccP.getMtt_prix(), newQte));
							cmdP.setIs_client_pr(true);
						} else{
							cmdP.setMtt_total(BigDecimalUtil.multiply((artP.getPrix_vente_tmp()!=null?artP.getPrix_vente_tmp():artP.getPrix_vente()), newQte));
						}
						
//						if(CURRENT_COMMANDE.getId() != null) {
							if(cmdP.getId() == null && cmdP.getType_opr() == null) {
								cmdP.setType_opr(Integer.valueOf(1)); //article added
//								cmdP.setOld_qte_line(BigDecimalUtil.isZero(cmdP.getQuantite()) ? BigDecimalUtil.get(0) : cmdP.getQuantite() );
							}
//						}
						cmdP.setQuantite(newQte);
						if(CURRENT_COMMANDE.getId() != null && cmdP.getType_opr() == null) {
							cmdP.setType_opr(Integer.valueOf(2));
						}

						// Maj statut pour les écrans cuisine
//						if(isElementChange){
							if(isPrintOnly) {
								cmdP.setLast_statut(ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());
							} else {
								boolean isAutoCmdPrep = StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("ECRAN_CMD_AUTO"));// Si passage à prête directement
								String last_statut = (isAutoCmdPrep ? ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.PREP.toString() : ContextAppli.STATUT_CAISSE_MOUVEMENT_ENUM.VALIDE.toString());
								cmdP.setLast_statut(last_statut);
							}
//						}
					}
					sortAndAddCommandeLigne(httpUtil, CURRENT_COMMANDE);

					// Maj total de la commande
					majTotalMontantCommande(httpUtil, CURRENT_COMMANDE);

					// Vérifier si on doit activer ou désactiver une offre ----------------------
					manageOffreTarif(httpUtil);
					manageModeLivraison(httpUtil, false);
				}
				
				/**
				 * @param httpUtil
				 */
				public void manage_reparition_conf(ActionUtil httpUtil){
					httpUtil.setRequestAttribute("listFamilles", familleService.getListeFamille("CU", true, false));
					httpUtil.setRequestAttribute("listMenus", menuService.getListeMenu(true, true));
					httpUtil.setRequestAttribute("listArticles", caisseService.getListArticleCaisseActif());
					
					if(httpUtil.getParameter("is_save") != null){
						Map<String, Object> dataMenu = httpUtil.getValuesByStartName("menu_");
//						Map<String, Object> dataArticle = httpUtil.getValuesByStartName("article_");
						String[] familles = httpUtil.getRequest().getParameterValues("familles_array");
						
						String familleStr = "";
						String menuArtStr = "";
						//
						if(familles != null){
							for (String fam : familles) {
								familleStr = familleStr + fam + ";";
							}
						}
						if(dataMenu != null){
							for(String key : dataMenu.keySet()){
								String[] dataArt = httpUtil.getRequest().getParameterValues("article_"+key);
								
								if(dataArt != null && dataArt.length > 0){
									String menuId = ""+dataMenu.get(key);
									String arts = "";
									for(String artId : dataArt){
										arts = arts + artId + "-";
									}
									arts = arts.substring(0, arts.length()-1);
									menuArtStr = menuArtStr + menuId+":"+arts+";";
								}
							}
						}
						menuService.saveConf(menuArtStr, familleStr);
						MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Mise à jour", "La mise à jour est effetctuée avec succès.");
					}
					
					EtablissementPersistant restauP = (EtablissementPersistant) menuService.findById(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
					String famStr = restauP.getVente_familles();
					String menuArtStr = restauP.getVente_menus_art();
					Map<Long, Long[]> mapMenu = new HashMap<>();
					List<Long> famArray = new ArrayList<Long>();
					
					// Famille
					if(StringUtil.isNotEmpty(famStr)){
						for(String fam : StringUtil.getArrayFromStringDelim(famStr, ";")){
							famArray.add(Long.valueOf(fam));
						}
					}
					// Articles
					if(StringUtil.isNotEmpty(menuArtStr)){
						for(String mnuArt : StringUtil.getArrayFromStringDelim(menuArtStr, ";")){
							String[] det = StringUtil.getArrayFromStringDelim(mnuArt, ":");
							Long menuId = Long.valueOf(det[0]);
							List<Long> artArray = new ArrayList<Long>();
							String[] arts = StringUtil.getArrayFromStringDelim(det[1], "-");
							//
							for (String art : arts) {
								artArray.add(Long.valueOf(art));
							}
							mapMenu.put(menuId, artArray.toArray(new Long[artArray.size()]));
						}
					}
					
					httpUtil.setRequestAttribute("dataFamille", famArray.toArray(new Long[famArray.size()]));
					httpUtil.setRequestAttribute("dataMenu", mapMenu);
					
					httpUtil.setDynamicUrl("/domaine/caisse_restau/back/repartition_article_vente_mnu_config.jsp");
				}
   }
   