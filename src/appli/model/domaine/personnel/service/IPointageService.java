package appli.model.domaine.personnel.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import appli.controller.domaine.personnel.bean.PointageBean;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.PointageEventPersistant;
import appli.model.domaine.personnel.persistant.paie.PointagePersistant;
import framework.model.service.IGenericJpaService;

public interface IPointageService extends IGenericJpaService<PointageBean, Long> {

	List<PointagePersistant> getListPointage(List<EmployePersistant> listEmpl, Date currentDate);

	PointageBean getPointageByTypeAndDate(Long employeId, String type, Date date);

	void updatePointeusePath(String pathDb, String ip, String port);

	void createEmployePointeuse(Long employeId, String numClinet, Date datePntg);

	void mergePointageEmploye(List<PointageEventPersistant> eppArray);

//	void loadDataPointeuse(Date currDate);

	void majPointageHoraire(Map<String, Object> mapData, Long employeId, Date currDate);

	Map<Long, String> getPointageHorairesByDate(Date currDate);

	Map<String, Object> getPointageHoraire(List<EmployePersistant> listEmpl, Date currentDate);

	Map<String, BigDecimal[]> getMapEmployeSynthese(String[] employeIds, Date dateDebut, Date dateFin);

}
