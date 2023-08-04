package appli.model.domaine.administration.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.ParametrageRightsConstantes;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_MOUVEMENT_ENUM;
import appli.controller.util_ctrl.ZipUtil4;
import appli.model.domaine.administration.persistant.EtatFinancePersistant;
import appli.model.domaine.administration.persistant.ParametragePersistant;
import appli.model.domaine.administration.service.IEtablissementService;
import appli.model.domaine.administration.service.IParametrageService;
import appli.model.domaine.administration.service.IUserService;
import appli.model.domaine.administration.validator.ParametrageValidator;
import appli.model.domaine.stock.persistant.InventairePersistant;
import appli.model.domaine.vente.persistant.CaissePersistant;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.AbonnePersistant;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.beanContext.SocietePersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.EncryptionEtsUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;
import framework.model.util.audit.ReplicationGenerationEventListener;
import framework.model.util.synchro.ISynchroniseService;

@WorkModelClassValidator(validator = ParametrageValidator.class)
@Named
public class ParametrageService extends	GenericJpaService<ParametragePersistant, Long> implements IParametrageService {

	private Logger LOGGER = Logger.getLogger(ParametrageService.class);
	@Inject
	private ISynchroniseService synchroService;
	
	@Override
	@Transactional
	public void updateParams(Map<String, Object> params, Long terminalId) {
		EntityManager em = getEntityManager();
		for (String code : params.keySet()) {
			ParametragePersistant parametragePersistant = getParameterByCode(code, terminalId);
			if(parametragePersistant == null){
				continue;
			}
			
			if(StringUtil.isNotEmpty(params.get(code))){
				parametragePersistant.setValeur(""+params.get(code));
			} else {
				parametragePersistant.setValeur(null);
			}
			
			if(terminalId != null){
				parametragePersistant.setOpc_terminal(findById(CaissePersistant.class, terminalId));
			}
			//
			em.merge(parametragePersistant);
		}
		
		ParametrageRightsConstantes.loadAllMapGlobParams(true);
		ParametrageRightsConstantes.loadAllMapSpecParams(true);
	}
	
	@Override
	@Transactional
	public void updateParams(Map<String, Object> params, String titrePublicite, String msgPublicite, Long terminalId) {
		// Params
		updateParams(params, terminalId);
		//
		EtablissementPersistant etsP = (EtablissementPersistant) findById(EtablissementPersistant.class, ContextGloabalAppli.getEtablissementBean().getId());
		etsP.setMsg_publicite(msgPublicite);
		etsP.setTitre_publicite(titrePublicite);
		//
		etsP = getEntityManager().merge(etsP);
		MessageService.getGlobalMap().put("GLOBAL_ETABLISSEMENT", etsP);
	}

	private ParametragePersistant getParameterLocalByCode(String code) {
		for(ParametragePersistant param : ParametrageRightsConstantes.GENERAL_GLOB_PARAMS){
			if(param.getCode().equals(code)){
				return param;
			}
		}
		for(ParametragePersistant param : ParametrageRightsConstantes.CAISSE_GLOB_PARAMS){
			if(param.getCode().equals(code)){
				return param;
			}
		}
		for(ParametragePersistant param : ParametrageRightsConstantes.CUISINE_GLOB_PARAMS){
			if(param.getCode().equals(code)){
				return param;
			}
		}
		for(ParametragePersistant param : ParametrageRightsConstantes.CAISSE_SPEC_PARAMS){
			if(param.getCode().equals(code)){
				return param;
			}
		}
		for(ParametragePersistant param : ParametrageRightsConstantes.BALANCE_SPEC_PARAMS){
			if(param.getCode().equals(code)){
				return param;
			}
		}
		for(ParametragePersistant param : ParametrageRightsConstantes.CUISINE_SPEC_PARAMS){
			if(param.getCode().equals(code)){
				return param;
			}
		}
		return null;
	}
	private List<ParametragePersistant> getParameterLocalByGroupe(String groupe) {
		List[] data = {
				ParametrageRightsConstantes.GENERAL_GLOB_PARAMS, 
				
				ParametrageRightsConstantes.CAISSE_GLOB_PARAMS, 
				ParametrageRightsConstantes.CUISINE_GLOB_PARAMS,
				ParametrageRightsConstantes.CAISSE_SPEC_PARAMS, 
				ParametrageRightsConstantes.CUISINE_SPEC_PARAMS, 
				ParametrageRightsConstantes.BALANCE_SPEC_PARAMS};
		
		List<ParametragePersistant> params = new ArrayList<>();
		for (List list : data) {
			List<ParametragePersistant> listD = list;
			for(ParametragePersistant param : listD){
				if(param.getGroupe().equals(groupe)){
					params.add(param);
				}
			}
		}
		return params;
	}
//	@Override
//	public String getParameterbyCode(PARAM_APPLI_ENUM code) {
//		ParametragePersistant paramP = (ParametragePersistant) getSingleResult(getQuery("from ParametragePersistant "
//				+ "where code=:code")
//				.setParameter("code", code.toString()));
//		
//		if(paramP == null){
//			return getParameterLocalByCode(code.toString()).getValeur();
//		} else{
//			return paramP.getValeur();
//		}
//	}
	@Override
	@Transactional
	public ParametragePersistant getParameterByCode(String code) {
		List<ParametragePersistant> paramPs = getQuery("from ParametragePersistant "
				+ "where code=:code order by id desc")
				.setParameter("code", code)
				.getResultList();
		
		ParametragePersistant paramP = null;
		if(paramPs.size() > 1){
			paramP = paramPs.get(0);
//			EntityManager em = getEntityManager();
//			em.remove(paramPs.get(1));
//			em.flush();
		} else{
			paramP = paramPs.size() > 0 ? paramPs.get(0) : null;
		}
		
		if(paramP == null){
			return getParameterLocalByCode(code);
		} else{
			return paramP;
		}
	}
	@Override
	@Transactional
	public ParametragePersistant getParameterByCode(String code, Long caisseId) {
		ParametragePersistant paramP = null;
		if(caisseId == null){
			paramP = getParameterByCode(code);
		} else{
			List<ParametragePersistant> listP = getQuery("from ParametragePersistant where code=:code and "
				+ "opc_terminal.id is not null and opc_terminal.id=:caisseId")
					.setParameter("code", code)
					.setParameter("caisseId", caisseId)
					.getResultList();
			if(listP.size() > 1){
				paramP = listP.get(0);
				EntityManager em = getEntityManager();
				em.remove(listP.get(1));
				em.flush();
			} else{
				paramP = listP.size() > 0 ? listP.get(0) : null;
			}
		}
		//
		if(paramP == null){
			paramP = getParameterLocalByCode(code);
		}
		return paramP;
	}
	
	@Override
	public List<ParametragePersistant> getParameterByGroupe(String groupe) {
		getEntityManager().clear();
		
		List<ParametragePersistant> dbParams = getQuery("from ParametragePersistant "
				+ "where groupe=:groupe order by ordre, libelle")
				.setParameter("groupe", groupe)
				.getResultList();
		
		List<ParametragePersistant> finalList = new ArrayList<>();
		
		List<ParametragePersistant> localParams = getParameterLocalByGroupe(groupe);
		for (ParametragePersistant parametragelocalP : localParams) {
			for (ParametragePersistant parametrageDbP : dbParams) {
				if(parametrageDbP.getCode().equals(parametragelocalP.getCode())){
					parametragelocalP = parametrageDbP;
					break;
				}
			}
			if(!finalList.contains(parametragelocalP)) {
				finalList.add(parametragelocalP);
			}
		}
		return finalList;
	}
	
	private void execReq(EntityManager em, String req){
		
		try {
			em.getTransaction().begin(); 
			em.createNativeQuery(req).executeUpdate();
			em.getTransaction().commit();
		} catch(Exception e){
			if(em.getTransaction().isActive()){
				em.getTransaction().rollback();
			}
			e.printStackTrace();
		}
	}

	
	@PersistenceUnit
	EntityManagerFactory emf;
	
	@Override
	public void executerScriptView(){
		EntityManager entityManager = emf.createEntityManager();
		BufferedReader br = null;
		FileReader fr = null;
		List<String> listReq = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		String line;
		
		try {
			// Ajouter les views
			File scriptFile = new File(getClass().getResource("/appli/conf/script_view.sql").getFile());
			fr = null;
			//
			sb = new StringBuilder();
			line = null;
			br = new BufferedReader(new FileReader(scriptFile));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0 || line.trim().startsWith("--")) {
					continue;	
				}
				sb.append(line); 
				
				if(line.indexOf(";") != -1){
					if(sb.toString().trim().length() > 0){
						listReq.add(sb.toString());
					}
					sb = new StringBuilder();
				}
			}
			// End	
						
			for (String req : listReq) {
				entityManager.getTransaction().begin();
				try{
					entityManager.createNativeQuery(req).executeUpdate();
					entityManager.getTransaction().commit();
				} catch(Exception e){
					entityManager.getTransaction().rollback();
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
		} finally {
			entityManager.close();
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				LOGGER.error(ex);
			}
		}
	}
	
	private void executerFirstScript(){
		EntityManager entityManager = emf.createEntityManager();
		File scriptFile = new File(getClass().getResource("/appli/conf/script_init.sql").getFile());
		BufferedReader br = null;
		FileReader fr = null;
		//
		try {
			List<String> listReq = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			String line;
			
			String codeAuth = StrimUtil.getGlobalConfigPropertieIgnoreErreur("caisse.code.auth");
			codeAuth = (codeAuth == null ? "xxCODE_AUTHxx" : codeAuth);
			String ets = StrimUtil.getGlobalConfigPropertieIgnoreErreur("ets.name");
			ets = (StringUtil.isEmpty(ets) ? "Etablissement" : ets);
			
			EncryptionEtsUtil encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
			
			String pwAdmin = StrimUtil.getGlobalConfigPropertieIgnoreErreur("remote.pw");
			if(StringUtil.isEmpty(pwAdmin)){
				pwAdmin = "SuperAdminAll?";
			}
			
			String pwAdminCrypt = encryptionUtil.encrypt(pwAdmin);
			String passCrypt = encryptionUtil.encrypt("01");
			String codeAuthCrypt = encryptionUtil.encrypt(codeAuth);
			
			br = new BufferedReader(new FileReader(scriptFile));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0 || line.trim().startsWith("--")) {
					continue;	
				}
				
				line = line.replaceAll("xxCODE_AUTHxx", codeAuthCrypt);
				line = line.replaceAll("xxNAMExx",  ets);
				line = line.replaceAll("xxUSER_PWxx", passCrypt);
				line = line.replaceAll("xxREMOTE_PWxx", pwAdminCrypt);
				
				sb.append(line); 
				
				if(line.indexOf(";") != -1){
					if(sb.toString().trim().length() > 0){
						listReq.add(sb.toString());
					}
					sb = new StringBuilder();
				}
			}
			
			// Ajouter les views
			scriptFile = new File(getClass().getResource("/appli/conf/script_view.sql").getFile());
			fr = null;
			//
			sb = new StringBuilder();
			line = null;
			br = new BufferedReader(new FileReader(scriptFile));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0 || line.trim().startsWith("--")) {
					continue;	
				}
				sb.append(line); 
				
				if(line.indexOf(";") != -1){
					if(sb.toString().trim().length() > 0){
						listReq.add(sb.toString());
					}
					sb = new StringBuilder();
				}
			}
			// End	
						
			for (String req : listReq) {
				entityManager.getTransaction().begin();
				try{
					entityManager.createNativeQuery(req).executeUpdate();
					entityManager.getTransaction().commit();
				} catch(Exception e){
					entityManager.getTransaction().rollback();
					e.printStackTrace();
				}
			}
			
			// set a password on the root account
			String user = StrimUtil.getGlobalConfigPropertie("db.user");
			String pw = StrimUtil.getGlobalConfigPropertie("db.pw");
			String dbName = StrimUtil.getGlobalConfigPropertie("db.name");
			
			execReq(entityManager, "FLUSH PRIVILEGES");
			execReq(entityManager, "default-storage-engine=InnoDB");
			
			// Pw pour le root
			execReq(entityManager, "update mysql.user set password=password('"+pw+"') where user='root';");
			execReq(entityManager, "FLUSH PRIVILEGES");
			//
			if(!user.equals("root")){
				execReq(entityManager, "CREATE USER IF NOT EXISTS '"+user+"'@'localhost' IDENTIFIED BY '"+pw+"'");
				execReq(entityManager, "FLUSH PRIVILEGES");
//				execReq(entityManager, "GRANT ALL ON " + dbName + ".* to '" + user + "'@'%' IDENTIFIED BY '" + pw + "'");
				execReq(entityManager, "GRANT ALL ON " + dbName + ".* to '" + user + "'@'localhost' IDENTIFIED BY '" + pw + "'");
				execReq(entityManager, "FLUSH PRIVILEGES");
			}
			
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
		} finally {
			entityManager.close();
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				LOGGER.error(ex);
			}
		}
	}
	
//	@Transactional
//	@Override
//	public AbonnePersistant addAbonneCloud(String funcId){
//		EntityManager em = getEntityManager();
//		
//		AbonnePersistant abnP = (AbonnePersistant) getSingleResult(em.createQuery("from AbonnePersistant where code_func=:funcId")
//				.setParameter("funcId", funcId));
//		
//		if(abnP == null){
//			abnP = new AbonnePersistant();
//			abnP.setCode_func(funcId);
//			abnP.setRaison_sociale("Abonnée");
//			abnP = em.merge(abnP);
//		}
//		
//		return abnP;
//	}
	
	@Transactional
	@Override
	public void addDataRequired(){
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		
//		String abonneKey = StrimUtil.getGlobalConfigPropertieIgnoreErreur("abn.key");
		List<EtablissementPersistant> listEts = em.createQuery("from EtablissementPersistant")
				.getResultList();
		EtablissementPersistant currEts = listEts.get(0);
		String codeAuth = ParametrageService.getDycCodeAuth(currEts, currEts.getCode_authentification());
		
		AbonnePersistant abnP = null;
		List<AbonnePersistant> listAbonne = em.createQuery("from AbonnePersistant")
				.getResultList();
		boolean isAbnAdded = false;
		//------------------------ Initialisation abonnée et société --------------
		if(listAbonne == null || listAbonne.size() == 0){
			abnP = new AbonnePersistant();
			abnP.setCode_func("ABN_"+codeAuth);
			abnP.setRaison_sociale("Abonné");
			abnP = em.merge(abnP);
			em.flush();
			
			MessageService.getGlobalMap().put("GLOBAL_ABONNE", ReflectUtil.cloneBean(abnP));
			isAbnAdded = true;
		} else{
			abnP = listAbonne.get(0);
		}

		
		List<SocietePersistant> listSociete = em.createQuery("from SocietePersistant").getResultList();
		if(listSociete == null || listSociete.size() == 0){
			SocietePersistant socP = new SocietePersistant();
			socP.setRaison_sociale("Société");
			socP.setOpc_abonne(abnP);
			socP.setCode_func("SOC_"+codeAuth);
			//
			socP = em.merge(socP);
			em.flush();
			listSociete.add(socP);
			
			MessageService.getGlobalMap().put("GLOBAL_SOCIETE", socP);
			
			for (EtablissementPersistant etsP : listEts) {
				etsP.setOpc_societe(socP);
				etsP.setOpc_abonne(abnP);
				etsP.setCode_func("ETS_"+codeAuth);
				//
				em.merge(etsP);
			}
		}
		
		for(EtablissementPersistant ets : listEts){
			if(StringUtil.isEmpty(ets.getDyc_key())) {
				ets.setDyc_key(EncryptionEtsUtil.getDecrypKey());
			}
			if(StringUtil.isEmpty(ets.getCode_func())) {
				ets.setCode_func("ETS_"+codeAuth);
			}
			//
			em.merge(ets);
		}
		
		if(isAbnAdded){
			for (SocietePersistant societeP : listSociete) {
				societeP.setOpc_abonne(abnP);
				societeP.setCode_func("SOC_"+codeAuth);
				//
				em.merge(societeP);
			}
		}
		
		transaction.commit();
		if(em.isOpen()){
			em.close();
		}
	}
	
	/**
	 * Maj DB si une des deux versions est à jour
	 */
	@Override
	public void executerInitScript(Date currentDateSoft) {
		List<EtablissementPersistant> listRestauP = findAll(EtablissementPersistant.class);
		
		if(listRestauP.size() == 0){
			executerFirstScript();
			return;
		}
		EtablissementPersistant restauP = listRestauP.get(0);
		
		if (!BooleanUtil.isTrue(restauP.getIs_script_torun())){
			return;
		}
		
		System.out.println("************************* Lancement script init *************************");
		
		EntityManager entityManager = emf.createEntityManager();
		
		File scriptFile = new File(getClass().getResource("/appli/conf/script_update.sql").getFile());
		BufferedReader br = null;
		FileReader fr = null;
		//
		try {
			List<String> listReq = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			String line;
			Date dateVersionFile = null;
			
			if(currentDateSoft == null) {
				currentDateSoft = restauP.getDate_soft();
			}
			
			br = new BufferedReader(new FileReader(scriptFile));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0 || line.trim().startsWith("--")) {
					continue;	
				}
				//	
				if(line.startsWith("**version")){
					String dateVersionSt = line.substring(line.indexOf("date_v:")+7).trim();
					dateVersionFile = DateUtil.stringToDate(dateVersionSt, "dd-MM-yyyy");
					continue;
				}
				// Si version soft inférieur alors on ne prend pas
				if(currentDateSoft != null && dateVersionFile.compareTo(currentDateSoft) <= 0){
					continue;
				}
				sb.append(line); 
				
				if(line.indexOf(";") != -1){
					if(sb.toString().trim().length() > 0){
						listReq.add(sb.toString());
					}
					sb = new StringBuilder();
				}
			}
			
			// Ajouter les veiws
			scriptFile = new File(getClass().getResource("/appli/conf/script_view.sql").getFile());
			fr = null;
			//
			sb = new StringBuilder();
			line = null;
			br = new BufferedReader(new FileReader(scriptFile));
			while ((line = br.readLine()) != null) {
				if (line.trim().length() == 0 || line.trim().startsWith("--")) {
					continue;	
				}
				sb.append(line); 
				
				if(line.indexOf(";") != -1){
					if(sb.toString().trim().length() > 0){
						listReq.add(sb.toString());
					}
					sb = new StringBuilder();
				}
			}
			// End	
						
			for (String req : listReq) {
				entityManager.getTransaction().begin();
				try{
					getNativeQuery(entityManager, req).executeUpdate();
					entityManager.getTransaction().commit();
				} catch(Exception e){
					entityManager.getTransaction().rollback();
					e.printStackTrace();
				}
			}
			
			entityManager.getTransaction().begin();
			restauP = entityManager.find(EtablissementPersistant.class, restauP.getId());
			// Maj flag
			restauP.setIs_script_torun(false);
			
			entityManager.merge(restauP);
			
			// commit
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
			entityManager.getTransaction().rollback();
		} finally {
			entityManager.close();
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				LOGGER.error(ex);
			}
		}
		// Synchroniser toutes les données
		if(ReplicationGenerationEventListener._IS_LOCAL_SYNCHRO_INSTANCE){
			synchroService.addToSynchroniseQueu(null);
		}
	}

	@Override
	@Transactional
	public void mergeEtablissement(EtablissementPersistant restaurantP, boolean isFromParam) {
		EtablissementPersistant etsPDb = findById(EtablissementPersistant.class, restaurantP.getId());
		
		if(!isFromParam) {
			etsPDb.setNom(restaurantP.getNom());
			etsPDb.setRaison_sociale(restaurantP.getRaison_sociale());
			etsPDb.setAdresse(restaurantP.getAdresse());
		} else {
			etsPDb.setIs_valid_cpt(restaurantP.getIs_valid_cpt());
			etsPDb.setIs_cmd_ets_ferme(restaurantP.getIs_cmd_ets_ferme());
			etsPDb.setIs_valid_auto_esp(restaurantP.getIs_valid_auto_esp());
			etsPDb.setDuree_cmd(restaurantP.getDuree_cmd());
			etsPDb.setMax_dist(restaurantP.getMax_dist());
			etsPDb.setMax_heure_cmd(restaurantP.getMax_heure_cmd());
			
			etsPDb.getList_ouverture().clear();
			etsPDb.getList_ouverture().addAll(restaurantP.getList_ouverture());
		}
		
		getEntityManager().merge(etsPDb);
	}
	
	/** ----------------------------- Maintenance ------------------------------------*/
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void purgerDonneesBase(Long etatFinancierId, Map<String, ?> mapPurge) {
		EtatFinancePersistant etatP = (EtatFinancePersistant)findById(EtatFinancePersistant.class, etatFinancierId);
		Date dateEtat = etatP.getDate_etat();
		EntityManager em = getEntityManager();
		// Nettoyer l'entité manager
		em.flush();
		em.clear();
		
		List<InventairePersistant> listInventaires = getQuery("from InventairePersistant where date_realisation>=:dateReference")
				.setParameter("dateReference", dateEtat)
				.getResultList();
		
		if(listInventaires.size() == 0){
			MessageService.addBannerMessage("Vous devez réaliser un inventaire postérieur à la date de cet état avant de purger les données.");
			return;
		}
		if(StringUtil.isTrue(""+mapPurge.get("JOU")) && !StringUtil.isTrue(""+mapPurge.get("VEC"))){
			MessageService.addBannerMessage("On ne peut pas purger les journées sans purger les ventes caisse.");
			return;
		}
		
		// Mouvements caisse
		if(StringUtil.isTrue(""+mapPurge.get("VEC"))){
			//Carte de fidelité
			getNativeQuery("delete from carte_fidelite_points where caisse_mouvement_id in (select id from caisse_mouvement where date_vente<:dateReference)")
			.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from carte_fidelite_conso where caisse_mouvement_id in (select id from caisse_mouvement where date_vente<:dateReference)")
			.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			// Portefeuille
			getNativeQuery("delete from client_portefeuille_mvm where caisse_mouvement_id in (select id from caisse_mouvement where date_vente<:dateReference)")
			.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			// Mouvements caisse
			getNativeQuery("delete from caisse_mouvement_article where mvm_caisse_id in (select id from caisse_mouvement where date_vente<:dateReference)")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from caisse_mouvement_offre where mvm_caisse_id in (select id from caisse_mouvement where date_vente<:dateReference)")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from caisse_mouvement_statut where caisse_mvm_id in (select id from caisse_mouvement where date_vente<:dateReference)")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from caisse_mouvement where date_vente<:dateReference")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			// Vente caisse
			purgeMvm(dateEtat, "vc");
		}
		// Ecritures
		if(StringUtil.isTrue(""+mapPurge.get("ECR"))){
			getNativeQuery("delete from compte_bancaire_ecriture where date_mouvement<:dateReference")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from compte_bancaire_fonds where date_mouvement<:dateReference")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
		}
		// Dépenses
		if(StringUtil.isTrue(""+mapPurge.get("REC"))){
			getNativeQuery("delete from charge_divers where date_mouvement<:dateReference and sens='D'")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
		}
		// Recettes
		if(StringUtil.isTrue(""+mapPurge.get("DEP"))){
			getNativeQuery("delete from charge_divers where date_mouvement<:dateReference and sens='C'")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
		}
		// Mouvements stocks
		if(StringUtil.isTrue(""+mapPurge.get("MVM"))){
			purgeMvm(dateEtat, 
						TYPE_MOUVEMENT_ENUM.av.toString(),
						TYPE_MOUVEMENT_ENUM.t.toString(), 
						TYPE_MOUVEMENT_ENUM.c.toString(), 
						TYPE_MOUVEMENT_ENUM.p.toString()
					);
		}
		// Ventes hors caisse
		if(StringUtil.isTrue(""+mapPurge.get("VEA"))){
			purgeMvm(dateEtat, TYPE_MOUVEMENT_ENUM.v.toString());
		}
		if(StringUtil.isTrue(""+mapPurge.get("ACH"))){
			purgeMvm(dateEtat, TYPE_MOUVEMENT_ENUM.a.toString());
		}
		// Inventaires // Ne purger que si un inventaire est réalisé aprés la date de purge
		if(StringUtil.isTrue(""+mapPurge.get("INV"))){
			// Mouvement stock
			purgeMvm(dateEtat, TYPE_MOUVEMENT_ENUM.i.toString());
			// Inventaire
			getNativeQuery("delete from inventaire_detail where inventaire_id in (select id from inventaire where date_realisation<:dateReference)")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from inventaire where date_realisation<:dateReference")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
		}
		
		// Journées
		if(StringUtil.isTrue(""+mapPurge.get("JOU"))){
			getNativeQuery("delete from caisse_journee where journee_id in (select id from journee where date_journee<:dateReference)")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
			getNativeQuery("delete from journee where date_journee<:dateReference")
				.setParameter("dateReference", dateEtat).executeUpdate();
			em.flush();
		}
				
		// Maj état
		etatP.setIs_purge(true);
		
		em.merge(etatP);
	}
	
	@Transactional
	private void purgeMvm(Date dateRef, String ...types){
		EntityManager em = getEntityManager();
		//
		for(String type : types){
			// Fournisseur chèque
			getNativeQuery("delete from fournisseur_cheque where mouvement_id in (select id from mouvement where date_mouvement<:dateReference "
					+ "and type_mvmnt=:type)")
				.setParameter("dateReference", dateRef)
				.setParameter("type", type)
				.executeUpdate();
			
			getNativeQuery("delete from mouvement_article where mouvement_id in (select id from mouvement where date_mouvement<:dateReference "
					+ "and type_mvmnt=:type)")
				.setParameter("dateReference", dateRef)
				.setParameter("type", type)
				.executeUpdate();
			em.flush();
			getNativeQuery("delete from mouvement where date_mouvement<:dateReference and type_mvmnt=:type")
				.setParameter("dateReference", dateRef)
				.setParameter("type", type)
				.executeUpdate();
			em.flush();
		}
	}
	
	@Override
	public void dumpBase(String targetDir) {
		if(!new File(targetDir).isDirectory()) {
			return;
		}
		
		targetDir = targetDir + "/BACKUP_DB_CAISSE";
		try {
			if(!new File(targetDir).exists()) {
				new File(targetDir).mkdirs();
			}
		} catch(Exception e) {
			
		}
		
		purgerFichier(targetDir, 30);
		String fileDbTarget = targetDir+"/backup_"+DateUtil.dateToString(new Date(), "dd_MM_yyyy-HH_mm_ss");
		//
		Process p = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			String mysqlDumpExePath = StrimUtil.getGlobalConfigPropertie("db.dump.path");
			
			if(StringUtil.isNotEmpty(mysqlDumpExePath) && new File(mysqlDumpExePath).exists()) {
				String userDb = StrimUtil.getGlobalConfigPropertie("db.user");
				String pwDb   = StrimUtil.getGlobalConfigPropertie("db.pw");
				String dbHost = StrimUtil.getGlobalConfigPropertie("db.host");
				String dbPort = StrimUtil.getGlobalConfigPropertie("db.port");
				String dbName = StrimUtil.getGlobalConfigPropertie("db.name");
				
				
				String cmd = mysqlDumpExePath+"\\mysqldump.exe --user="+userDb+" --password="+pwDb+" --host="+dbHost+" --port="+dbPort+" --result-file=\""+fileDbTarget+".sql\" --databases \""+dbName+"\"";
				p = runtime.exec(cmd);
				
		       int processComplete = p.waitFor();
		       if (processComplete == 0) {
		    	   new ZipUtil4("CM123456?!", fileDbTarget+".sql", fileDbTarget+".zip", false).pack();
		       }
				
				try {
					FileUtils.forceDelete(new File(fileDbTarget+".sql"));
				} catch (IOException e) {
					e.printStackTrace();
				}
		      }
		} catch(Exception e) {
			e.printStackTrace();
		}
//		 Process p = null;
//		try {
//			Runtime runtime = Runtime.getRuntime();
//			String mysqlDumpExePath = StrimUtil.getGlobalConfigPropertie("db.dump.path");
//			
//			if(!new File(mysqlDumpExePath).exists()) {
//				MessageService.addGrowlMessage("Répertoire non trouvé", "Le chemin du binaire MySQL "+mysqlDumpExePath+" est incorrecte.");
//				return;
//			}
//			
//			if(!new File(targetDir).exists()) {
//				MessageService.addGrowlMessage("Répertoire non trouvé", "Le chemin destination "+targetDir+" n'exite pas.");
//				return;
//			}
//			
//			String cmd = mysqlDumpExePath+"/mysqldump.exe --user="+StrimUtil.getGlobalConfigPropertie("db.user")+" --password="+StrimUtil.getGlobalConfigPropertie("db.pw")+" --host="+StrimUtil.getGlobalConfigPropertie("db.host")+" --port="+StrimUtil.getGlobalConfigPropertie("db.port")+" --result-file=\""+targetDir+"/backup_caisse_manager_"+DateUtil.dateToString(new Date(), "dd_MM_yyyy_HH_mm_ss")+".sql\" --databases \""+StrimUtil.getGlobalConfigPropertie("db.name")+"\"";
//			p = runtime.exec(cmd);
//			//
//	        int processComplete = p.waitFor();
//	        if (processComplete == 0) {
//	        	MessageService.addGrowlMessage(MSG_TYPE.SUCCES, "Dump OK", "La fichier de dump est généré avec succès.");
//	        } else {
//	        	MessageService.addGrowlMessage("Dump KO", "La sauvegarde a échoué.");
//	        }
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	private static void purgerFichier(String targetDir, int maxSaveNbr) {
		File backUpFolder = new File(targetDir);
		
		Map<Long,File> mapData = new LinkedHashMap<Long, File>();
		List<Long> listTime = new ArrayList<>();
		File[] files = backUpFolder.listFiles();
		//
		if(files != null) {
			for (File tempFile : files) {
				if(tempFile.getName().startsWith("BACKUP_DB_CAISSE")) {
					mapData.put(tempFile.lastModified(), tempFile);
					listTime.add(tempFile.lastModified());
		        }
			}
		}
		
		Collections.sort(listTime);

		while(mapData.size() >= maxSaveNbr) {
			Object key = mapData.keySet().iterator().next();
			File file = mapData.get(key);
			try {
				FileUtils.forceDelete(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mapData.remove(key);
		}
	}
	
	@Override
	public Map<String, String> getSystemInfos() {
		MessageService.getGlobalMap().put("NO_ETS", true);
		Map<String, String> mapInfos = new HashMap<>();
		//
		Object[] data = (Object[]) getSingleResult(getNativeQuery("SELECT table_schema \"Table\", "+
	    "sum( data_length + index_length ) / 1024 / 1024 \"Taille MB\", "+
	    	"sum( data_free )/ 1024 / 1024 \"Espace libre MB\" "+
			"FROM information_schema.TABLES "+
			"where table_schema='"+StrimUtil.getGlobalConfigPropertie("db.name")+"' "+
			"GROUP BY table_schema"));
		
		
		if(data == null){
			data = new Object[10];
		}
		
		mapInfos.put("DB_SIZE", BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+data[1])));
		
		File dirFiles = new File(StrimUtil.BASE_FILES_PATH);
		mapInfos.put("FILES_SIZE", BigDecimalUtil.formatNumber((dirFiles.exists() ? BigDecimalUtil.get(""+FileUtils.sizeOfDirectory(dirFiles)/1048576) : BigDecimalUtil.ZERO)));
		
		long diskSize = new File("/").getTotalSpace();
		mapInfos.put("HDD_SIZE", BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+(diskSize/1073741824))));//1048576 Mo
		
		long maxMemory = Runtime.getRuntime().maxMemory();
		mapInfos.put("RAM_SYSTEM", BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+(maxMemory/1073741824)))); 
		
		long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
		        .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
		mapInfos.put("RAM_SERVEUR_SIZE", BigDecimalUtil.formatNumber(BigDecimalUtil.get(""+(memorySize/1073741824))));
		
		String osName = System.getProperty("os.name"); 
		mapInfos.put("OS_NAME", osName);
		
		MessageService.getGlobalMap().remove("NO_ETS");
		
		return mapInfos;
	}
	
	@Override
	@Transactional
	public void mergeParams(ParametragePersistant ... params) {
		EntityManager em = getEntityManager();
		Map mapConfig = (Map) MessageService.getGlobalMap().get("GLOBAL_CONFIG");
		for (ParametragePersistant parametrageP : params) {
			if(parametrageP == null) {
				continue;
			}
			em.merge(parametrageP);
			
			// Maj session
			mapConfig.put(parametrageP.getCode(), parametrageP.getValeur());			
		}
	}
	
	@Override
	@Transactional
	public void updateParams(Map<String, Object> params) {
		Map mapConfig = (Map) MessageService.getGlobalMap().get("GLOBAL_CONFIG");
		EntityManager em = getEntityManager();
		//
		for (String code : params.keySet()) {
			List<ParametragePersistant> listParametrageP = getQuery("from ParametragePersistant where code=:code")
					.setParameter("code", code)
					.getResultList();

			ParametragePersistant parametrageP = listParametrageP.size() > 0 ? listParametrageP.get(0) : null;
			int i = 0;
			for (ParametragePersistant parametragePersistant : listParametrageP) {
				if(i > 0) {
					em.remove(parametragePersistant);
					em.flush();
				}
				i++;
			}
			if(parametrageP == null){
				parametrageP = getParameterLocalByCode(code);
			}
			
			if(StringUtil.isNotEmpty(params.get(code))){
				parametrageP.setValeur(""+params.get(code));
			} else {
				parametrageP.setValeur(null);
			}
			// Maj session
			mapConfig.put(parametrageP.getCode(), parametrageP.getValeur());			
			//
			em.merge(parametrageP);
		}
	}

	@Override
	@Transactional
	public void mergeLocalPrinters(String codeAuth, String imprimantes) {
		EtablissementPersistant etsP = getOneByField(EtablissementPersistant.class, "code_authentification", codeAuth);
		etsP = (EtablissementPersistant) findById(EtablissementPersistant.class, etsP.getId());
		
		etsP.setImprimantes(imprimantes);
		
		getEntityManager().merge(etsP);
	}

	@Override
	public Map<String, String> getCodeBarreBalanceStart() {
		List<CaissePersistant> listCaisse = getQuery("from CaissePersistant where type_ecran=:type")
			.setParameter("type", ContextAppli.TYPE_CAISSE_ENUM.BALANCE.toString())
			.getResultList();

		Map<String, String> mapData = new HashMap<>();
		for (CaissePersistant caisseP : listCaisse) {
			String mode = "C";
			ParametragePersistant paramModeP = getParameterByCode("BALANCE_MODE", caisseP.getId());
			if(paramModeP != null && StringUtil.isNotEmpty(paramModeP.getValeur())){
				mode = paramModeP.getValeur();
			}
			
			String code = "C".equals(mode) ? "99" : "22";
			
			ParametragePersistant paramP = getParameterByCode("CODE_BARRE_BALANCE", caisseP.getId());
			if(paramP != null && StringUtil.isNotEmpty(paramP.getValeur())){
				code = paramP.getValeur();
			}
			
			String compo = null;
			ParametragePersistant paramCompoP = getParameterByCode("CODE_BARRE_BALANCE_COMPO", caisseP.getId());
			if(paramCompoP != null && StringUtil.isNotEmpty(paramCompoP.getValeur())){
				compo = paramCompoP.getValeur();
			}
			mapData.put(code, compo);
		}
		
		return mapData;
	}
	
	public static String getDycCodeAuth(EtablissementPersistant etsP, String codeAuth) {
		if(etsP == null || codeAuth == null) {
			return null;
		}
		EncryptionEtsUtil encryptionUtil = new EncryptionEtsUtil(EncryptionEtsUtil.getDecrypKey());
		codeAuth = encryptionUtil.decrypt(codeAuth);
		if(codeAuth == null) {
			codeAuth = etsP.getCode_authentification();
		}
		return codeAuth;
	}
	
	public static String getEtsCodeAuth() {
		try {
			if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE || ContextAppli.IS_FULL_CLOUD()){
		 		return getDycCodeAuth(ContextAppli.getEtablissementBean(), ContextAppli.getEtablissementBean().getCode_authentification());
			} else {
				String codeAuth = StrimUtil.getGlobalConfigPropertie("caisse.code.auth");
				if(StringUtil.isEmpty(codeAuth)) {
					if(ContextAppli.getEtablissementBean() != null) {
						return getDycCodeAuth(ContextAppli.getEtablissementBean(), ContextAppli.getEtablissementBean().getCode_authentification());
					}
				} else {
					return codeAuth;
				}
			}
			
			IEtablissementService etsSrv = ServiceUtil.getBusinessBean(IEtablissementService.class);	
			if(etsSrv != null) {
				EtablissementPersistant etsDb = etsSrv.findAll(EtablissementPersistant.class).get(0);
				return getDycCodeAuth(etsDb, etsDb.getCode_authentification());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
    /**
     * @return
     */
    public static EtablissementPersistant getEtablissement(){
    	IUserService userService = ServiceUtil.getBusinessBean(IUserService.class);
    
    	EtablissementPersistant etablissement = null;
 		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE || ContextAppli.IS_FULL_CLOUD()){
 			etablissement = userService.getEntityManager().find(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
 		} else{
 			etablissement = userService.findAll(EtablissementPersistant.class).get(0);
 	    	etablissement = userService.findById(EtablissementPersistant.class, etablissement.getId());	
 		}
		
		return etablissement;
    }
	
	@Override
	public EtablissementPersistant getEtsOneOrCodeAuth() {
		EtablissementPersistant etsP = null;
		//
		if(ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE || ContextAppli.IS_FULL_CLOUD()){
			if(ContextAppli.getEtablissementBean() != null) {
				//etsP = getOneByField(EtablissementPersistant.class, "code_authentification", ContextAppli.getEtablissementBean().getCode_authentification());
				etsP = getEntityManager().find(EtablissementPersistant.class, ContextAppli.getEtablissementBean().getId());
			}
		} else {
			etsP = (EtablissementPersistant) findAll(EtablissementPersistant.class).get(0);
		}
		
		if(etsP == null) {
			etsP = new EtablissementPersistant();
		}
		return etsP;
	}

	@Override
	@Transactional
	public void majInfosSaveDb(String path, String startDt1, String startDt2) {
		EtablissementPersistant etsP = findById(EtablissementPersistant.class, ContextGloabalAppli.getEtablissementBean().getId());
		etsP.setHeure_save_db1(startDt1);
		etsP.setHeure_save_db2(startDt2);
		etsP.setSave_db_path(path);
		//
		getEntityManager().merge(etsP);
	}
}
