package appli.model.domaine.stock.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.stock.bean.ChargeDiversBean;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.administration.service.ICompteBancaireService;
import appli.model.domaine.compta.persistant.PaiementPersistant;
import appli.model.domaine.stock.dao.IChargeDiversDao;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import appli.model.domaine.stock.persistant.LabelsGroupPersistant;
import appli.model.domaine.stock.service.IChargeDiversService;
import appli.model.domaine.stock.validator.ChargeDiversValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ChargeDiversValidator.class)
@Named
public class ChargeDiversService extends GenericJpaService<ChargeDiversBean, Long> implements IChargeDiversService{
	@Inject
	private IChargeDiversDao chargeDao;
	@Inject
	private ICompteBancaireService compteBancaireService;

	@Override
	public Date getMaxDate(String sens) {
		Date dateMax = (Date) getSingleResult(getQuery("select max(date_mouvement) from ChargeDiversPersistant "
				+ "where sens=:sens")
				.setParameter("sens", sens));
		if(dateMax == null){
			return new Date();
		}
		return dateMax;
	}
	
	@Override
	@Transactional
	public void valideRegroupementBL(ChargeDiversBean mvmBean) {
		EntityManager entityManager = getEntityManager();
		
		List<PaiementPersistant> listPaiement = null;
		if(mvmBean.getList_paiement() != null){
			listPaiement = new ArrayList<>(mvmBean.getList_paiement());			
		}
		
		//
		ChargeDiversPersistant mvmDb = mvmBean.getId()!=null ? chargeDao.findById(mvmBean.getId()) : null;
		
		if(mvmDb == null) {
			mvmDb = new ChargeDiversPersistant();
			mvmDb.setSens("D");
			mvmDb.setDate_creation(new Date());
			mvmDb.setIs_groupant(true);
		} else {
			// Effacer les anciens
			getQuery("update ChargeDiversPersistant set charge_group_id=NULL where charge_group_id=:mvmGrpId")
				.setParameter("mvmGrpId", mvmDb.getId())
				.executeUpdate();
		}
		mvmDb.setOpc_fournisseur(mvmBean.getOpc_fournisseur());
		mvmDb.setNum_bl(mvmBean.getNum_bl());
		// Mode de paiement
		mvmDb.setDate_mouvement(mvmBean.getDate_mouvement());
		//--------------------------------------------
		mvmDb = entityManager.merge(mvmDb);
		mvmBean.setId(mvmDb.getId());
		
		TYPE_ECRITURE source = mvmDb.getSens().equals("D") ? TYPE_ECRITURE.DEPENSE : TYPE_ECRITURE.RECETTE;
		compteBancaireService.mergePaiements(source, listPaiement, mvmDb.getOpc_fournisseur(), mvmDb.getId(), "Regroupement dépenses", "D", mvmDb.getDate_mouvement());
		
		// Maj des mouvement dépendants
		for(Long mvmId : mvmBean.getMouvementIds()) {
			getQuery("update ChargeDiversPersistant set charge_group_id=:grpId where id=:mvmId")
				.setParameter("grpId", mvmDb.getId())
				.setParameter("mvmId", mvmId)
				.executeUpdate();
		}
	}
	
	@Override
	public List<ChargeDiversPersistant> getListCDGroupe(Long charge_id) {
		return chargeDao.getQuery("from ChargeDiversPersistant where charge_group_id=:parentId order by date_mouvement desc")
				.setParameter("parentId", charge_id)
				.getResultList();
	}
	
	@Override
	public List<ChargeDiversPersistant> getChargeNonGroupe(Long fournisseurId, List<Long> ignoreIds, Date dateDebut, Date dateFin) {
		String req = "from ChargeDiversPersistant where opc_fournisseur.id=:fournId and sens='D' ";
		
		if(ignoreIds != null && ignoreIds.size() > 0) {
			req = req + "and (charge_group_id is null or id in (:ignoreIds)) ";
		} else{
			req = req + "and charge_group_id is null ";
		}
		
		if(dateDebut != null){
			req = req + "and date_mouvement>=:dtDebut ";
		}
		if(dateFin != null){
			req = req + "and date_mouvement<=:dtFin ";
		}
		req = req + "order by id";
		
		Query query = getQuery(req).setParameter("fournId", fournisseurId);
		
		if(ignoreIds != null && ignoreIds.size() > 0) {
			query.setParameter("ignoreIds", ignoreIds);
		}
		if(dateDebut != null){
			query.setParameter("dtDebut", dateDebut);
		}
		if(dateFin != null){
			query.setParameter("dtFin", dateFin);
		}
		List<ChargeDiversPersistant> listMvm = query.getResultList();
		List<ChargeDiversPersistant> listMvmNonPaye = new ArrayList<>();
		//
		for (ChargeDiversPersistant mouvementP : listMvm) {
			if(mouvementP.getList_paiement().size() == 0) {
				listMvmNonPaye.add(mouvementP);
			}
		}

		return listMvmNonPaye;
	}
	
	/**
	 * @param mouvementBean
	 * @return
	 */
//	@Transactional
//	private void ajouterEcritureCompte(ChargeDiversBean e){
//		// Supprimer l'ancienne écriture
//		supprimerEcritureCompte(e);
//		
//		// Ajouter
//		if(e.getOpc_compte_bancaire() != null && !BigDecimalUtil.isZero(e.getMontant())){
//			EcriturePersistant ecritureP = new EcriturePersistant();
//			ecritureP.setDate_mouvement(e.getDate_mouvement());
//			ecritureP.setElementId(e.getId());
//			ecritureP.setLibelle(e.getLibelle());
//			ecritureP.setMontant(e.getMontant());
//			ecritureP.setOpc_banque(e.getOpc_compte_bancaire());
//			ecritureP.setSource(e.getSens().equals("D") ? TYPE_ECRITURE.DEPENSE.toString() : TYPE_ECRITURE.RECETTE.toString());
//			ecritureP.setSens(e.getSens());
//			//
//			getEntityManager().merge(ecritureP);
//		}
//	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public ChargeDiversBean update(ChargeDiversBean e) {
		List<PaiementPersistant> listPaiement = null;
		if(e.getList_paiement() != null){
			listPaiement = new ArrayList<>(e.getList_paiement());			
		}
		if(e.getSens() == null) {
			e.setSens("D");
		}
		TYPE_ECRITURE source = e.getSens().equals("D") ? TYPE_ECRITURE.DEPENSE : TYPE_ECRITURE.RECETTE;
		super.update(e);
		
		
		compteBancaireService.mergePaiements(source, listPaiement, e.getOpc_fournisseur(), e.getId(), e.getLibelle(), e.getSens(), e.getDate_mouvement());
		
		return e;
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create(ChargeDiversBean e) {
		List<PaiementPersistant> listPaiement = null;
		if(e.getList_paiement() != null){
			listPaiement = new ArrayList<>(e.getList_paiement());			
		}
		if(e.getSens() == null) {
			e.setSens("D");
		}		
		super.create(e);
		
		TYPE_ECRITURE source = "D".equals(e.getSens()) ? TYPE_ECRITURE.DEPENSE : TYPE_ECRITURE.RECETTE;
		compteBancaireService.mergePaiements(source, listPaiement, e.getOpc_fournisseur(), e.getId(), e.getLibelle(), e.getSens(), e.getDate_mouvement());
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(Long id) {
		ChargeDiversBean e = findById(id);
		// Supprimer les paiements
		TYPE_ECRITURE source = e.getSens().equals("D") ? TYPE_ECRITURE.DEPENSE : TYPE_ECRITURE.RECETTE;
		compteBancaireService.deletePaiements(id, source);
		
		super.delete(id);
	}
	
	@Override
	public Map<Long, List<ChargeDiversPersistant>> getMapChargeGroupe(List<ChargeDiversPersistant> listGroup) {
		Set<Long> listIds = new HashSet<>();
		Map<Long, List<ChargeDiversPersistant>> mapData = new HashMap<>();
		//
		for (ChargeDiversPersistant mvmP : listGroup) {
			listIds.add(mvmP.getId());
		}
		List<ChargeDiversPersistant> listData = (listIds.size()>0 ? chargeDao.getQuery("from ChargeDiversPersistant mvm "
				+ "where charge_group_id in (:ids) order by mvm.date_mouvement desc")
				.setParameter("ids", listIds)
				.getResultList() : new ArrayList<>());
		//
		for (ChargeDiversPersistant mvm : listData) {
			List<ChargeDiversPersistant> dataBD = mapData.get(mvm.getCharge_group_id());
			if(dataBD == null){
				dataBD = new ArrayList<>();
				mapData.put(mvm.getCharge_group_id(), dataBD);
			}
			dataBD.add(mvm);
		}
		
		return mapData;
	}
	
	@Override
	public String generate_numBl(String type) {
		
		Date currentDate = DateUtil.getCurrentDate(); 
		SimpleDateFormat formater =  new SimpleDateFormat("yyMMdd");
		String date = formater.format(currentDate);
		
		int maxNum = chargeDao.max_num(date);
		String max = "001";
		String numBl = "";
		
		if(StringUtil.isNotEmpty(maxNum)){
			max = maxNum+1+"";
		}
		if(max.length() == 1){
			max = "00"+max;
		} else if(max.length() == 2){
			max = "0"+max;
		}
		
		if(type == null || type.equals("D")){
			numBl = "DP-"+date+"-"+max;
		}else {
			numBl = "RC-"+date+"-"+max;
		}
		
		return numBl;
	}

	/**
	 * automatiser une charge avec une date de début, date de fin et une fréquence en jours
	 */
	@Override
	@WorkModelMethodValidator
	public void update_automate(ChargeDiversPersistant cdb,Date dateDebut, Date dateFin, Integer frequence) {
		chargeDao.update_automate(cdb,dateDebut, dateFin, frequence);
	}

	/**
	 *  activer ou désactiver l'automatisation d'une charge donnée par son id
	 */
	@Override
	public void controleAutomate(Long id) {
		chargeDao.controleAutomate(id);
	}

	/**
	 * retourne toutes les charges qui sont automatisés et activées
	 */
	@Override
	public List<ChargeDiversPersistant> getAllTheAutomatedCharge() {
		return chargeDao.getAllTheAutomatedCharge();
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void deleteAutomatisation(ChargeDiversPersistant cdp) {
		chargeDao.deleteAutomatisation(cdp);
	}
//
//	@Override
//	@Transactional
//	public void pointerAchatCheque(Long depId, Date datePointage) {
//		ChargeDiversPersistant chargeP = chargeDao.findById(depId);
//		
//		if(chargeP.getDate_encaissement() != null){
//			chargeP.setDate_encaissement(null);
//		} else{
//			chargeP.setDate_encaissement(datePointage);	
//		}
//		
//		chargeDao.update(chargeP);
//	}

	@Transactional
	@Override
	public void mergeLabels(List<LabelsGroupPersistant> listLib, String source) {
		getQuery("delete from LabelsGroupPersistant where source=:source")
			.setParameter("source", source)
			.executeUpdate();
		//
		EntityManager em = getEntityManager();
		//
		for (LabelsGroupPersistant labelsGroupP : listLib) {
			labelsGroupP.setSource(source);
			//
			em.merge(labelsGroupP);
		}
	}

	@Override
	public List<LabelsGroupPersistant> getLibelleParametres(String source) {
		return getQuery("from LabelsGroupPersistant where source=:source order by idxIhm")
				.setParameter("source", source)
				.getResultList();
	}
}
