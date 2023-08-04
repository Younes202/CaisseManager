package appli.model.domaine.stock.dao.impl;

import java.math.BigInteger;

import javax.inject.Named;
import javax.persistence.Query;

import appli.model.domaine.stock.dao.IMouvementDao;
import appli.model.domaine.stock.persistant.MouvementPersistant;
import framework.model.util.GenericJpaDao;

@Named
public class MouvementDao extends GenericJpaDao<MouvementPersistant, Long> implements IMouvementDao{
	@Override
	public Integer max_numBl(String date) {
		Query query = getNativeQuery("select max(CAST(SUBSTR(num_bl, 11) AS UNSIGNED)) from mouvement"
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
}
