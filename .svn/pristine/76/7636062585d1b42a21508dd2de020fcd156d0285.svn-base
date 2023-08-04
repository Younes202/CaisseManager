package appli.model.domaine.stock.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import appli.model.domaine.stock.dao.IChargeDiversDao;
import appli.model.domaine.stock.persistant.ChargeDiversPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class ChargeDiversDao extends GenericJpaDao<ChargeDiversPersistant, Long> implements IChargeDiversDao{

	public ChargeDiversPersistant createCharge(ChargeDiversPersistant charge){
		charge.setId(null);
		 super.getEntityManager().persist(charge);
		 return charge;
	}
	
	
	@Override
	public Integer max_num(String date) {
		Query query = getNativeQuery("select max(CAST(SUBSTR(num_bl, 11) AS UNSIGNED)) from charge_divers"
				+ "	where num_bl like '%-%'"
				+ " and LENGTH(SUBSTR(num_bl, 11)) = 3"
				+ " and SUBSTR(num_bl, 11) is not null"
				+ " and SUBSTR(num_bl, 11) != ''"
				+ " and SUBSTR(num_bl, 4, 6) = :date").setParameter("date", date);
	BigInteger max_num = (BigInteger)query.getSingleResult();
	if(max_num != null){
		return max_num.intValue();
	} else{
		return 0;
	}
	}

	@Override
	public void update_automate(ChargeDiversPersistant cdp,Date dateDebut, Date dateFin, Integer frequence) {
		Long id = cdp.getId();
		ChargeDiversPersistant cd = findById(id);
		cdp.setDate_debut_auto(dateDebut);
		cdp.setDate_fin_auto(dateFin);
		cdp.setFrequence(frequence);
		cdp.setIs_automatique(true);
		cdp.setIs_active(true);
		update(cdp);
	}

	@Override
	public void controleAutomate(Long id) {
		ChargeDiversPersistant charges = findById(id);
		if(charges.getIs_active()){
			charges.setIs_active(false);
		}else{
			charges.setIs_active(true);
		}
		
		update(charges);
	}

	/**
	 *   retourne toutes les charges automatiques et activés
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChargeDiversPersistant> getAllTheAutomatedCharge() {
		return getQuery("from ChargeDiversPersistant where is_automatique=:auto and "
				+ "is_active =:active and (date_debut_auto is not null and date_debut_auto >=:dateJ) and (date_fin_auto is null or date_fin_auto>=:dateJ)") 
				.setParameter("auto", true)
				.setParameter("active", true)
				.setParameter("dateJ", new Date())
				.getResultList();
	}

	/**
	 * 
	 */
	@Override
	public void deleteAutomatisation(ChargeDiversPersistant cdp) {
		cdp.setIs_automatique(false);
		cdp.setDate_debut_auto(null);
		cdp.setDate_fin_auto(null);
		cdp.setIs_active(false);
		cdp.setFrequence(null);
		cdp.setFrequence_type(null);
		
		getEntityManager().merge(cdp);
	}
	
}
