package appli.model.domaine.compta.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import appli.model.domaine.compta.dao.IExerciceDao;
import framework.model.beanContext.ExercicePersistant;
import framework.model.common.util.DateUtil;
import framework.model.common.util.DateUtil.TIME_ENUM;
import framework.model.util.GenericJpaDao;

@Named
public class ExerciceDao extends GenericJpaDao<ExercicePersistant, Long> implements IExerciceDao{
	
	@Override
	public Date getMaxDateFin() {
		Query query = getQuery("select max(e.date_fin) from ExercicePersistant e ");
		return (Date)getSingleResult(query);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ExercicePersistant getLastExercice() {
		Query query = getQuery("from ExercicePersistant e ");
		List list = query.getResultList();
		
		return (ExercicePersistant) ((list.size() > 0) ? list.get(0) : null);
	}

	@Override
	public ExercicePersistant getExerciceByDate(Date dateDebut, Date dateFin) {
		Query query = getQuery("from ExercicePersistant e "
				+ "where e.date_debut <= :date_debut and e.date_fin >= :date_fin")
							.setParameter("date_debut", dateDebut)
							.setParameter("date_fin", dateFin);
		return (ExercicePersistant)getSingleResult(query);
	}

	/* (non-Javadoc)
	 * @see org.metier.domaine.reg.dao.IExerciceDao#getExerciceSuivant(org.metier.domaine.reg.persistant.ExercicePersistant)
	 */
	@Override
	public ExercicePersistant getExerciceSuivant(ExercicePersistant e) {
		if(e == null){
			return null;
		}
		Query query = getQuery("from ExercicePersistant e "
				+ "where e.date_debut > :date_fin order by e.date_debut asc")
							.setParameter("date_fin", e.getDate_fin());
		
		List resultList = query.getResultList();
		return (ExercicePersistant) (resultList.size() > 0 ? resultList.get(0) : null);
	}

	@Override
	public ExercicePersistant getExercicePrecedent(ExercicePersistant e) {
		if(e == null || e.getDate_debut() == null){
			return null;
		}
		Calendar calendarDebut = DateUtil.getCalendar(e.getDate_debut());
		calendarDebut.add(Calendar.MONTH, -12);
		Date dateDebut = calendarDebut.getTime(); 
		Date dateFin = DateUtil.addSubstractDate(e.getDate_debut(), TIME_ENUM.DAY, -1);
		return getExerciceByDate(dateDebut, dateFin);
	}
}
