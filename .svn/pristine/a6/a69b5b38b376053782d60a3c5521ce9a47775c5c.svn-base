package appli.model.domaine.personnel.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.personnel.bean.PointageBean;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.PointageEventPersistant;
import appli.model.domaine.personnel.persistant.paie.PointagePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.service.IPointageService;
import framework.controller.ContextGloabalAppli;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.util.DateUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@Named
public class PointageService extends GenericJpaService<PointageBean, Long> implements IPointageService{
	
	@Inject
	private IEmployeService employeService;
	
	@Override
	public PointageBean getPointageByTypeAndDate(Long employeId, String type, Date date) {
		Calendar cal = DateUtil.getCalendar(date);
		
		return (PointageBean) ServiceUtil.persistantToBean(PointageBean.class, getSingleResult(getQuery("from PointagePersistant "
				+ "where type=:type "
				+ "and year(date_event)=:year "
				+ "and month(date_event)=:month "
				+ "and day(date_event)=:day "
				+ "and opc_employe.id=:employeId")
				.setParameter("type", type)
				.setParameter("year", cal.get(Calendar.YEAR))
				.setParameter("month", cal.get(Calendar.MONTH)+1)
				.setParameter("day", cal.get(Calendar.DAY_OF_MONTH))
				.setParameter("employeId", employeId)
			));
	}

	@Override
	public Map<String, BigDecimal[]> getMapEmployeSynthese(String[] employe_ids, Date dateDebut, Date dateFin) {
			
		Set<Long> employeIds = new HashSet<>();
		if(employe_ids != null) {
			for(String emplId : employe_ids) {
				if(StringUtil.isNotEmpty(emplId)) {
					employeIds.add(Long.valueOf(emplId));
				}
			}
		}
		
		String employeIdsReq = (employeIds.isEmpty() == false? " and opc_employe.id IN :ids " : "");
		String employeFraisIdsReq = (employeIds.isEmpty() == false? " and opc_frais.opc_employe.id IN :ids " : "");
		
		String reqSalaire = "select opc_employe.id, SUM(montant_net) from SalairePersistant where date_debut >=:dateDebut and date_fin<=:dateFin "+employeIdsReq+" group by opc_employe.id order by opc_employe.nom asc  ";
		String reqFrais = "select opc_frais.opc_employe.id, SUM(montant) from FraisDetailPersistant where date_depense >=:dateDebut and date_depense<=:dateFin "+employeFraisIdsReq+" group by opc_frais.opc_employe.id order by opc_frais.opc_employe.nom asc ";
		String reqTravail = "select opc_employe.id, SUM(montant) from PointagePersistant where date_event >=:dateDebut and date_event<=:dateFin "+employeIdsReq+" and type='tr' group by opc_employe.id order by opc_employe.nom asc ";
		String reqAvance = "select opc_employe.id, SUM(montant) from PointagePersistant where date_event >=:dateDebut and date_event<=:dateFin "+employeIdsReq+" and type='av' group by opc_employe.id order by opc_employe.nom asc ";
		String reqPrime = "select opc_employe.id, SUM(montant) from PointagePersistant where date_event >=:dateDebut and date_event<=:dateFin "+employeIdsReq+"and type='pr' group by opc_employe.id order by opc_employe.nom asc ";
		String reqPret = "select opc_employe.id, SUM(montant) from PointagePersistant where date_event >=:dateDebut and date_event<=:dateFin "+employeIdsReq+"and type='pre' group by opc_employe.id order by opc_employe.nom asc ";
		String reqConge = "select opc_employe.id, SUM(montant) from PointagePersistant where date_event >=:dateDebut and date_event<=:dateFin "+employeIdsReq+" and type='cg' group by opc_employe.id order by opc_employe.nom asc ";
		String reqRetenue = "select opc_employe.id, SUM(montant) from PointagePersistant where date_event >=:dateDebut and date_event<=:dateFin "+employeIdsReq+" and type='rt' group by opc_employe.id order by opc_employe.nom asc ";

		Map<String, BigDecimal[]> mapData = new LinkedHashMap<>();
		Map<Long, String> mapEmploye = new HashMap<>();
		List<EmployePersistant> listEmploye = employeService.findAll(EmployePersistant.class);
		for (EmployePersistant employeP : listEmploye) {
			mapEmploye.put(employeP.getId(), employeP.getNumero()+"-"+employeP.getNomPrenom());
		}
		
		setDataInReq("sal", reqSalaire, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("fra", reqFrais, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("trv", reqTravail, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("ava", reqAvance, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("pri", reqPrime, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("pre", reqPret, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("cong", reqConge, employeIds, dateDebut, dateFin, mapData, mapEmploye);
		setDataInReq("ret", reqRetenue, employeIds, dateDebut, dateFin, mapData, mapEmploye);

		return mapData;
	}
		
	public void setDataInReq(String type,  String req, Set<Long> employe_ids, 
				Date dateDebut, Date dateFin,Map<String, BigDecimal[]> mapData, Map<Long, String> mapEmploye) {
		 Query query = getQuery(req)
					.setParameter("dateDebut", dateDebut)
					.setParameter("dateFin", dateFin);
					
			if(!employe_ids.isEmpty()) {
				query.setParameter("ids", employe_ids);
			}
			List<Object[]> list = query.getResultList();
				   
			for (Object[] val : list) {
				Long employeId = (Long) val[0];
				String employe = mapEmploye.get(employeId);
				//
				BigDecimal[] dataEmpl = mapData.get(employe);
				if(dataEmpl == null) {
					dataEmpl = new BigDecimal[8];
					mapData.put(employe, dataEmpl);
				}
			
				switch (type) {
					case "sal": dataEmpl[0] = (BigDecimal) val[1];break;
					case "fra": dataEmpl[1] = (BigDecimal) val[1];break;
					case "trv": dataEmpl[2] = (BigDecimal) val[1];break;
					case "ava": dataEmpl[3] = (BigDecimal) val[1];break;
					case "pri": dataEmpl[4] = (BigDecimal) val[1];break;
					case "pre": dataEmpl[5] = (BigDecimal) val[1];break;
				    case "cong": dataEmpl[6] = (BigDecimal) val[1];break;
				    case "ret": dataEmpl[7] = (BigDecimal) val[1];break;
				}
			}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PointagePersistant> getListPointage(List<EmployePersistant> listEmpl, Date currentDate) {
		Calendar cal = DateUtil.getCalendar(currentDate);

		Set<Long> emplIds = new HashSet<>();
		for(EmployePersistant emplP : listEmpl) {
			emplIds.add(emplP.getId());
		}
		
		return getQuery("from PointagePersistant where "
				+ "year(date_event)>=:year "
				+ "and month(date_event)<=:month "
				+ "and opc_employe.id in (:emplIds) "
				+ "order by date_event asc, type")
			.setParameter("emplIds", emplIds)	
			.setParameter("year", cal.get(Calendar.YEAR))
			.setParameter("month", cal.get(Calendar.MONTH)+1)
			.getResultList();
	}

	@Override
	@Transactional
	public void updatePointeusePath(String path, String zktIp, String zktPort) {
		EtablissementPersistant restauP = (EtablissementPersistant) findById(EtablissementPersistant.class, ContextGloabalAppli.getEtablissementBean().getId());
		restauP.setPointeuse_db_path(path);
		restauP.setPointeuse_ip(zktIp);
		restauP.setPointeuse_port(zktPort);
		//
		getEntityManager().merge(restauP);
	}
	
//	@Override
//	@Transactional
//	public void loadDataPointeuse(Date currDate) {
//		EntityManager entityManager = getEntityManager();
//		Date dateSignature = new Date();
//		
//		Calendar cal = DateUtil.getCalendar(currDate);
//		int mois = cal.get(Calendar.MONTH)+1;
//		int annee = cal.get(Calendar.YEAR);
//		
//		// Mise à jour du pointage ---------------------------------
//		// Calcul durée travail
//		List<EmployePersistant> employe = employeService.getListEmployeActifs();
//		
//		// Calcul par jour
//		Map<String, BigDecimal> mapDet = new HashMap<>();
//		for (EmployePersistant employePersistant : employe) {
//			List<PointageEventPersistant> listPointageEmpl = getQuery("from PointageEventPersistant "
//					+ "where opc_employe.id=:emplId "
//					+ "and month(date_pointage)=:month "
//					+ "and year(date_pointage)=:year")
//			.setParameter("month", mois)
//			.setParameter("year", annee)
//			.getResultList();
//			
//			Date oldDate = null;
//			for (PointageEventPersistant employePointeuseP : listPointageEmpl) {
//				if(oldDate != null){
//					BigDecimal diffMinute = BigDecimalUtil.get(""+DateUtil.getDiffMinuts(employePointeuseP.getDate_pointage(), oldDate));
//					//BigDecimal duree = BigDecimalUtil.divide(diffMinute, BigDecimalUtil.get(""+60));
//					String key = employePersistant.getId()+"-"+DateUtil.dateToString(employePointeuseP.getDate_pointage(), "dd");
//					mapDet.put(key, BigDecimalUtil.add(mapDet.get(key), diffMinute));// En minute
//				}
//				oldDate = employePointeuseP.getDate_pointage();
//			}
//		}
//		
//		for(String key : mapDet.keySet()){
//			Long emplId = Long.valueOf(key.substring(0, key.indexOf("-")));
//			String day = key.substring(key.indexOf("-")+1);
//			Date dt = DateUtil.stringToDate(day+"/"+mois+"/"+annee);
//			BigDecimal dureeMinutes = mapDet.get(key);
//			
//			SalairePersistant salaireP = employeService.getLastSalaire(emplId);
//			
//			BigDecimal salaireJour = BigDecimalUtil.ZERO;
//			if(salaireP != null){
//				salaireJour = BigDecimalUtil.divide(salaireP.getMontant_net(), BigDecimalUtil.get(DateUtil.getCalendar(currDate).getActualMaximum(Calendar.DAY_OF_MONTH)));
//			}
//			
//			EmployePersistant employeP = employeService.findById(emplId);
//			
//			Query query = getQuery("from PointagePersistant where type='tr' "
//					+ "year(date_event)>=:year "
//					+ "and month(date_event)<=:month "
//					+ "and day(date_event)<=:day "
//					+ "and opc_employe.id=:emplId")
//				.setParameter("emplId", emplId)
//				.setParameter("year", cal.get(Calendar.YEAR))
//				.setParameter("month", cal.get(Calendar.MONTH)+1)
//				.setParameter("day", cal.get(Calendar.DAY_OF_MONTH));
//		
//			//
//			PointagePersistant pointageP = (PointagePersistant) getSingleResult(query);
//			if(pointageP == null){
//				pointageP = new PointagePersistant();
//				pointageP.setDate_event(dt);
//				pointageP.setType("tr");
//				pointageP.setOpc_employe(employeP);
//				pointageP.setDate_maj(dateSignature);
//				pointageP.setSignature("BACTH");
//			}
//			BigDecimal dureeHeure = BigDecimalUtil.divide(dureeMinutes, BigDecimalUtil.get(""+60));
//			pointageP.setMontant(BigDecimalUtil.multiply(salaireJour, dureeHeure));
//			pointageP.setTarif_ref(salaireJour);
//			pointageP.setDuree(dureeMinutes);
//			//
//			entityManager.merge(pointageP);
//		}
//	}
	/*
	private void checkPointeuseZktEco(Date currentDate){
		List<EmployePersistant> listEmployes = (List<EmployePersistant>) findAll(EmployePersistant.class);
		String filePath = ContextGloabalAppli.getEtablissementBean().getPointeuse_db_path(); //"C:/Program Files/ZKTeco/att2000.mdb";
		List<PointageEventPersistant> listPointeuseEmpl = new ArrayList<>();
		Connection conAdministrator = null;
		
		Calendar cal = DateUtil.getCalendar(currentDate);
		Date dateDebut = DateUtil.stringToDate("01/"+DateUtil.dateToString(currentDate, "MM/yyyy"));
		Date dateFin =DateUtil.stringToDate(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"/"+DateUtil.dateToString(currentDate, "MM/yyyy"));
		
		dateDebut = DateUtil.getStartOfDay(dateDebut);
		dateFin = DateUtil.getEndOfDay(dateFin);
		
		Map<String, EmployePersistant> mapClient = new HashMap<>();
		for(EmployePersistant emplP : listEmployes){
			if(StringUtil.isNotEmpty(emplP.getNumero())){
				mapClient.put(emplP.getNumero(), emplP);
			}
		}
		
		try{
			String strConnectionString = "";
			
			// Register driver
			Class.forName( "net.ucanaccess.jdbc.UcanaccessDriver" );
			strConnectionString = "jdbc:ucanaccess://" + filePath;
			
			// Open a connection to the database
			conAdministrator = DriverManager.getConnection(strConnectionString);
			
		      Statement stmt = conAdministrator.createStatement();
	
		      String sql = "SELECT CHECKTIME, ui.badgenumber FROM CHECKINOUT cho inner join USERINFO ui on cho.userid=ui.userid "
		      			+ "where CHECKTIME "
			      		+ "Between #"+DateUtil.dateToString(dateDebut, "yyyy-MM-dd HH:mm:ss")+"# "
			      		+ "and #"+DateUtil.dateToString(dateFin, "yyyy-MM-dd HH:mm:ss")+"# "
			      		+ "order by CHECKTIME desc";
		      
		      ResultSet rs = stmt.executeQuery(sql);
		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         String badgeNum  = rs.getString("badgenumber");
		         String CHECKTIME  = rs.getString("CHECKTIME");
		         
		         EmployePersistant currEmpl = mapClient.get(badgeNum);
		         
		         if(currEmpl != null){
			         PointageEventPersistant ep = new PointageEventPersistant();
			         ep.setDate_pointage(DateUtil.stringToDate(CHECKTIME, "yyyy-MM-dd HH:mm"));//07/05/2018 18:59:07    yyyy-MM-dd HH:mm:ss
			         ep.setNumero_client(badgeNum);
			         ep.setOpc_employe(currEmpl);
			         ep.setDate_creation(new Date());
			         //
			         listPointeuseEmpl.add(ep);
		         }
		      }
		      rs.close();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				conAdministrator.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		EntityManager entityManager = getEntityManager();
		for (PointageEventPersistant employePointeuseP : listPointeuseEmpl) {
			List listData = getQuery("from PointageEventPersistant where opc_employe.id=:emplId and date_pointage=:dt")
				.setParameter("emplId", employePointeuseP.getOpc_employe().getId())
				.setParameter("dt", employePointeuseP.getDate_pointage())
				.getResultList();
			
			if(listData.size() == 0){
				entityManager.merge(employePointeuseP);
			}
		}
		entityManager.flush();
	}*/

	@Override
	public Map<String, Object> getPointageHoraire(List<EmployePersistant> listEmpl, Date currentDate) {
		Calendar cal = DateUtil.getCalendar(currentDate);
		
		Set<Long> emplIds = new HashSet<>();
		for(EmployePersistant emplP : listEmpl) {
			emplIds.add(emplP.getId());
		}
		
		List<PointageEventPersistant> listData = getQuery("from PointageEventPersistant where year(date_pointage)=:year "
				+ "and month(date_pointage)=:month "
				+ "and opc_employe.id in (:emplIds) "
				+ "order by opc_employe.numero, date_pointage")
				.setParameter("year", cal.get(Calendar.YEAR))
				.setParameter("month", cal.get(Calendar.MONTH)+1)
				.setParameter("emplIds", emplIds)
				.getResultList();

		// Map de : Employe ==> Jour : pointage de la journée
		Map<String, Map<String, List<PointageEventPersistant>>> mapData = new HashMap<>();
		
		for (EmployePersistant opc_employe : listEmpl) {
			String key = opc_employe.getNumero()+"-"+opc_employe.getNom()+" "+StringUtil.getValueOrEmpty(opc_employe.getPrenom());
			Map<String, List<PointageEventPersistant>> mapDetail = new HashMap<String, List<PointageEventPersistant>>();
			mapData.put(key, mapDetail);
		}
		
		for (PointageEventPersistant employePointeuseP : listData) {
			EmployePersistant opc_employe = employePointeuseP.getOpc_employe();
			String key = opc_employe.getNumero()+"-"+opc_employe.getNom()+" "+StringUtil.getValueOrEmpty(opc_employe.getPrenom());
			Map<String, List<PointageEventPersistant>> mapDetail = mapData.get(key);
			 
			 if(mapDetail == null){
				 mapDetail = new HashMap<String, List<PointageEventPersistant>>();
				 mapData.put(key, mapDetail);
			 }
			 
			 String detayKey = DateUtil.dateToString(employePointeuseP.getDate_pointage(), "dd");
			 List<PointageEventPersistant> listDetail = mapDetail.get(detayKey);
			 
			 if(listDetail == null){
				 listDetail = new ArrayList<>();
				 mapDetail.put(detayKey, listDetail);
			 }
			 listDetail.add(employePointeuseP);
		}

		// Map de : Employé ==> Jour : Total temps
		Map<String, Map<String, Long>> mapDataPeriode = new HashMap<>();
		
		for(String emplStr : mapData.keySet()){ 
			Map<String, List<PointageEventPersistant>> mapDetail = mapData.get(emplStr);
			
			Map<String, Long> mapDetailPeriode = mapDataPeriode.get(emplStr);
			if(mapDetailPeriode == null){
				mapDetailPeriode = new HashMap<String, Long>();
				mapDataPeriode.put(emplStr, mapDetailPeriode);
			 }
			
			for(String dt : mapDetail.keySet()){ 
				Long periode = mapDetailPeriode.get(dt);
				if(periode == null){
					mapDetailPeriode.put(dt, Long.valueOf("0"));
				}
				
				List<PointageEventPersistant> listDetail = mapDetail.get(dt);
				
				// On prend par deux dates
				Date oldDate = null;
				for (Iterator<PointageEventPersistant> iterator = listDetail.iterator(); iterator.hasNext();) {
					PointageEventPersistant employePersistant = iterator.next();
					if(oldDate != null) {
						int diffMinute = DateUtil.getDiffMinuts(oldDate, employePersistant.getDate_pointage());
						if(diffMinute < 5) { //5min
							continue;
						}
						mapDetailPeriode.put(dt, mapDetailPeriode.get(dt)+diffMinute);
					 }
					// Sauter une boucle
					if(oldDate != null && iterator.hasNext()){
						oldDate = iterator.next().getDate_pointage();	
					} else {
						oldDate = employePersistant.getDate_pointage();
					}
				}
			}
		}
		
		Map<String, Object> mapDet = new HashMap<>(); 
		mapDet.put("mapHorairePointage", mapData); 
		mapDet.put("mapTotalHorairePointage", mapDataPeriode);
		
		return mapDet;
	}
	
	@Override
	@Transactional
	public void majPointageHoraire(Map<String, Object> mapData, 
				Long employeId, 
				Date currDate) {
		
		Calendar cal = DateUtil.getCalendar(currDate);
		// Purge
		getQuery("delete from PointageEventPersistant where year(date_pointage)=:year "
				+ "and month(date_pointage)=:month and day(date_pointage)=:day")
				.setParameter("year", cal.get(Calendar.YEAR))
				.setParameter("month", cal.get(Calendar.MONTH)+1)
				.setParameter("day", cal.get(Calendar.DAY_OF_MONTH))
				.executeUpdate();		
		
		EmployePersistant employeP = employeService.findById(EmployePersistant.class, employeId);
		
		BigDecimal tarif = employeP.getTarif();
		EntityManager entityManager = getEntityManager();
		for (String pointageId : mapData.keySet()) {
			String str_heure = (String) mapData.get(pointageId);
			if(StringUtil.isEmpty(str_heure)) {
				continue;
			}
			
			int heure_H = Integer.valueOf(str_heure.substring(0, 2));
			int heure_M = Integer.valueOf(str_heure.substring(3));
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), heure_H, heure_M);
			
    		PointageEventPersistant pointeuseEmplP = new PointageEventPersistant();
			pointeuseEmplP.setNumero_client(employeP.getNumero());
			pointeuseEmplP.setOpc_employe(employeP);
			pointeuseEmplP.setTarif_ref(tarif);
			
			pointeuseEmplP.setDate_creation(new Date());			
			pointeuseEmplP.setDate_maj(new Date());
			pointeuseEmplP.setDate_pointage(cal.getTime());
			//
			entityManager.merge(pointeuseEmplP);
		}
	}
	
	/*
	@Override
	@Transactional
	public void majDatePointage(Map<String, Object> mapData, 
				Long employeId, 
				Date currDate) {

		PointageBean pointageB = getPointageByTypeAndDate(employeP.getId(), "tr", currDate);
		String key = employeP.getNumero()+"-"+employeP.getNom()+" "+StringUtil.getValueOrEmpty(employeP.getPrenom());
		
		Map<String, Object> mapDetPointage= getPointageHoraire(listEmployePagger, currEmpl, currDt);
		mapDetPointage.get("mapHorairePointage");
		mapDetPointage.get("mapTotalHorairePointage");
		
		
//		Map[] tableauMap = getListPointageEmploye(employeP.getId(), currDate, startIdx);
//		Map<String, Map<String, Long>> mapDataPeriode = tableauMap[1];
		
//		Map<String, Long> mapDetail = mapDataPeriode.get(key);
		
		if(mapDetail != null) {
			Calendar cal = DateUtil.getCalendar(currDate);
			String day = (cal.get(Calendar.DAY_OF_MONTH)<10 ? "0"+cal.get(Calendar.DAY_OF_MONTH) : ""+cal.get(Calendar.DAY_OF_MONTH));
//			Long secondes = mapDetail.get(day);
			
			if(secondes > 0) {
				BigDecimal duree = null;
				float minutes = secondes.floatValue() / 60; //en minute
				float heures = secondes.floatValue() / 60 / 60; //en heure
				if(employeP.getMode_paie().equals("J")) { // Jour
					BigDecimal nbrHourHalfDay = BigDecimalUtil.divide(employeP.getHeureParJour(), BigDecimalUtil.get(2));
					BigDecimal halfDays = null;
					while(nbrHourHalfDay.compareTo(BigDecimal.valueOf(heures)) == -1 || nbrHourHalfDay.compareTo(BigDecimal.valueOf(heures)) == 0) {
						halfDays = BigDecimalUtil.add(halfDays, BigDecimalUtil.get("0.5"));
						heures = (float) (heures - nbrHourHalfDay.doubleValue());
					}
					
					duree = halfDays;
				} else {
					duree = BigDecimal.valueOf(heures);
				}
				
				if(duree != null && duree.compareTo(BigDecimalUtil.ZERO) == 1) {
					if(pointageB == null) {
						pointageB = new PointageBean();
						pointageB.setDate_event(currDate);
						pointageB.setOpc_employe(employeP);
						pointageB.setType("tr");
					}
					pointageB.setDuree(duree);
					pointageB.setTarif_ref(employeP.getTarif());
					if(pointageB.getId() == null) {
						create(pointageB);
					} else {
						update(pointageB);
					}
				}
			}
		} else {
			delete(pointageB.getId());
		}
	}*/
	
	@Override
	public Map<Long, String> getPointageHorairesByDate(Date currDate) {
		Map<Long, String> mapData = new LinkedHashMap<>();
		Calendar cal = DateUtil.getCalendar(currDate);

		List<PointageEventPersistant> listEvents = getQuery("from PointageEventPersistant "
				+ "where year(date_pointage)=:year "
				+ "and month(date_pointage)=:month "
				+ "and day(date_pointage)=:day "
				+ "order by date_pointage asc")
				.setParameter("year", cal.get(Calendar.YEAR))
				.setParameter("month", cal.get(Calendar.MONTH)+1)
				.setParameter("day", cal.get(Calendar.DAY_OF_MONTH))
				.getResultList();
		
		for (PointageEventPersistant pointageEventP : listEvents) {
			mapData.put(pointageEventP.getId(), DateUtil.dateToString(pointageEventP.getDate_pointage(), "HH:mm"));	
		}
		
		return mapData;
	}
	
	@Override
	public void createEmployePointeuse(Long employeId, String numClinet, Date datePntg) {
		EmployePersistant employeP = (EmployePersistant) findById(EmployePersistant.class, employeId);
		BigDecimal tarif = employeP.getTarif();
		
		PointageEventPersistant emplPnt = new PointageEventPersistant();
		emplPnt.setDate_pointage(datePntg);
		emplPnt.setDate_creation(new Date());
		emplPnt.setNumero_client(numClinet);
		emplPnt.setOpc_employe(employeP);
		emplPnt.setTarif_ref(tarif);
		
		getEntityManager().merge(emplPnt);
	}

	
	@Override
	@Transactional
	public void mergePointageEmploye(List<PointageEventPersistant> eppArray) {
		if(eppArray == null) {
			return;
		}
		
		EntityManager entityManager = getEntityManager();
//		Date datePointage = null;
		
		for (PointageEventPersistant employeP : eppArray) {
			if(employeP.getNumero_client() != null) {
				EmployePersistant empl = (EmployePersistant) getSingleResult(getQuery("from EmployePersistant "
						+ "where cast(numero as int)=:numero")
						.setParameter("numero", Integer.valueOf(employeP.getNumero_client())));
				
				if(empl == null) {
					continue;
				}
				
				BigDecimal tarif = empl.getTarif();
				Date dateMax = (Date)getSingleResult(getQuery("select max(date_pointage) from PointageEventPersistant "
						+ "where numero_client=:numero")
						.setParameter("numero", employeP.getNumero_client()));
				
				if(dateMax != null && DateUtil.getDiffMinuts(dateMax, employeP.getDate_pointage()) < 3) {// Au moins 3 minutes d'écart
					continue;
				}
				
//				datePointage = employeP.getDate_pointage();
				
				PointageEventPersistant ep = new PointageEventPersistant();
				ep.setNumero_client(employeP.getNumero_client());
				ep.setDate_creation(new Date());
				ep.setDate_pointage(employeP.getDate_pointage());
				ep.setTarif_ref(tarif);
				ep.setOpc_employe(empl);
				ep.setOpc_etablissement(ContextGloabalAppli.getEtablissementBean());
				
				entityManager.merge(ep);
				entityManager.flush();
			}
		}
		
		//loadDataPointeuse(datePointage);
	}
}
