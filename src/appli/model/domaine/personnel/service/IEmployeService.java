package appli.model.domaine.personnel.service;

import java.util.Date;
import java.util.List;

import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.model.domaine.caisse.persistant.LivreurPositionPersistant;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import framework.model.service.IGenericJpaService;

public interface IEmployeService extends IGenericJpaService<EmployeBean, Long> {

	SalairePersistant getLastSalaire(Long employeId);

	List<EmployePersistant> getListEmployeActifs();

//	List<EmployePersistant> getListEmployeActifs(String codePoste);

	String generateNumero();

	EmployePersistant getEmployeByNumero(String numEmploye);

	EmployePersistant getEmployeByCin(String employe_cin);

	List<EmployePersistant> getEmployesAutocomplete(String value);

	void activerDesactiverElement(Long employeId);

	List<Object[]> getNomberOfApperciationAndNameAllEmp();

	List<Object[]> getNomberOfApperciationAndNameEmp(EmployePersistant emp, Date completeDate);


}
