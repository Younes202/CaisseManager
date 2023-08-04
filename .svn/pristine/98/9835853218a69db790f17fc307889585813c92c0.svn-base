package appli.model.domaine.dashboard.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.habilitation.bean.ProfileBean;
import appli.model.domaine.dashboard.service.IDashBoardAutoService;
import appli.model.domaine.stock.dao.IFamilleDao;
import framework.model.service.GenericJpaService;

@Named
public class DashBoardAutoService extends GenericJpaService<ProfileBean, Long> implements IDashBoardAutoService {
	@Inject
	IFamilleDao familleDao;
	
	/**
	 *  
	 */
	@Override
	public List<Object[]> getAssuranceProche(){
		String requete = "select "
				+ "	val.libelle as vehicule, "
				+ "veh.modele as model, "
				+ "veh.matricule as matricule, "
				+ "ass.date_fin "
				+ "from assurance ass "
				+ "inner join vehicule veh on ass.vehicule_id=veh.id " 
				+ "inner join val_type_enumere val on val.id=veh.marque_enum "
				+ "where CURRENT_DATE>=DATE_SUB(ass.date_fin, INTERVAL 15 DAY) "
				+ "group by veh.id "
				+ "order by date_fin desc"; 

		return familleDao.getNativeQuery(requete).getResultList();
	}
	
	@Override
	public List<Object[]> getVidangeProche(){
		String requete = "select "
				+ "val.libelle as vehicule, "
				+ "veh.modele as model, "
				+ "veh.matricule as matricule, "
				+ "(datediff(CURRENT_DATE, vid.date_passage)*km_jour) as km_theorique, "
				+ "(IFNULL(carb.kilometrage, 0)-vid.kilometrage) as km_theorique_carb, "
				+ "vid.date_passage "
				+ "from vehicule veh inner join val_type_enumere val on val.id=veh.marque_enum "
				+ "left join (select kilometrage, vehicule_id from carburant order by date_passage desc limit 1) carb on carb.vehicule_id=veh.id "
				+ "left join (select kilometrage, vehicule_id, date_passage from vidange order by date_passage desc limit 1) vid on vid.vehicule_id=veh.id "
				+ "where  "
				+ "vid.date_passage is not null and " 
				+ "((IFNULL(carb.kilometrage, 0)-vid.kilometrage)>=(km_vidange-IFNULL(km_alert,0)) "
				+ "or (datediff(CURRENT_DATE, vid.date_passage)*km_jour>=(km_vidange-IFNULL(km_alert,0))))";
		
		return familleDao.getNativeQuery(requete).getResultList();
	}
	
	@Override
	public List<Object[]> getVignetteProche(){
		String requete = "select "
				+ "val.libelle as vehicule, "
				+ "veh.modele as model, "
				+ "veh.matricule as matricule, "
				+ "max(vig.date_fin) as date_fin "
				+ " from vignette vig "
				+ " inner join	vehicule veh on veh.id=vig.vehicule_id "
				+ " inner join val_type_enumere val on val.id=veh.marque_enum "
				+ " where CURRENT_DATE>=DATE_SUB(vig.date_fin, INTERVAL 15 DAY) "
				+ "group by vig.vehicule_id "
				+ "order by vig.date_fin desc";
	
		return familleDao.getNativeQuery(requete).getResultList();
	}
	
	@Override
	public List<Object[]> getVisiteTechniqueProche(){
		String requete = "select "
				+ "val.libelle as vehicule, "
				+ "veh.modele as model, "
				+ "veh.matricule as matricule, "
				+ "max(vis.date_fin) as date_fin "
				+ " from visite_technique vis "
				+ " inner join	vehicule veh on veh.id=vis.vehicule_id "
				+ " inner join val_type_enumere val on val.id=veh.marque_enum "
				+ " where CURRENT_DATE>=DATE_SUB(vis.date_fin, INTERVAL 15 DAY) "
				+ " group by vis.vehicule_id "
				+ " order by vis.date_fin desc";
	
		return familleDao.getNativeQuery(requete).getResultList();
	}
	
	@Override
	public List<Object[]> getConsommation(){
		String requete = "SELECT "
				+ "val.libelle as vehicule, "
				+ "veh.modele as model, "
				+ "veh.matricule as matricule, "
				+ "concat(empl.nom, ' ', empl.prenom) as conducteur, "
				+ "veh.conso_ref as conso_ref, "
				+ "veh.conso_min as conso_min, "
				+ "veh.conso_max as conso_max, "
				+ "((100*carb.nbr_litre)/carb_info.km_parcouru) as consoCent, "
				+ "max(carb.date_passage) as dateCarb "
				+ "FROM carburant carb "
				+ "inner join (select id,  "
				+ "(case "
				+ "when (is_reference=1) then 0 "
				+ "else (kilometrage-prev_kilometrage) "
				+ "end) as km_parcouru "
				+ "from ( "
				+ "select ca2.id, ca2.nbr_litre, ca2.mtt_valide, ca2.vehicule_id, ca2.kilometrage, ca2.is_reference, "
				+ "(select kilometrage from carburant ca3 where id=(select max(id) from carburant where vehicule_id=ca2.vehicule_id and id<ca2.id)) as prev_kilometrage "
				+ "from carburant ca2 "
				+ "order by ca2.id desc "
				+ ") ff) carb_info on carb_info.id=carb.id "
				+ "inner join vehicule veh on carb.vehicule_id=veh.id "
				+ " inner join val_type_enumere val on val.id=veh.marque_enum "
				+ "inner join employe empl on empl.id=carb.employe_id "
				+ "inner join val_type_enumere valMarque on valMarque.id=veh.marque_enum "
				+ "where ((100*carb.nbr_litre)/carb_info.km_parcouru) >=veh.conso_max or ((100*carb.nbr_litre)/carb_info.km_parcouru) <=veh.conso_min "
				+ "group by carb.vehicule_id";
	
        return familleDao.getNativeQuery(requete).getResultList();
	}
	
	@Override
	public List<Object[]> getListIncidents(){
		String requete = "select "
				+ "inc.date_incident, "
				+ "val.libelle as vehicule, "
				+ "veh.modele as model, "
				+ "veh.matricule as matricule, "
				+ "concat(empl.nom,' ',empl.prenom) as conducteur "
				+ "from incident inc inner join vehicule veh on inc.vehicule_id=veh.id " 
				+ "inner join val_type_enumere val on val.id=veh.marque_enum "
				+ "inner join employe empl on inc.conducteur_id=empl.id "
				+ "order by date_incident desc LIMIT 10 OFFSET 0";
		
		return familleDao.getNativeQuery(requete).getResultList();
	}
	
}
