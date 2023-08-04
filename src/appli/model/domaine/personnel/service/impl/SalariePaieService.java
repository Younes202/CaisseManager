package appli.model.domaine.personnel.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.personnel.bean.paie.SalariePaieBean;
import appli.controller.domaine.util_erp.ContextAppli;
import appli.controller.domaine.util_erp.ContextAppli.TYPE_ECRITURE;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.PointageEventPersistant;
import appli.model.domaine.personnel.persistant.paie.PointagePersistant;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import appli.model.domaine.personnel.service.IPointageService;
import appli.model.domaine.personnel.service.ISalariePaieService;
import framework.model.common.util.BigDecimalUtil;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.DateUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
public class SalariePaieService extends GenericJpaService<SalariePaieBean, Long> implements ISalariePaieService{
	@Inject
	private IPointageService pointageService;
	
	@Override
	public Map<String, SalairePersistant> getMapPaie(Integer annee) {
		Map<String, SalairePersistant> mapPaie = new HashMap<String, SalairePersistant>();
		
		List<SalairePersistant> listPaie = getQuery("from SalairePersistant where annee=:annee ")
				.setParameter("annee", annee)
				.getResultList();
		
		for (SalairePersistant employePaieP : listPaie) {
			String key = employePaieP.getOpc_employe().getId()+"-";
			if(employePaieP.getMois() < 10) {
				key = key + "0" + employePaieP.getMois();
			} else {
				key = key + employePaieP.getMois();
			}
			mapPaie.put(employePaieP.getOpc_employe().getId()+"-"+employePaieP.getMois(), employePaieP);
		}
		
		return mapPaie;
	}

	@Override
	public SalairePersistant getPaieByEmployeYearMonth(Long employe_id, Integer annee, Integer mois) {
		return (SalairePersistant) getSingleResult(getQuery("from SalairePersistant where opc_employe.id=:employe_id "
				+ " and annee=:annee and mois=:mois")
				.setParameter("employe_id", employe_id)
				.setParameter("annee", annee)
				.setParameter("mois", mois)
		);
	}
	
	@Override
	public BigDecimal getSalaireByEmployeAndDate(Long employe_id, Date date_debut, Date date_fin) {
		return (BigDecimal) getQuery("select sum(montant_net) from SalairePersistant where opc_employe.id=:employeId "
				+ "and date_debut>=:dateDebut and date_fin<=:dateFin")
				.setParameter("employeId", employe_id)
				.setParameter("dateDebut", date_debut)
				.setParameter("dateFin", date_fin)
				.getSingleResult();
	}
	
	@Override
	public List<SalairePersistant> getPaieByYearMonth(Integer annee, Integer mois) {
		return (List<SalairePersistant>) getQuery("from SalairePersistant where "
				+ "annee=:annee and mois=:mois "
				+ "order by opc_employe.nom")
				.setParameter("annee", annee)
				.setParameter("mois", mois)
				.getResultList();
	}
	
//	@Override
//	public List<Integer> getListAnnee() {
//		Object[] cal = (Object[])getNativeQuery("select min(date_debut), max(date_fin) from exercice").getSingleResult();
//		List<Integer> listAnnee = new ArrayList<>();
//		//
//		if(cal != null && cal.length > 0 && cal[0] != null){
//			Date date_debut = new Date( ( (Date) cal[0] ).getTime());
//			Date date_fin = new Date( ( (Date) cal[1] ).getTime());
//			
//			int debut_annee = DateUtil.getCalendar(date_debut).get(Calendar.YEAR);
//			int fin_annee = DateUtil.getCalendar(date_fin).get(Calendar.YEAR);
//			
//			while(debut_annee <= fin_annee) {
//				listAnnee.add(debut_annee);
//				debut_annee++;
//			}
//		}
//		return listAnnee;
//	}
	
	/**
	 * @param ab
	 */
	@Transactional
	private void supprimerEcritureCompte(Long elementId){
		getQuery("delete from EcriturePersistant where elementId=:elementId "
				+ "and source=:source")
			.setParameter("elementId", elementId)
			.setParameter("source", TYPE_ECRITURE.PAIE_SAL.toString())
			.executeUpdate();
	}

	@Override
	public Map<Integer, Boolean> getMapEtatMois(Integer annee) {
		Map<Integer, Boolean> mapEtatMois = new HashMap();
		
		List<Integer> listMoisClos = getQuery("select mois from SalairePersistant where annee=:annee "
				+ "group by mois")
				.setParameter("annee", annee)
				.getResultList();
		
		for(Integer i=1; i<13; i++) {
			Boolean isClos = false;
			for (Integer mois : listMoisClos) {
				if(mois.equals(i)) {
					isClos = true;
					break;
				}
			}
			mapEtatMois.put(i, isClos);
		}
		return mapEtatMois;
	}
	
	@Override
	public boolean isLastMois(String mois, Integer annee) {
		Integer currMonth = Integer.valueOf(mois);
		List<SalairePersistant> listSalaireOfLastMonth = new ArrayList<SalairePersistant>();
		
		if(currMonth != 1) {
			Integer lastMonth = Integer.valueOf(mois) - 1;
			
			listSalaireOfLastMonth = getQuery("from SalairePersistant where mois=:mois and annee=:annee ")
					.setParameter("mois", lastMonth)
					.setParameter("annee", annee)
					.getResultList();
		} else {
			listSalaireOfLastMonth = getQuery("from SalairePersistant where mois=:mois and annee=:annee ")
					.setParameter("mois", Integer.valueOf(12))
					.setParameter("annee", annee-1)
					.getResultList();
		}
		
		if(listSalaireOfLastMonth.size() > 0) {
			SalairePersistant firstSal = listSalaireOfLastMonth.get(0);
			if(BooleanUtil.isTrue(firstSal.getDate_paiement() != null)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	/**
	 * @param httpUtil
	 * @param currentDate
	 * @return
	 */
	@Override
	@Transactional
	public void calculSalaireFromPointage(Date currentDate){
		Calendar cal = DateUtil.getCalendar(currentDate);
		Map<Long, SalairePersistant> mapSalaire = new HashMap<>();
		int month = cal.get(Calendar.MONDAY)+1;
		EntityManager em = getEntityManager();
		
		List<PointageEventPersistant> listEvents = getQuery("from PointageEventPersistant where "
				+ "year(date_pointage)=:year "
				+ "and month(date_pointage)=:month "
				+ "order by opc_employe.numero, date_pointage")
				.setParameter("month", month)
				.setParameter("year", cal.get(Calendar.YEAR))
				.getResultList();
		
		Map<Long, BigDecimal> mapTarif = new HashMap<>();
 		List<EmployePersistant> listEmpl = new ArrayList<>();
 		List<Long> listEmplIds = new ArrayList<>();
		
 		for(PointageEventPersistant eventP : listEvents) {
 			Long emplId = eventP.getOpc_employe().getId();
 			if(listEmplIds.contains(emplId)) {
 				continue;
 			}
			listEmpl.add(eventP.getOpc_employe());
			mapTarif.put(emplId, eventP.getTarif_ref());
			//
			listEmplIds.add(emplId);
		}

		Map<String, Object> dureePointage = pointageService.getPointageHoraire(listEmpl, currentDate);
		Map<String, Map<String, Long>>  mapDuree = (Map<String, Map<String, Long>>) dureePointage.get("mapTotalHorairePointage");
		//String key = opc_employe.getNumero()+"-"+opc_employe.getNom()+" "+StringUtil.getValueOrEmpty(opc_employe.getPrenom());
			// Map<Jour mois, Durees en minute>

		for(EmployePersistant emplP : listEmpl) {
			long salaireNet = 0;
			
			String modePaie = emplP.getMode_paie();
			BigDecimal heureParJour = emplP.getHeureParJour();
			
			BigDecimal tarif = mapTarif.get(emplP.getId());
			if(BigDecimalUtil.isZero(tarif)){
				tarif = BigDecimalUtil.ZERO;
			}
			
			if(StringUtil.isEmpty(modePaie)) {
				modePaie = "J";
			}
			
			if(BigDecimalUtil.isZero(heureParJour)){
				heureParJour = BigDecimalUtil.get(7);
			}
			
			String key = emplP.getNumero()+"-"+emplP.getNom()+" "+StringUtil.getValueOrEmpty(emplP.getPrenom());		
			Map<String, Long> mapDureeDet = mapDuree.get(key);
		
			BigDecimal ttlMinute = null;
			for(String dt : mapDureeDet.keySet()) {
				Long durreMinute = mapDureeDet.get(dt);
				ttlMinute = BigDecimalUtil.add(ttlMinute, BigDecimalUtil.get(""+durreMinute));
			}
			// Transformer en heure
			Long nbTemps = Long.valueOf(0);
			if("J".equals(modePaie)) {
				nbTemps = (ttlMinute.longValue() / 60 / heureParJour.longValue());
			} else {
				nbTemps = ttlMinute.longValue() / 60 % heureParJour.longValue();
			}
			
			salaireNet = (nbTemps * tarif.longValue());
			
			SalairePersistant salaireP = mapSalaire.get(emplP.getId());
			if(salaireP == null) {
				salaireP = new SalairePersistant();
				salaireP.setOpc_employe(emplP);
				mapSalaire.put(emplP.getId(), salaireP);
			}
			salaireP.setTarif_jour(tarif);
			salaireP.setNbr_jours(nbTemps.intValue());
			
			salaireP.setMontant_brut(BigDecimalUtil.get(""+salaireNet));
			salaireP.setMontant_net(BigDecimalUtil.get(""+salaireNet));
		}
		
		List<PointagePersistant> listPointage = getQuery("from PointagePersistant where "
				+ "year(date_event)=:year "
				+ "and month(date_event)=:month "
				+ "order by date_event asc, type")
				.setParameter("month", month)
				.setParameter("year", cal.get(Calendar.YEAR))
				.getResultList();

		for(PointagePersistant eventP : listPointage) {
			SalairePersistant salaireP = mapSalaire.get(eventP.getOpc_employe().getId());
			if(salaireP == null) {
				salaireP = new SalairePersistant();
				salaireP.setOpc_employe(eventP.getOpc_employe());
				mapSalaire.put(eventP.getOpc_employe().getId(), salaireP);
			}
			
			if(eventP.getType().equals("tr")) {
				salaireP.setMontant_net(BigDecimalUtil.add(salaireP.getMontant_net(), BigDecimalUtil.multiply(eventP.getDuree(), eventP.getTarif_ref())));
			} else if(eventP.getType().equals("av")) {
				salaireP.setMt_avance(BigDecimalUtil.add(salaireP.getMt_avance(), eventP.getMontant()));
			} else if(eventP.getType().equals("pre")) {
				salaireP.setMt_pret(BigDecimalUtil.add(salaireP.getMt_pret(), eventP.getMontant()));
			} else if(eventP.getType().equals("pr")) {
				salaireP.setMt_prime(BigDecimalUtil.add(salaireP.getMt_prime(), eventP.getMontant()));
			} else if(eventP.getType().equals("rt")) {
				salaireP.setMt_retenue(BigDecimalUtil.substract(salaireP.getMt_retenue(), eventP.getMontant()));
			} else if(eventP.getType().equals("cg")) {
				if("NP".equals(eventP.getText_1())){
					salaireP.setMontant_net(BigDecimalUtil.substract(salaireP.getMontant_net(), BigDecimalUtil.multiply(eventP.getDuree(), eventP.getTarif_ref())));
				}
			} else if(eventP.getType().equals("cg")) {
				salaireP.setDescipline(StringUtil.getValueOrEmpty(salaireP.getDescipline()
						+"\n"+eventP.getDescription()
						+"\n"+eventP.getDescription()
						+"\n"+eventP.getDescription()
						));
			}
		}
		
		for(Long emplId : mapSalaire.keySet()) {
			SalairePersistant salaireP = mapSalaire.get(emplId);
			
			SalairePersistant salaireDB = (SalairePersistant)getSingleResult(getQuery("from SalairePersistant "
					+ "where mois=:mois "
					+ "and annee=:annee "
					+ "and opc_employe.id=:emplId")
					.setParameter("mois", month)
					.setParameter("annee", cal.get(Calendar.YEAR))
					.setParameter("emplId", emplId));
			
			if(salaireDB == null) {
				salaireP.setDate_creation(new Date());
			} else {
				salaireP.setId(salaireDB.getId());
				salaireP.setCode_func(salaireDB.getCode_func());
				salaireP.setOpc_travaux(salaireDB.getOpc_travaux());
			}
			Date dateDebut = DateUtil.stringToDate("01/"+month+"/"+cal.get(Calendar.YEAR));
			salaireP.setDate_debut(dateDebut);
			salaireP.setDate_fin(DateUtil.stringToDate(DateUtil.getMaxMonthDate(dateDebut)+"/"+month+"/"+cal.get(Calendar.YEAR)));
			salaireP.setMois(month);
			salaireP.setAnnee(cal.get(Calendar.YEAR));
			salaireP.setDate_maj(new Date());
			salaireP.setSignature(ContextAppli.getUserBean().getLogin());
			
			//
			em.merge(salaireP);
		}
	}
	
	@Override
	@Transactional
	public void cloreMois(Integer annee, Integer idxMois) {
		List<SalairePersistant> listMoisClos = getQuery("from SalairePersistant where mois=:mois and annee=:annee "
				+ "group by mois")
				.setParameter("mois", idxMois)
				.setParameter("annee", annee)
				.getResultList();
		EntityManager em = getEntityManager();
		Date date_paiement = new Date();
		//
		for (SalairePersistant salairePersistant : listMoisClos) {
			salairePersistant.setDate_paiement(date_paiement);
			em.merge(salairePersistant);
		}
	}

	@Override
	public String[][] getHistoLibelle(String tp) {
		String field = "I".equals(tp) ? "indemnite_lib" : "prime_lib";
		List<String> listData = getQuery("select distinct("+field+") "
				+ "from SalairePersistant "
				+ "where "+field+" is not null and "+field+" != '' "
				+ "order by "+field)
			.getResultList();
		
		String[][] data = null;
		if(listData.size() > 0) {
			data = new String[2][listData.size()];
			int i = 0;
			for (String st : listData) {
				data[0][i] = st;
				data[1][i] = st;
				i++;
			}
		}
		return data;
	}
}
