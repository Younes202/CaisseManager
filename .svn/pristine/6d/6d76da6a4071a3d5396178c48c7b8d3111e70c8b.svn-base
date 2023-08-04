package appli.model.domaine.stock.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.CompteBancaireBean;
import appli.controller.domaine.administration.bean.EtatFinanceBean;
import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.controller.domaine.stock.bean.CentraleSynchroBean;
import appli.controller.domaine.stock.bean.MouvementBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.SOFT_ENVS;
import appli.model.domaine.administration.persistant.EtatFinanceDetailPersistant;
import appli.model.domaine.administration.persistant.EtatFinancePaiementPersistant;
import appli.model.domaine.administration.service.IClientService;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.administration.service.impl.ParametrageService;
import appli.model.domaine.caisse.service.IJourneeService;
import appli.model.domaine.personnel.persistant.ClientPersistant;
import appli.model.domaine.stock.dao.IArticleDao;
import appli.model.domaine.stock.persistant.ArticlePersistant;
import appli.model.domaine.stock.persistant.DemandeTransfertArticlePersistant;
import appli.model.domaine.stock.persistant.DemandeTransfertPersistant;
import appli.model.domaine.stock.persistant.EmplacementPersistant;
import appli.model.domaine.stock.persistant.FamilleCuisinePersistant;
import appli.model.domaine.stock.persistant.FamilleFournisseurPersistant;
import appli.model.domaine.stock.persistant.FamilleStockPersistant;
import appli.model.domaine.stock.persistant.FournisseurPersistant;
import appli.model.domaine.stock.persistant.MouvementArticlePersistant;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import appli.model.domaine.stock.persistant.centrale.CentraleEtsPersistant;
import appli.model.domaine.stock.persistant.centrale.CentraleSynchroPersistant;
import appli.model.domaine.stock.service.ICentraleSynchroService;
import appli.model.domaine.stock.service.IEmplacementService;
import appli.model.domaine.stock.service.IFamilleService;
import appli.model.domaine.stock.service.IMouvementService;
import appli.model.domaine.vente.persistant.JourneePersistant;
import appli.model.domaine.vente.persistant.ListChoixPersistant;
import appli.model.domaine.vente.persistant.MenuCompositionPersistant;
import framework.controller.ControllerUtil;
import framework.controller.FileUtilController;
import framework.model.beanContext.CompteBancairePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.ModelConstante;

@Named
public class CentraleSynchroService extends GenericJpaService<CentraleSynchroBean, Long> implements ICentraleSynchroService{
	@Inject
	private IMouvementService mouvementService;
	@Inject
	private IJourneeService journeeService;
	@Inject
	private IEmplacementService emplacementService;
	@Inject
	private IClientService clientService;
	@Inject
	private ICompteBancaireService banqueService;
	@Inject
	private IArticleDao articleDao;
	@Inject
	private IValTypeEnumService valEnumService;
	@Inject
	private IFamilleService familleService;
	
	/**
	 * Télécharger les établissement faisant partie de la centrale pour cet ets
	 */
	@Override
	@Transactional
	public void loadEtsCentrale() {
		String cloudUrl = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url")+"/update";
		String codeAuth = ParametrageService.getEtsCodeAuth();
		EntityManager em = getEntityManager();
		
		try {
			String retourCloud = FileUtilController.callURL(cloudUrl+"?mt=centraleEts&auth=" + codeAuth);
			// On désactive les existants
			List<CentraleEtsPersistant> listCentrales = findAll(CentraleEtsPersistant.class);
			for (CentraleEtsPersistant centraleEtsP : listCentrales) {
				centraleEtsP.setIs_disable(true);
				em.merge(centraleEtsP);
			}
			em.flush();
			
			String[] etsAuth = StringUtil.getArrayFromStringDelim(retourCloud, "|");
			for (String info : etsAuth) {
				String[] etsInfos = StringUtil.getArrayFromStringDelim(info, ";");
				
				String code_auth = etsInfos[0];
				String nom = etsInfos[1];
				String url = etsInfos[2];

				CentraleEtsPersistant csP = getOneByField(CentraleEtsPersistant.class, "code_auth", code_auth);
				csP = (csP == null ? new CentraleEtsPersistant() : csP);
				//
				csP.setCode_auth(code_auth);
				csP.setNom(nom);
				csP.setUrl(url);
				csP.setIs_disable(null);
				//
				em.merge(csP);
				
				// Ajouter les ets comme clients pour la vente centrale
				ClientPersistant localCli = getOneByField(ClientPersistant.class, "origine_auth", code_auth);
				if(localCli == null) {
					localCli = new ClientPersistant();
				}
				localCli.setOrigine_auth(code_auth);
				localCli.setType_client("PP");
				localCli.setNumero(clientService.generateNum());
				localCli.setNom(nom+" (Centrale)");
				
				setLocalInfos(localCli);
				//
				em.merge(localCli);
				
				// Charger les emplacements
				String requestData = null;
				try{
					requestData = FileUtilController.callURL(url+"/printCtrl?tp=centrale&opr=syncEmpl&auth="+code_auth);
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
				
				if(StringUtil.isEmpty(requestData)) {
					continue;
				}
				List<EmplacementPersistant> listEmplDist= ControllerUtil.getObjectFromJson(requestData, EmplacementPersistant.class);
				if(listEmplDist == null) {
					continue;
				}
				
				for (EmplacementPersistant emplP : listEmplDist) {
					EmplacementPersistant localEmpl = getOneByField(EmplacementPersistant.class, "origine_id", emplP.getId());
					emplP.setOrigine_id(emplP.getId());
					emplP.setOrigine_auth(emplP.getOpc_etablissement().getCode_authentification());
					
					if(localEmpl != null) {
						emplP.setId(localEmpl.getId());
					} else {
						emplP.setId(null);
					}
					emplP.setTitre(emplP.getTitre()+" ("+nom+")");
					emplP.setIs_externe(true);
					setLocalInfos(emplP);
					//
					em.merge(emplP);
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional
	public void addElementToSynchronise(String opr, String codeAuth, Long elementId, boolean isAnnul) {
		CentraleSynchroPersistant csP = (CentraleSynchroPersistant) getSingleResult(getQuery("from CentraleSynchroPersistant "
				+ "where type_opr=:opr and date_synchro is null and element_id=:elmntId")
				.setParameter("opr", opr)
				.setParameter("elmntId", elementId)
				);
		
		CentraleEtsPersistant etsB = getOneByField(CentraleEtsPersistant.class, "code_auth", codeAuth);
		
		csP = (csP == null ? new CentraleSynchroPersistant() : csP);
		csP.setType_opr(opr);// ART, FAM, FOURN, LCHOIX, MNU, TRANSF, VENTE
		csP.setOpc_centrale_ets(etsB);
		csP.setElement_id(elementId);
		csP.setIs_to_disable(isAnnul);// Suppression mvm
		csP.setCode_auth(codeAuth);
		//
		getEntityManager().merge(csP);
	}
	
	@Override
	@Transactional
	public void addElementsToSynchronise(String[] oprs, String[] etsIds, boolean isDisable) {
		EntityManager em = getEntityManager();
		
		for (String opr : oprs) {
			if(StringUtil.isEmpty(opr)) {
				continue;
			}
			for(String etsCentraleId : etsIds) {
				CentraleEtsPersistant ceP = findById(CentraleEtsPersistant.class, Long.valueOf(etsCentraleId));
				
				CentraleSynchroPersistant csP = (CentraleSynchroPersistant) getSingleResult(getQuery("from CentraleSynchroPersistant "
						+ "where type_opr=:opr and opc_centrale_ets.id=:etsId")
						.setParameter("opr", opr)
						.setParameter("etsId", Long.valueOf(etsCentraleId))
					);
				
				csP = (csP == null ? new CentraleSynchroPersistant() : csP);
				csP.setType_opr(opr);// ART, FAM, FOURN, LCHOIX, MNU, TRANSF, VENTE
				csP.setOpc_centrale_ets(ceP);
				csP.setIs_to_disable(isDisable);
				csP.setCode_auth(ceP.getCode_auth());
				//
				em.merge(csP);
			}
		}
	}
	
	// Demande de transfert reçue depuis un établissement
	@Override
	@Transactional
	public void synchroniseDemandeTransfert(String requestData, String codeAuthSrc) {
		EntityManager em = getEntityManager();
		DemandeTransfertPersistant demandeSynchP = ControllerUtil.getSingleObjectFromJson(requestData, DemandeTransfertPersistant.class);
		
		demandeSynchP.setOrigine_auth(codeAuthSrc);
		demandeSynchP.setOrigine_id(demandeSynchP.getId());
		demandeSynchP.setId(null);
		// Mettre les artcile en local
		for (DemandeTransfertArticlePersistant articleP : demandeSynchP.getList_article()) {
			ReflectUtil.setProperty(articleP, "id", null);
			// Cas heritage
			Class superClass = articleP.getClass();
			while(superClass.getDeclaredFields() != null && superClass.getDeclaredFields().length > 0){
				for (Field fieldSub : superClass.getDeclaredFields()) {
		    		if(fieldSub.getName().startsWith("opc_")){
		    			setOpcByCodeFunc(em, fieldSub, articleP);
		    		}
				}
	    		superClass = superClass.getSuperclass();
			}
			//
			setLocalInfos(articleP);
		}
		demandeSynchP.setStatut("ENREGISTREE");
		setLocalInfos(demandeSynchP);
		//
		em.merge(demandeSynchP);
	}
	
	private void setLocalInfos(Object dbEntitiy) {
		ReflectUtil.setProperty(dbEntitiy, ProjectConstante.ETABLISSEMENT, ContextAppli.getEtablissementBean());
		ReflectUtil.setProperty(dbEntitiy, ProjectConstante.SOCIETE, ContextAppli.getSocieteBean());
		ReflectUtil.setProperty(dbEntitiy, ProjectConstante.ABONNE, ContextAppli.getAbonneBean());
	}
	
	@Override
	@Transactional
	public String synchroniserOutAll(Long syncId) {
		boolean isRestau = SOFT_ENVS.restau.toString().equals(StrimUtil.getGlobalConfigPropertie("context.soft"));
		
		List<CentraleSynchroPersistant> listSynchro = null;
		if(syncId == null) {
			listSynchro = getQuery("from CentraleSynchroPersistant where date_synchro is null")
					.getResultList();
		} else {
			listSynchro = new ArrayList<>();
			listSynchro.add(findById(CentraleSynchroPersistant.class, syncId));
		}
		
		String ret = "";
		Date dateRef = DateUtil.addSubstractDate(new Date(), TIME_ENUM.YEAR, -3);
		//
		for (CentraleSynchroPersistant synchroDateMajP : listSynchro) {
			Date date_synchro = synchroDateMajP.getDate_synchro();
			if(date_synchro == null) {
				date_synchro = dateRef;
			}
			
			List listData = null;
			//
			if(synchroDateMajP.getType_opr().equals("ART")
					|| synchroDateMajP.getType_opr().equals("LCHOIX")
					|| synchroDateMajP.getType_opr().equals("MNU")) {//-------------------------------------
				listData = getQuery("from FamilleStockPersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "FAM_ST");
				
				if(isRestau) {
					listData = getQuery("from FamilleCuisinePersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
					ret += sendDataToRemoteEts(synchroDateMajP, listData, "FAM_CU");
				}
				listData = getQuery("from ArticlePersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "ART");
			}
			
			if(synchroDateMajP.getType_opr().equals("LCHOIX")) {//-----------------------------
				listData = getQuery("from ListChoixPersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "LCHOIX");
				
			} else if(synchroDateMajP.getType_opr().equals("MNU")) {//---------------------------------
				listData = getQuery("from MenuCompositionPersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "MNU");
				
			} else if(synchroDateMajP.getType_opr().equals("FOURN")) {//------------------------------
				listData = getQuery("from FamilleFournisseurPersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "FAM_FO");
				
				listData = getQuery("from FournisseurPersistant where date_maj>=:dtRef")
						.setParameter("dtRef", date_synchro)
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "FOURN");
			
			} else if(synchroDateMajP.getType_opr().equals("TRANSF")) {//-------------------------
				listData = getQuery("from MouvementPersistant where id=:mvmId")
						.setParameter("mvmId", synchroDateMajP.getElement_id())
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "TRANSF");
				
			} else if(synchroDateMajP.getType_opr().equals("VENTE")) {//-------------------------
				listData = getQuery("from MouvementPersistant where id=:mvmId")
						.setParameter("mvmId", synchroDateMajP.getElement_id())
						.getResultList();
				ret += sendDataToRemoteEts(synchroDateMajP, listData, "VENTE");
			}
		}
		
		return ret;
	}
	
	private String sendDataToRemoteEts(CentraleSynchroPersistant synchroDateMajP, List listData, String opr) {
		if(listData.size() == 0) {
			return "";
		}
		
		for(Object entP : listData) {
			String code_func = ReflectUtil.getStringPropertieValue(entP, "code_func");
			if(StringUtil.isEmpty(code_func)) {
				String curr_id = ReflectUtil.getStringPropertieValue(entP, "id");
				code_func = curr_id.toString()
						+entP.getClass().getSimpleName().substring(0, 2)
						+(""+System.currentTimeMillis()).substring(6);
				//
				ReflectUtil.setProperty(entP, "code_func",  code_func);
			}
		}
		
		EntityManager em = getEntityManager();
		String json = ControllerUtil.getJSonDataAnnotStartegy(listData);
		String url = synchroDateMajP.getOpc_centrale_ets().getUrl();	
		
		String retour = ControllerUtil.sendJsonPOST(json, url+"/printCtrl?tp=centrale"
				+ "&isDis="+synchroDateMajP.getIs_to_disable()+""
				+ "&opr="+opr
				+ "&auth="+synchroDateMajP.getCode_auth());
		
		String ret = "";
		if(retour.equals("OK")) {
			synchroDateMajP.setDate_synchro(new Date());
			em.merge(synchroDateMajP); 
			
			ret += synchroDateMajP.getOpc_centrale_ets().getNom()+"=>"+opr+" : <span style='color:green;'>OK</span><br>";
		} else {
			ret += synchroDateMajP.getOpc_centrale_ets().getNom()+"=>"+opr+" : <span style='color:red;'>KO</span><br>";
		}
		return ret;
	}

	/**
	 * Reçu de la centrale
	 */
	@Override
	@Transactional
	public String synchroniseInAll(EtablissementPersistant etsP, String typeOpr, String requestData, boolean isToDisable) {
		Class classTypeCl = null;
		String key = null;
		
		if(typeOpr.equals("ART")) {
			classTypeCl = ArticlePersistant.class;
			key = "code";
			if(isToDisable) {
				getQuery("update ArticlePersistant set is_disable=1").executeUpdate();
			}
			
		} else if(typeOpr.equals("FAM_FO")) {// On désactive d'office les famille pour éviter le problèle b_left et b_right
			classTypeCl = FamilleFournisseurPersistant.class;
			key = "code";
			getQuery("update FamilleFournisseurPersistant set is_disable=1").executeUpdate();
			
		} else if(typeOpr.equals("FAM_CU")) {
			classTypeCl = FamilleCuisinePersistant.class;
			key = "code";
			getQuery("update FamilleCuisinePersistant set is_disable=1").executeUpdate();

		} else if(typeOpr.equals("FAM_ST")) {
			classTypeCl = FamilleStockPersistant.class;
			key = "code";
			getQuery("update FamilleStockPersistant set is_disable=1").executeUpdate();

		} else if(typeOpr.equals("LCHOIX")) {
			classTypeCl = ListChoixPersistant.class;
			key = "code";
			if(isToDisable) {
				getQuery("update ListChoixPersistant set is_disable=1").executeUpdate();
			}

		} else if(typeOpr.equals("MNU")) {
			key = "code";
			classTypeCl = MenuCompositionPersistant.class;
			getQuery("update MenuCompositionPersistant set is_desactive=1").executeUpdate();

		} else if(typeOpr.equals("FOURN")) {
			key = "code";
			classTypeCl = FournisseurPersistant.class;
			if(isToDisable) {
				getQuery("update FournisseurPersistant set is_disable=1").executeUpdate();
			}

		} else if(typeOpr.equals("TRANSF")) {
			classTypeCl = MouvementPersistant.class;
		} else if(typeOpr.equals("VENTE")) {
			classTypeCl = MouvementPersistant.class;
		}
		
		List listDataSync = ControllerUtil.getObjectFromJson(requestData, classTypeCl);
		
		EntityManager em = getEntityManager();
		em.flush();
		
		for(Object dataRemote : listDataSync) {
			if(typeOpr.equals("TRANSF")) {
				synchroniserTransfert((MouvementPersistant) dataRemote, isToDisable);
			} else if(typeOpr.equals("VENTE")) {
				synchroniserVente((MouvementPersistant) dataRemote, isToDisable);
			} else {
				mergeEntity(em, etsP, dataRemote, key, isToDisable);
			}
		}
		
		return "OK";
	}
	private void mergeEntity(EntityManager em, EtablissementPersistant etsP, Object dataRemote, 
			String key,
			boolean isToDisable) {
		
		String valueKey = ReflectUtil.getStringPropertieValue(dataRemote, key);
		Object dataLocal = null;
		if(key != null) {
			List data = findByField(dataRemote.getClass(), key, valueKey);
			dataLocal = (data != null && data.size() > 0 ? data.get(0) : null); //getOneByField(dataRemote.getClass(), key, valueKey);
		}
		// Recherche par cle entité
		if(dataLocal == null) {
			String code_func = ReflectUtil.getStringPropertieValue(dataRemote, "code_func");
			dataLocal = getEntityByCodeFunc(em, dataRemote.getClass().getSimpleName(), code_func);	
		}
		
		Field[] fields = dataRemote.getClass().getDeclaredFields();
    	
    	if(dataLocal != null) {
    		Long idLocal = (Long) ReflectUtil.getObjectPropertieValue(dataLocal, "id"); 
    		ReflectUtil.setProperty(dataRemote, "id", idLocal);
    		// Remettre les listes
    		for (Field field : fields) {
    			if(field.getName().startsWith("list")){// Traiter le cas des listes avec cascade
    				field.setAccessible(true);
    				Object listData = null;
					try {
						listData = field.get(dataLocal);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
    				if(listData != null){
    					ReflectUtil.setProperty(dataRemote, field.getName(), listData); 
    				}
    				field.setAccessible(false); 
    			}
    		}
    	} else{
    		ReflectUtil.setProperty(dataRemote, "id", null);
    	}
    	// Les Opc
    	for (Field field : fields) {
    		if(field.getName().startsWith("opc_")){
    			setOpcByCodeFunc(em, field, dataRemote);
    		}
    	}
    	// Cas heritage
		Class superClass = dataRemote.getClass().getSuperclass();
		while(superClass.getDeclaredFields() != null && superClass.getDeclaredFields().length > 0){
			for (Field fieldSub : superClass.getDeclaredFields()) {
	    		if(fieldSub.getName().startsWith("opc_")){
	    			setOpcByCodeFunc(em, fieldSub, dataRemote);
	    		}
			}
    		superClass = superClass.getSuperclass();
		}
		
		if(etsP != null) {
			ReflectUtil.setProperty(dataRemote, ProjectConstante.ETABLISSEMENT, etsP);
			ReflectUtil.setProperty(dataRemote, ProjectConstante.SOCIETE, etsP.getOpc_societe());
			ReflectUtil.setProperty(dataRemote, ProjectConstante.ABONNE, etsP.getOpc_abonne());
		}
		
		em.merge(dataRemote);
	}
	
	private void setOpcByCodeFunc(EntityManager em, Field field, Object dataRemote){
		try {
			field.setAccessible(true);
			Object opcEntity = field.get(dataRemote);
			if(opcEntity == null){
				return;
			}
			
			String currCode = ReflectUtil.getStringPropertieValue(opcEntity, "code_func");
			Object opc_persistant = super.getEntityByCodeFunc(em, opcEntity.getClass().getSimpleName(), currCode);
			
			ReflectUtil.setProperty(dataRemote, field.getName(), opc_persistant);
			field.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	@Transactional
	private void synchroniserTransfert(MouvementPersistant mouvementP, boolean isAnnulation) {
		EntityManager em = getEntityManager();
		MouvementPersistant mvmPDb =  (MouvementPersistant) getSingleResult(getQuery("from MouvementPersistant where origine_id=:oriId")
				.setParameter("oriId", mouvementP.getId()));
		
		if(isAnnulation) {
			mouvementService.delete(mvmPDb.getId());
			return;
		}

		mouvementP.setOrigine_id(mouvementP.getId());
		if(mvmPDb == null) {
			if(StringUtil.isEmpty(mouvementP.getNum_bl())) {
				mouvementP.setNum_bl(mouvementService.generateNumBl("t"));
			}
			mouvementP.setId(null);
			mouvementP.setDate_creation(new Date());
			mouvementP.setDate_reception(new Date());
		} else {
			mouvementP.setId(mvmPDb.getId());
		}
		
		
		if(mouvementP.getOpc_demande() != null) {//si transfert suite à une demande
			DemandeTransfertPersistant demandeP = (DemandeTransfertPersistant) findById(DemandeTransfertPersistant.class, mouvementP.getOpc_demande().getOrigine_id());
			mouvementP.setOpc_demande(demandeP);
		}
		
		EmplacementPersistant emplacement_dst = (EmplacementPersistant) findById(EmplacementPersistant.class, mouvementP.getOpc_destination().getOrigine_id());
		mouvementP.setOpc_destination(emplacement_dst);
		
		//si emplacement de la centrale n'existe pas, on le crée
		EmplacementPersistant emplacement_src =  (EmplacementPersistant) getSingleResult(getQuery("from EmplacementPersistant "
				+ "where origine_id is not null and origine_id=:org")
				.setParameter("org", mouvementP.getOpc_emplacement().getId()));
		if(emplacement_src == null) {
			emplacement_src = mouvementP.getOpc_emplacement();
			emplacement_src.setTitre(emplacement_src.getTitre() + " (centrale)");
			emplacement_src.setOrigine_id(mouvementP.getOpc_emplacement().getId());
			emplacement_src.setId(null);
			//
			emplacement_src = getEntityManager().merge(emplacement_src);
			getEntityManager().flush();
		}
		
		mouvementP.setOpc_emplacement(emplacement_src);

		//mettre à jour la liste des MouvementArticlePersistant du mouvement
		List<MouvementArticlePersistant> new_list_article = new ArrayList<>();
		for (MouvementArticlePersistant mvmArticle : mouvementP.getList_article()) {
			//créer l'article s'il n'existe pas, si non le modifier
			mergeEntity(em, mvmArticle.getOpc_etablissement(), mvmArticle.getOpc_article(), null, isAnnulation);
			
			//affecter les nouveux ids aux articles
			List<ArticlePersistant> listArticleP = getQuery("from ArticlePersistant where code=:code and is_stock is not null and is_stock=1")
					.setParameter("code", mvmArticle.getOpc_article().getCode())
					.getResultList();
			
			ArticlePersistant articleP = listArticleP.size() > 0 ? listArticleP.get(0) : null;
			
			mvmArticle.setId(null);
			mvmArticle.setOpc_article(articleP);
			new_list_article.add(mvmArticle);
		}
		
		mouvementP.getList_article().clear();
		mouvementP.setList_article(new_list_article);
		
		MouvementBean mouvementBean = (MouvementBean) ServiceUtil.persistantToBean(MouvementBean.class, mouvementP);
		mouvementBean.setType_transfert("A");
		mouvementService.create(mouvementBean);
		
		if(mouvementP.getOpc_demande() != null) {//mettre à jour le statut de la demande
			DemandeTransfertPersistant demandeP = (DemandeTransfertPersistant) findById(DemandeTransfertPersistant.class, mouvementP.getOpc_demande().getId());
			demandeP.setStatut("TRAITEE");
			demandeP.setDate_transfert(new Date());
			getEntityManager().merge(demandeP);
		}
	}
	
	@Transactional
	private void synchroniserVente(MouvementPersistant mouvementP, boolean isAnnulation) {
		// Vente devient achat ici
		EntityManager em = getEntityManager();
		MouvementPersistant mvmPDb =  (MouvementPersistant) getSingleResult(getQuery("from MouvementPersistant where origine_id=:oriId")
				.setParameter("oriId", mouvementP.getId()));
		
		if(isAnnulation) {
			mouvementService.delete(mvmPDb.getId());
			return;
		}

		mouvementP.setOrigine_id(mouvementP.getId());
		
		if(mvmPDb == null) {
			mouvementP.setNum_bl(mouvementService.generateNumBl("a"));
			mouvementP.setId(null);
			mouvementP.setDate_creation(new Date());
			mouvementP.setDate_reception(new Date());
		} else {
			mouvementP.setId(mvmPDb.getId());
		}
		
		mouvementP.setType_mvmnt(ContextAppli.TYPE_MOUVEMENT_ENUM.a.toString());// Type achat
		mouvementP.setMontant_ht(mouvementP.getMontant_ttc());
		mouvementP.setMontant_tva(BigDecimalUtil.ZERO);
		
		// On va créer la centrale comme fournisseur
		FournisseurPersistant fournDb =  (FournisseurPersistant) getSingleResult(getQuery("from FournisseurPersistant where code=:code")
				.setParameter("code", "CENTRALE"));
		if(fournDb == null) {
			List listFamille = familleService.getListeFamille("FO", true, false);
			
			fournDb = new FournisseurPersistant();
			fournDb.setNom("CENTRALE");	
			fournDb.setCode("CENTRALE");
			fournDb.setLibelle("Etablissement CENTRALE");
			fournDb.setOpc_famille((FamilleFournisseurPersistant) listFamille.get(0));
			//
			em.merge(fournDb);
		}
		
		mouvementP.setOpc_fournisseur(fournDb);
		
		EmplacementPersistant emplacement_dst = emplacementService.getListEmplacementActifs().get(0);
		mouvementP.setOpc_destination(emplacement_dst);
		
		ValTypeEnumBean valTypeEnumExo = null;
		List<ValTypeEnumBean> listVal = valEnumService.getListValeursByType(ModelConstante.ENUM_TVA);
		for (ValTypeEnumBean valTypeEnum : listVal) {
			if("0".equals(valTypeEnum.getCode())) {
				valTypeEnumExo = valTypeEnum;
				break;
			}
		}
		//mettre à jour la liste des MouvementArticlePersistant du mouvement
		List<MouvementArticlePersistant> new_list_article = new ArrayList<>();
		for (MouvementArticlePersistant mvmArticle : mouvementP.getList_article()) {
			mvmArticle.setOpc_tva_enum(valTypeEnumExo);
			mvmArticle.setPrix_ht(mvmArticle.getPrix_vente());
			mvmArticle.setPrix_ht_total(mvmArticle.getPrix_ttc_total());
			mvmArticle.setPrix_ttc(mvmArticle.getPrix_vente());
			mvmArticle.setPrix_ttc_total(mvmArticle.getPrix_ttc_total());
			mvmArticle.setPrix_vente(null);
			mvmArticle.setPrix_vente_ht(null);
			//créer l'article s'il n'existe pas, si non le modifier
			mergeEntity(em, mvmArticle.getOpc_etablissement(), mvmArticle.getOpc_article(), null, isAnnulation);
			
			//affecter les nouveux ids aux articles
			List<ArticlePersistant> listArticleP = getQuery("from ArticlePersistant where code=:code and is_stock is not null and is_stock=1")
					.setParameter("code", mvmArticle.getOpc_article().getCode())
					.getResultList();
			
			ArticlePersistant articleP = listArticleP.size() > 0 ? listArticleP.get(0) : null;
			
			mvmArticle.setId(null);
			mvmArticle.setOpc_article(articleP);
			new_list_article.add(mvmArticle);
		}
		
		mouvementP.getList_article().clear();
		mouvementP.setList_article(new_list_article);
		
		MouvementBean mouvementBean = (MouvementBean) ServiceUtil.persistantToBean(MouvementBean.class, mouvementP);
		mouvementService.create(mouvementBean);		
	}

	private EtatFinanceBean getEtatFinanceBean(Date dateDebut, Date dateFin){
		List<EtatFinancePaiementPersistant> listP = new ArrayList<EtatFinancePaiementPersistant>();
		EtatFinanceBean etatFinanceBean = new EtatFinanceBean();
		etatFinanceBean.setList_detail(new ArrayList<>());
		etatFinanceBean.setList_paiement(listP);
		
		/*********************************************** VENTE CAISSE ****************************************************/
		String date = DateUtil.dateToString(dateDebut, "dd/MM/yyyy");
		JourneePersistant jr = journeeService.getJourneeByDate(DateUtil.stringToDate(date));
		
		if(jr.getStatut_journee().equals("O")) {
			journeeService.setDataJourneeFromView(jr);
			
			etatFinanceBean.setMtt_vente_caisse(jr.getMtt_total_net());
			etatFinanceBean.setMtt_vente_caisse_cloture(BigDecimalUtil.substract(jr.getMtt_cloture_caissier(), jr.getMtt_ouverture()));
			etatFinanceBean.setNbr_livraison(jr.getNbr_livraison());
			etatFinanceBean.setMtt_livraison(BigDecimalUtil.multiply(BigDecimalUtil.get(jr.getNbr_livraison()!=null ? jr.getNbr_livraison() : 0), jr.getTarif_livraison()));
			etatFinanceBean.setMtt_livraison_part(BigDecimalUtil.multiply(BigDecimalUtil.get(jr.getNbr_livraison()!=null ? jr.getNbr_livraison() : 0), jr.getTarif_livraison_part()));
		} else {
			// Total net et clôture des ventes caisse --------------------------------------------------------
			Object[] mtt_vente_caisse = (Object[]) mouvementService.getSingleResult(mouvementService.getEntityManager().createNativeQuery(
					"select * from ( "
					+ "(select sum(mtt_total_net) as mtt_net, "
						+ " sum(mtt_cloture_caissier-mtt_ouverture) as mtt_cloture "
					+ "from journee where date_journee>=:dateDebut and date_journee<=:dateFin) jr1, "
					+ "(select count(0) as nbrLiv, "
						+ "sum(nbr_livraison*tarif_livraison) as mttLiv,"
					+ "sum(nbr_livraison*tarif_livraison_part) as mttLivSoc "
					+ "from journee where date_journee>=:dateDebut and date_journee<=:dateFin and nbr_livraison is not null and nbr_livraison!=0) jr2"
					+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
			
			etatFinanceBean.setMtt_vente_caisse((BigDecimal) mtt_vente_caisse[0]);
			etatFinanceBean.setMtt_vente_caisse_cloture((BigDecimal) mtt_vente_caisse[1]);
			etatFinanceBean.setNbr_livraison(((BigInteger) mtt_vente_caisse[2]).intValue());
			etatFinanceBean.setMtt_livraison((BigDecimal) mtt_vente_caisse[3]);
			etatFinanceBean.setMtt_livraison_part((BigDecimal) mtt_vente_caisse[4]);
		}
		
		/*********************************************** DEPENSE ****************************************************/
		// Dépenses, CHÈQUES depenses de ce mois non encaissé, encaissé ce mois --------------------------------------------------------------
		BigDecimal totalDep = null;
		
		Object[] mtt_depense = (Object[]) mouvementService.getSingleResult(mouvementService.getEntityManager().createNativeQuery(
				"select * from ( "
				+ "(select sum(montant) as mt1 from assurance mvm where mvm.date_paiement>=:dateDebut and mvm.date_paiement<=:dateFin) mv1, "
				+ "(select sum(montant) as mt2 from incident mvm where mvm.date_incident>=:dateDebut and mvm.date_incident<=:dateFin) mv2, "
				+ "(select sum(montant_total) as mt3 from vidange mvm where mvm.date_passage>=:dateDebut and mvm.date_passage<=:dateFin) mv3, "
				+ "(select sum(montant) as mt4 from vignette mvm where mvm.date_paiement>=:dateDebut and mvm.date_paiement<=:dateFin) mv4, "
				+ "(select sum(montant) as mt5 from visite_technique mvm where mvm.date_paiement>=:dateDebut and mvm.date_paiement<=:dateFin) mv5, "
				+ "(select sum(mtt_valide) as mt6 from carburant mvm where mvm.date_passage>=:dateDebut and mvm.date_passage<=:dateFin) mv6) "
			).setParameter("dateDebut", dateDebut)
			.setParameter("dateFin", dateFin));
		
		totalDep = BigDecimalUtil.add(totalDep, (BigDecimal)mtt_depense[0], (BigDecimal)mtt_depense[1], (BigDecimal)mtt_depense[2], 
				(BigDecimal)mtt_depense[3], (BigDecimal)mtt_depense[4], (BigDecimal)mtt_depense[5]);
		
		/*********************************************** RECETTE ****************************************************/
		// Recettes --------------------------------------------------------
		Object[] mtt_recette = (Object[]) mouvementService.getSingleResult(mouvementService.getEntityManager().createNativeQuery(
				"select * from ( "
				+ "(select sum(montant) as mta1 from charge_divers mvm where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='C') mv1, "
				+ "(select sum(montant) as mta2 from charge_divers mvm where mvm.date_mouvement>=:dateDebut and mvm.date_mouvement<=:dateFin and mvm.sens='D') mvD1) ")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
		
		etatFinanceBean.setMtt_recette_divers((BigDecimal) mtt_recette[0]);
		etatFinanceBean.setMtt_depense_divers(BigDecimalUtil.add(totalDep, (BigDecimal)mtt_recette[1]));// Ajout des autres depenses

		/*********************************************** VENTE HORS CAISSE + AVOIR ****************************************************/
		// Vente hors caisse, Les chèques vente de ce mois non encaissé,  Les chèques encaissé ce mois des ventes --------------------------------------------------------
		mtt_recette= (Object[]) mouvementService.getSingleResult(mouvementService.getEntityManager().createNativeQuery(
				"select * from ("
				+ "(select sum(montant_ttc) as mta1 from mouvement mvm where date_mouvement>=:dateDebut and date_mouvement<=:dateFin and mvm.type_mvmnt='v') mv1,"
				+ "(select sum(montant_ttc) as mta2 from mouvement mvm where date_mouvement>=:dateDebut and date_mouvement<=:dateFin and mvm.type_mvmnt='av') mvAV1,"
				+ "(select sum(montant_ttc-IFNULL(montant_ttc_rem,0)) as mt1 from mouvement mvm where date_mouvement>=:dateDebut and date_mouvement<=:dateFin and mvm.type_mvmnt='a') mvA1 "
				+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
		
		etatFinanceBean.setMtt_vente_hors_caisse((BigDecimal)mtt_recette[0]);
		etatFinanceBean.setMtt_avoir((BigDecimal)mtt_recette[1]);
		etatFinanceBean.setMtt_achat((BigDecimal)mtt_recette[2]);
				
		/*********************************************** SALAIRE ****************************************************/
		Object[] mtt_salaire = (Object[]) mouvementService.getSingleResult(mouvementService.getEntityManager().createNativeQuery(
				"select * from ("
				+ "(select sum(montant_net) as mt1 from salaire sal where "
				+ "date_paiement>=:dateDebut and date_paiement<=:dateFin) sal1 "
				+ ")")
				.setParameter("dateDebut", dateDebut)
				.setParameter("dateFin", dateFin));
		
		etatFinanceBean.setMtt_salaire(mtt_salaire!=null ? (BigDecimal)mtt_salaire[0] : BigDecimalUtil.get(0));

		/*********************************************** STOCK ****************************************************/
		Date finMoisEnCours = dateFin;
		//
		List<Object[]> listMttEmplacementEnCours = articleDao.getMontantStock(finMoisEnCours);
		
		for (Object[] objectsP : listMttEmplacementEnCours) {
			if(objectsP[0] == null){
				continue;
			}
			EtatFinanceDetailPersistant etatP = new EtatFinanceDetailPersistant();
			etatP.setLibelle((String) objectsP[1]);
			etatP.setMtt_etat_actuel(BigDecimalUtil.get(""+objectsP[2]));
			etatP.setType("EMPL");
			etatFinanceBean.getList_detail().add(etatP);
		}
		/*********************************************** COMPTES BANCAIRES ****************************************************/
		List<CompteBancaireBean> listCompteBancaire = banqueService.findAll(Order.asc("libelle"));
		
		for (CompteBancairePersistant compteBanque: listCompteBancaire) {
			EtatFinanceDetailPersistant etatP = new EtatFinanceDetailPersistant();
			etatP.setType("BANQ");
			etatP.setLibelle(compteBanque.getLibelle());
			etatP.setMtt_etat_actuel(banqueService.getSoldeCompte(compteBanque.getId(), finMoisEnCours));
			//
			etatFinanceBean.getList_detail().add(etatP);
		}

		return etatFinanceBean;
	}
	
	@Override
	@Transactional
	/**
	 * Envoyer la demande à la centrale
	 * @param element
	 * @param restauP
	 */
	public String sendDemandeTransfertToCentrale(Long demandeId) {		
		DemandeTransfertPersistant transert = findById(DemandeTransfertPersistant.class, demandeId);
		String json = ControllerUtil.getJSonDataAnnotStartegy(transert);
		
		String codeAuth = ParametrageService.getEtsCodeAuth();
		String centraleUrl = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url")+"/update?mt=centraleUrl&auth="+codeAuth;
		try {
			centraleUrl = FileUtilController.callURL(centraleUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String retour = ControllerUtil.sendJsonPOST(json, centraleUrl+"/printCtrl?tp=centrale&auth="+codeAuth+"&opr=DEMTRANS");
		
		return retour;
	}
	
	/**
	 * Méthode instance centrale 
	 * @param dmdTransP
	 * @return
	 */
	@Override
	@Transactional
	public boolean annulerDemandeTransfert(Long dmdTransId, String codeAuth) {
		List<DemandeTransfertPersistant> demandesP = getQuery("from DemandeTransfertPersistant where origine_id=:origine"
				+ " and origine_auth=:codeAuth")
				.setParameter("origine", dmdTransId)
				.setParameter("codeAuth", codeAuth)
				.getResultList();
		
		DemandeTransfertPersistant demandeP = (demandesP.size() > 0 ? demandesP.get(0) : null);
		if(demandeP.getStatut().equals("TRAITEE")) {
			return false;
		} else {
			demandeP.setStatut("ANNULEE");
			DemandeTransfertPersistant dmdTrans = getEntityManager().merge(demandeP);
			if(dmdTrans != null) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public List<JourneePersistant> getJourneeInfosForCentrale(CentraleEtsPersistant etsP, Date dateDebut, Date dateFin){
		List<JourneePersistant> listJ = null;
		try {
			String retourCloud = FileUtilController.callURL(etsP.getUrl()+"/printCtrl?tp=centrale&opr=syncJournee&dts="
					+DateUtil.dateToString(dateDebut, "dd-MM-yyyy")+"&dte="+DateUtil.dateToString(dateFin, "dd-MM-yyyy")+"&auth=" + etsP.getCode_auth());
			if(!"KO".equals(retourCloud) && StringUtil.isNotEmpty(retourCloud) && !"[]".equals(retourCloud)) {
				listJ = ControllerUtil.getObjectFromJson(retourCloud, JourneePersistant.class);
			}
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		
		return listJ;
	}
	
	@Override
	public EtatFinanceBean getEtatForCentrale(CentraleEtsPersistant etsP, Date dateDebut, Date dateFin) {
		List<EtatFinanceBean> etatFB = null;
		try {
			String retourCloud = FileUtilController.callURL(etsP.getUrl()+"/printCtrl?tp=centrale&opr=syncEtat&dts="
						+DateUtil.dateToString(dateDebut, "dd-MM-yyyy")+"&dte="+DateUtil.dateToString(dateFin, "dd-MM-yyyy")+"&auth=" + etsP.getCode_auth());
			if(!"KO".equals(retourCloud) && StringUtil.isNotEmpty(retourCloud) && !"[]".equals(retourCloud)) {
				etatFB = ControllerUtil.getObjectFromJson(retourCloud, EtatFinanceBean.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (etatFB != null && etatFB.size() > 0) ? etatFB.get(0) : null;
	}
	
	@Override
	public List<CentraleEtsPersistant> findActifsCentrale() {
		return getQuery("from CentraleEtsPersistant where (is_disable is null or is_disable=0) "
				+ "order by nom")
				.getResultList();
	}

	@Override
	public String sendAnnulationDemandeTransfertToCentrale(Long demandeId) {
		String retour = null;
		String codeAuth = ParametrageService.getEtsCodeAuth();
		String centraleUrl = StrimUtil.getGlobalConfigPropertie("caisse.cloud.url")+"/update?mt=centraleUrl&auth="+codeAuth;
		try {
			centraleUrl = FileUtilController.callURL(centraleUrl);
			retour = FileUtilController.callURL(centraleUrl+"/printCtrl?tp=centrale&auth="+codeAuth+"&opr=ANNTRANS&demid="+demandeId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return retour;
	}
}
