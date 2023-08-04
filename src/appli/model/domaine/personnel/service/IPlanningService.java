package appli.model.domaine.personnel.service;

import java.util.List;

import appli.controller.domaine.personnel.bean.PlanningBean;
import appli.model.domaine.personnel.persistant.PlanningPersistant;
import framework.component.text.Date;
import framework.model.service.IGenericJpaService;

public interface IPlanningService extends IGenericJpaService<PlanningBean, Long> {

	void addMail(PlanningBean planningBean);

	List<PlanningPersistant> getResaPostDate();

	List<String> checkTableReserved(java.util.Date date_db, java.util.Date date_fn);


}
