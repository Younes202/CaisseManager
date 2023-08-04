package appli.model.domaine.stock.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.IEtatFinanceService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.ArticleStockInfoPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamillePersistant;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IInventaireService;
import appli.model.domaine.vente.persistant.CaisseMouvementPersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.StringUtil;

@Named
public class MouvementValidator {
	@Inject
	private IMouvementDao mouvementDao;
	@Inject
	private IInventaireService inventaireService;
	@Inject 
	private IEtatFinanceService etatFinancierService;
	@Inject 
	private IFamilleService familleService;
	@Inject 
	private IClientService clientService;
	
	/*
	 * 
	 */
	public void updateCreateValidator(MouvementBean mouvementBean) {
		boolean isModeAdmin = MessageService.getGlobalMap().get("IS_ADMIN_MODE") != null;
		// Contrôler s'il y a un inventaire non validé -------------------------------------------------
		boolean isCtrlInventaire =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("INVENTAIRE_OBLIGATOIRE_MVM"));
		boolean isCtrlStockBO =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CTRL_STOCK_MVM_BO"));
		boolean isBlocageDepassEcheancePaiement =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("BLOCAGE_ECHEANCE_PAIE"));
		boolean isCtrlEmplacement =  StringUtil.isTrue(ContextGloabalAppli.getGlobalConfig("CTRL_EMPLACEMENT"));
		String minMargeStr =  ContextGloabalAppli.getGlobalConfig("MINIMUM_MARGE_BO");
		
		//
		if(isCtrlInventaire){
			List<InventairePersistant> listInventaire = mouvementDao.getQuery("from InventairePersistant where is_valid is null or is_valid=0")
				.getResultList();
			
			if(listInventaire.size() > 0) {
				MessageService.addBannerMessage("Un inventaire non validé (le "+DateUtil.dateToString(listInventaire.get(0).getDate_realisation())+") a été trouvé. "
						+ "Veuillez le valider avant de continuer.");
				return;
			}
		}
		
		String type_mvmnt = mouvementBean.getType_mvmnt();
		if(type_mvmnt.equals("t") && mouvementBean.getId()!=null && mouvementBean.getType_transfert()==null){
			mouvementBean.setType_transfert("A");
		}
		
		if(!"c".equals(type_mvmnt) && !"p".equals(type_mvmnt) 
				&& StringUtil.isEmpty(mouvementBean.getNum_bl()) 
				&& StringUtil.isEmpty(mouvementBean.getNum_facture()) 
				&& StringUtil.isEmpty(mouvementBean.getNum_recu())){
			MessageService.addBannerMessageKey("mouvement.msg.numblfacture");
		}
		
		Long emplacementId = null;
		if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.t.toString()) || type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.tr.toString())) {
			emplacementId = mouvementBean.getOpc_emplacement().getId();
			Date dateMax = inventaireService.getMaxDateInventaireValide(emplacementId);
			if(dateMax != null && mouvementBean.getDate_mouvement().before(dateMax)){
				MessageService.addBannerMessage("La date du mouvement doit être postérieure ou égale à la date du dernier inventaire ("+DateUtil.dateToString(dateMax)+")");
			}
			if(mouvementBean.getOpc_destination() != null) {
				emplacementId = mouvementBean.getOpc_destination().getId();
			}
		} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.a.toString())) {
			if(mouvementBean.getOpc_destination() != null) {
				emplacementId = mouvementBean.getOpc_destination().getId();
			}
		} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.av.toString())) {
			if(mouvementBean.getOpc_emplacement() != null) {
				emplacementId = mouvementBean.getOpc_emplacement().getId();
			}
		} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.v.toString())) {
			if(mouvementBean.getOpc_destination() != null) {
				emplacementId = mouvementBean.getOpc_destination().getId();
			}
		} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.p.toString())) {
			if(mouvementBean.getOpc_emplacement() != null) {
				emplacementId = mouvementBean.getOpc_emplacement().getId();
			}
		} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.c.toString())) {
			if(mouvementBean.getOpc_emplacement() != null) {
				emplacementId = mouvementBean.getOpc_emplacement().getId();
			}
		} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.rt.toString())) {
			if(mouvementBean.getOpc_destination() != null) {
				emplacementId = mouvementBean.getOpc_destination().getId();
			}
		}
		
		if(emplacementId == null && !type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.dv.toString()) && !type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.cm.toString())) {
			MessageService.addBannerMessage("Veuillez sélectionner un emplacement stock");
			return;
		}
		
		// La date du mouvement doit ÃªtrpostÃ©rieure Ã  la date du dernier inventaire
		boolean isDtMax = false;
		Date dateMax = inventaireService.getMaxDateInventaireValide(emplacementId);
		if(dateMax != null && mouvementBean.getDate_mouvement().before(dateMax)){
			MouvementPersistant mvmDB = null;
			if(mouvementBean.getId() != null){
				mvmDB = mouvementDao.findById(mouvementBean.getId());
			}
			if(mouvementBean.getId() == null || mvmDB.getDate_mouvement().compareTo(mouvementBean.getDate_mouvement())!=0){
				MessageService.addBannerMessage("La date du mouvement doit être postérieure ou égale à "
						+ "la date du dernier inventaire ("+DateUtil.dateToString(dateMax)+")");	
			} else{
				isDtMax = true;				
			}
		}
		
		if(!isDtMax){
			if((type_mvmnt.equals("t") && "A".equals(mouvementBean.getType_transfert())) || !type_mvmnt.equals("t")){
				if(mouvementBean.getList_article() == null || mouvementBean.getList_article().size() == 0){
					MessageService.addBannerMessageKey("mouvement.msg.articles");
				} else{
					List<MouvementArticlePersistant> listArticle = mouvementBean.getList_article();
					for (MouvementArticlePersistant mouvementArticlePersistant : listArticle) {
						int idxIhm = mouvementArticlePersistant.getIdxIhm(); 
						if(mouvementArticlePersistant.getOpc_article() == null){
							MessageService.addFieldMessageKey("opc_article.id_"+idxIhm, "work.required.error.short");
						}
						if(mouvementArticlePersistant.getQuantite() == null){
							MessageService.addFieldMessageKey("quantite_"+idxIhm, "work.required.error.short");
						} 
						else if(type_mvmnt.equals("t") || type_mvmnt.equals("v") || type_mvmnt.equals("c")){
							if(isCtrlStockBO){
								Map<Long , BigDecimal> mapArticle = new HashMap<>();
								List<ArticleStockInfoPersistant> listArticleEtat = mouvementDao.getQuery("from ArticleStockInfoPersistant etatArt where etatArt.opc_emplacement.id = :destinationId")
										.setParameter("destinationId", mouvementBean.getOpc_emplacement().getId()).getResultList();
								for (ArticleStockInfoPersistant articleEtat : listArticleEtat) {
									BigDecimal qteStock = BigDecimalUtil.substract(articleEtat.getQte_entree(), articleEtat.getQte_sortie());
									mapArticle.put(articleEtat.getOpc_article().getId(), qteStock);
								}
								BigDecimal qteArticle = mapArticle.get(mouvementArticlePersistant.getOpc_article().getId());
								qteArticle = qteArticle == null ? BigDecimalUtil.ZERO : qteArticle;
								
								if(mouvementArticlePersistant.getQuantite() != null && mouvementArticlePersistant.getQuantite().compareTo(qteArticle) > 0){
									ArticlePersistant article = mouvementDao.findById(ArticlePersistant.class, mouvementArticlePersistant.getOpc_article().getId());
									MessageService.addBannerMessage("Il manque "+BigDecimalUtil.formatNumber(BigDecimalUtil.substract(mouvementArticlePersistant.getQuantite(), qteArticle))+" éléments pour l'article "+article.getLibelle());
								}
							}
						}
						if(type_mvmnt.equals("a")){
							if(mouvementArticlePersistant.getOpc_tva_enum() == null){
								MessageService.addFieldMessageKey("opc_tva_enum.id_"+idxIhm, "work.required.error.short");
							}
							if(mouvementArticlePersistant.getPrix_ht() == null){
								MessageService.addFieldMessageKey("prix_ht_"+idxIhm, "work.required.error.short");
							}
						}
						if(type_mvmnt.equals("v")){
							if(mouvementArticlePersistant.getPrix_vente() == null){
								MessageService.addFieldMessageKey("prix_vente_"+idxIhm, "work.required.error.short");
							}
						}
					}
				}
			}
		}
		
		if(MessageService.isError()){
			return;
		}

		if(mouvementBean.getDate_mouvement()!=null && etatFinancierService.isMoisClos(mouvementBean.getDate_mouvement())) {
			MessageService.addBannerMessage("La date du mouvement appartient à un mois clos.");
		}
		if(mouvementDao.isNotUnique(mouvementBean, "num_bl")){
			MessageService.addFieldMessage("mouvement.num_bl", "Cette valeur existe déjà");
		}
		if(mouvementDao.isNotUnique(mouvementBean, "num_facture")){
			MessageService.addFieldMessage("mouvement.num_facture", "Cette valeur existe déjà");
		}
		
		if(mouvementBean.getOpc_destination() != null && "t".equals(type_mvmnt)){
			if(mouvementBean.getOpc_destination().getId().equals(mouvementBean.getOpc_emplacement().getId())){
				MessageService.addBannerMessage("Le stock destination doit être différent du stock d'origine");
			}
		}
		
		// Il ne doit pas y avoir d'inventaire non valide sur ce stock
		if(mouvementBean.getOpc_emplacement() != null){
			if(inventaireService.getInventaireNonValide(mouvementBean.getOpc_emplacement().getId()).size() > 0){
				MessageService.addBannerMessage("Un inventaire non validé est lié à cet emplacement. Veuillez le valider en premier.");
				return;
			}
		}
		 if(mouvementBean.getOpc_destination() != null){
			 if(inventaireService.getInventaireNonValide(mouvementBean.getOpc_destination().getId()).size() > 0){
				 MessageService.addBannerMessage("Un inventaire non validé est lié à cet emplacement. Veuillez le valider en premier.");
				 return;
			 }
		 }
		 if(isCtrlEmplacement){
			 boolean isValid = true;
			 if(mouvementBean.getOpc_destination() != null){
				 EmplacementPersistant empl = (EmplacementPersistant)mouvementDao.findById(EmplacementPersistant.class, mouvementBean.getOpc_destination().getId());
				 isValid = isFamilleArtValide(empl.getFamilles_ex_cmd(), mouvementBean, true); 
				 if(isValid){isValid = isFamilleArtValide(empl.getFamilles_cmd(), mouvementBean, false);};
				 if(isValid){isValid = isArtValide(empl.getArticles_ex_cmd(), mouvementBean, true);}; 
				 if(isValid){isValid = isArtValide(empl.getArticles_cmd(), mouvementBean, false);};
			 }
			 if(isValid && mouvementBean.getOpc_emplacement() != null){
				 EmplacementPersistant empl = (EmplacementPersistant)mouvementDao.findById(EmplacementPersistant.class, mouvementBean.getOpc_emplacement().getId());
				 if(isValid){isValid = isFamilleArtValide(empl.getFamilles_ex_cmd(), mouvementBean, true);}; 
				 if(isValid){isValid = isFamilleArtValide(empl.getFamilles_cmd(), mouvementBean, false);};
				 if(isValid){isValid = isArtValide(empl.getArticles_ex_cmd(), mouvementBean, true);};
				 if(isValid){isValid = isArtValide(empl.getArticles_cmd(), mouvementBean, false);};
			 }
		}
		 
		// Contrôler les marges
		 if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.v.toString())){
			 BigDecimal minMarge = null;
			 if(StringUtil.isNotEmpty(minMargeStr)){
				 minMarge = BigDecimalUtil.get(minMargeStr);
			 }
			 List<MouvementArticlePersistant> listArticle = mouvementBean.getList_article();
			 //
			 for (MouvementArticlePersistant mouvementArtP : listArticle) {
				ArticlePersistant opc_article = mouvementArtP.getOpc_article();
				
				if(!BigDecimalUtil.isZero(opc_article.getMin_marge())){
					minMarge = opc_article.getMin_marge();
				}
				
				if(!BigDecimalUtil.isZero(minMarge)){
					BigDecimal marge = BigDecimalUtil.substract(mouvementArtP.getPrix_ttc(), opc_article.getPrix_achat_ttc());
					BigDecimal margeTaux = BigDecimalUtil.divide(BigDecimalUtil.multiply(marge, BigDecimalUtil.get(100)), mouvementArtP.getPrix_ttc());
					//
					if(margeTaux.compareTo(minMarge) < 0){
						MessageService.addBannerMessage("La marge sur l'article "+opc_article.getLibelle()+" doit être d'au moins "+BigDecimalUtil.formatNumber(minMarge));
					}
				}
			 }
			 
			 // Vérifier le plafond de la dette client
			 BigDecimal plafond_dette = (mouvementBean.getOpc_client() != null ? mouvementBean.getOpc_client().getPlafond_dette() : null);
			 if(mouvementBean.getOpc_client() != null 
					 && !BigDecimalUtil.isZero(plafond_dette)){
				 Date currDate = new Date();
				 BigDecimal mttEcheance = null;
				 if(!StringUtil.isEmptyList(mouvementBean.getList_paiement())){
					 for(PaiementPersistant paiementP : mouvementBean.getList_paiement()){
						if(paiementP.getDate_echeance() != null && paiementP.getDate_echeance().after(currDate)){
							mttEcheance = BigDecimalUtil.add(mttEcheance, paiementP.getMontant());
						 }
					 }
				 }
				 if(!BigDecimalUtil.isZero(mttEcheance)){
					 BigDecimal detteClient = clientService.getSituationDetteClient(mouvementBean.getOpc_client().getId());
					 detteClient = BigDecimalUtil.add(detteClient, mttEcheance);
					 
					if(!isModeAdmin &&
							 detteClient != null && detteClient.compareTo(plafond_dette) >= 0){
						 MessageService.addBannerMessage("Le plafond autorisé de la dette client ("+BigDecimalUtil.formatNumber(plafond_dette)+") est atteint. "
								 +ParametrageRightsConstantes.getAdminValidationMsg());
					 }
				 }
			 }
			 
			 if(!isModeAdmin && isBlocageDepassEcheancePaiement){
				 List<PaiementPersistant> listPaiementEcheance = clientService.getEcheancePasseClient(mouvementBean.getOpc_client().getId());
				 if(listPaiementEcheance.size() > 0){
					 MessageService.addBannerMessage("Une ou plusieures échéance de paiement non réglées ont été trouvée. "
					 		+ParametrageRightsConstantes.getAdminValidationMsg());
				 }
			 }
		 }
		 
		 // Vérifier les fournisseurs exclusifs
		 if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.a.toString())){
			 List<MouvementArticlePersistant> listArticle = mouvementBean.getList_article();
			 for (MouvementArticlePersistant mouvementArtP : listArticle) {
				ArticlePersistant opc_article = mouvementArtP.getOpc_article();
				opc_article = mouvementDao.findById(ArticlePersistant.class, opc_article.getId());
				String[] fournIds = StringUtil.getArrayFromStringDelim(opc_article.getFournisseurs(), "|");
				if(fournIds != null && fournIds.length > 0 && mouvementBean.getOpc_fournisseur() != null){
					List<String> ids = Arrays.asList(fournIds);
					
					if(!ids.contains(mouvementBean.getOpc_fournisseur().getId().toString())){
						MessageService.addBannerMessage("Le fournisseur ne correspond pas aux fournisseurs autorisés pour l'article "+opc_article.getLibelle());
					}
				}
			 }	
		 }
	}
	
	public void validerAvoir(Long avoirId){
		MouvementPersistant mouvementP = mouvementDao.findById(MouvementPersistant.class, avoirId);
		Integer dureeValiditeAvoir = Integer.valueOf(ContextGloabalAppli.getGlobalConfig("VALIDITE_AVOIR"));
		boolean isModeAdmin = MessageService.getGlobalMap().get("IS_ADMIN_MODE") != null;
		//
		if(!isModeAdmin && dureeValiditeAvoir != null && dureeValiditeAvoir > 0){
			 Date dateRef = DateUtil.addSubstractDate(mouvementP.getDate_creation(), TIME_ENUM.DAY, dureeValiditeAvoir);
			
			 if(new Date().before(dateRef)){
				 MessageService.addBannerMessage("La date de validité de cet avoir ("+DateUtil.dateToString(dateRef)+")  est dépassé."+ParametrageRightsConstantes.getAdminValidationMsg());
			 }
		}
	}
	
	private boolean isArtValide(String listArticle, MouvementBean mouvementBean, boolean isExclude){
		 List<Long> idsExcludeArt = new ArrayList<>();
		 String[] articles = StringUtil.getArrayFromStringDelim(listArticle, ";");
		 if(articles != null){
			for(String id : articles){
				if(StringUtil.isEmpty(id)){
					continue;
				}
				Long idArtLong = Long.valueOf(id);
				idsExcludeArt.add(idArtLong);
			}
		}
		 if(idsExcludeArt.size() == 0){
			return true;
		}
		List<MouvementArticlePersistant> listArt = mouvementBean.getList_article();
		for (MouvementArticlePersistant mouvementArt : listArt) {
			ArticlePersistant opc_article = mouvementArt.getOpc_article();
			opc_article = mouvementDao.findById(opc_article.getClass(), opc_article.getId());
			//
			if((isExclude && idsExcludeArt.contains(opc_article.getId()))
					|| (!isExclude && !idsExcludeArt.contains(opc_article.getId()))){
				MessageService.addBannerMessage("L'article "+opc_article.getCode()+"-"+opc_article.getLibelle()+" n'est pas paramétré pour cet emplacement.");
				return false;
			}
		}
		return true;
	}
	
	private boolean isFamilleArtValide(String listFamille, MouvementBean mouvementBean, boolean isExclude){
		 List<Long> idsExcludeFam = new ArrayList<>();
		 String[] familles = StringUtil.getArrayFromStringDelim(listFamille, ";");
		 if(familles != null){
			for(String id : familles){
				if(StringUtil.isEmpty(id)){
					continue;
				}
				Long idFamLong = Long.valueOf(id);
				idsExcludeFam.add(idFamLong);
				List<FamillePersistant> listEnfants = familleService.getFamilleEnfants("ST", idFamLong, true);
				for (FamillePersistant famillePersistant : listEnfants) {
					idsExcludeFam.add(famillePersistant.getId());
				}
			}
		}
		if(idsExcludeFam.size() == 0){
			return true;
		}
		 
		List<MouvementArticlePersistant> listArt = mouvementBean.getList_article();
		if(listArt != null){
			for (MouvementArticlePersistant mouvementArt : listArt) {
				ArticlePersistant opc_article = mouvementArt.getOpc_article();
				opc_article = mouvementDao.findById(opc_article.getClass(), opc_article.getId());
				//
				if((isExclude && idsExcludeFam.contains(opc_article.getOpc_famille_stock().getId()))
						|| (!isExclude && !idsExcludeFam.contains(opc_article.getOpc_famille_stock().getId()))){
					MessageService.addBannerMessage("L'article "+opc_article.getCode()+"-"+opc_article.getLibelle()+" n'est pas paramétré pour cet emplacement.");
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(MouvementBean mouvementBean) {
		updateCreateValidator(mouvementBean);
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(MouvementBean mouvementBean){
		updateCreateValidator(mouvementBean);
		
		Calendar cal = DateUtil.getCalendar(mouvementBean.getDate_mouvement());
		Date dateDebut = DateUtil.stringToDate("01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
		//
		EtatFinancePersistant etatP = (EtatFinancePersistant) mouvementDao.getSingleResult(mouvementDao.getQuery("from EtatFinancePersistant where date_etat=:dateDebut")
				.setParameter("dateDebut", dateDebut));
		
		if(etatP != null){
			MessageService.addBannerMessage("La date du mouvement appartient à un mois clos.");
		}
	}
	
	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){
		MouvementPersistant mouvementPersistant = mouvementDao.findById(id);
		
		if(BooleanUtil.isTrue(mouvementPersistant.getIs_groupant())){
			return;
		}
		
//		List<CaisseMouvementPersistant> listCaisseMvm = mouvementDao.getQuery("from CaisseMouvementPersistant where opc_mouvement.id=:mvmId")
//			.setParameter("mvmId", id)
//			.getResultList();
		List<CaisseMouvementPersistant> listCaisseMvmRetour = mouvementDao.getQuery("from CaisseMouvementPersistant where mvm_retour_ids like :mvmId")
				.setParameter("mvmId", "%;"+id+";%")
				.getResultList();
		
//		if(listCaisseMvm.size() > 0 || listCaisseMvmRetour.size() > 0){
//			MessageService.addBannerMessage("Cet mouvement est lié à un mouvement caisse.");
//		} else{
			Long emplacementId = null;
			String type_mvmnt = mouvementPersistant.getType_mvmnt();
			if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.t.toString()) || type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.tr.toString())) {
				emplacementId = mouvementPersistant.getOpc_emplacement().getId();
				Date dateMax = inventaireService.getMaxDateInventaireValide(emplacementId);
				if(dateMax != null && mouvementPersistant.getDate_mouvement().before(dateMax)){
					MessageService.addBannerMessage("La date du mouvement doit être postérieure ou égale à la date du dernier inventaire ("+DateUtil.dateToString(dateMax)+")");
				}
//				emplacementId = mouvementPersistant.getOpc_destination().getId();
			} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.a.toString())) {
				emplacementId = mouvementPersistant.getOpc_destination().getId();
			} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.v.toString())) {
				emplacementId = mouvementPersistant.getOpc_emplacement().getId();
			} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.p.toString())) {
				emplacementId = mouvementPersistant.getOpc_emplacement().getId();
			} else if(type_mvmnt.equals(TYPE_MOUVEMENT_ENUM.c.toString())) {
				emplacementId = mouvementPersistant.getOpc_emplacement().getId();
			}
			
			// La date du mouvement doit ÃªtrpostÃ©rieure Ã  la date du dernier inventaire
			Date dateMax = inventaireService.getMaxDateInventaireValide(emplacementId);
			if(dateMax != null && mouvementPersistant.getDate_mouvement().before(dateMax)){
				MessageService.addBannerMessage("La date du mouvement doit être postérieure ou égale à la date du dernier inventaire");
			}
//		}
		
		Calendar cal = DateUtil.getCalendar(mouvementPersistant.getDate_mouvement());
		Date dateDebut = DateUtil.stringToDate("01/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR));
		//
		EtatFinancePersistant etatP = (EtatFinancePersistant) mouvementDao.getSingleResult(mouvementDao.getQuery("from EtatFinancePersistant where date_etat=:dateDebut")
				.setParameter("dateDebut", dateDebut));
		
		if(etatP != null){
			MessageService.addBannerMessage("Ce mouvement appartient à un mois clos.");
		}
	}
	
}
