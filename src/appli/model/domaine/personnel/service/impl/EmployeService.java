package appli.model.domaine.personnel.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.personnel.bean.EmployeBean;
import appli.model.domaine.personnel.dao.IEmployeDao;
import appli.model.domaine.personnel.persistant.EmployePersistant;
import appli.model.domaine.personnel.persistant.paie.SalairePersistant;
import appli.model.domaine.personnel.service.IEmployeService;
import appli.model.domaine.personnel.validator.EmployeValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=EmployeValidator.class)
@Named
public class EmployeService extends GenericJpaService<EmployeBean, Long> implements IEmployeService{
	@Inject
	IEmployeDao employeDao;
	@Inject
	IEmployeService employeService;
	
	@Override
	public List<EmployePersistant> getListEmployeActifs(){
		return getQuery("from EmployePersistant emp where (emp.date_entree is null or emp.date_entree<=:currDate) "
				+ "and (emp.date_sortie is null or emp.date_sortie>:currDate) order by nom, prenom") 
				.setParameter("currDate", new Date())
				.getResultList();
	}
	
	
//	@Override
//	public List<EmployePersistant> getListEmployeActifs(String codePoste){
//		return getQuery("from EmployePersistant emp where (emp.date_entree is null or emp.date_entree<=:currDate) "
//				+ "and (emp.date_sortie is null or emp.date_sortie>:currDate) "
//				+ "and opc_poste.id is not null and opc_poste.code=:codePoste "
//				+ "order by nom, prenom") 
//				.setParameter("currDate", new Date())
//				.setParameter("codePoste", codePoste)
//				.getResultList();
//	}
	
	

	@Override
	public List<Object[]> getNomberOfApperciationAndNameAllEmp() {
	    List<EmployePersistant> listAllEmp = employeService.findAll(EmployePersistant.class);
	    List<Object[]> employeeDataList = new ArrayList<>();

	    Calendar currentDate = Calendar.getInstance();
	    int currentYear = currentDate.get(Calendar.YEAR);
	    int currentMonth = currentDate.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

	    for (EmployePersistant employePersistant : listAllEmp) {
	        if (employePersistant.getDate_entree() != null) {
	            Calendar employeeDate = Calendar.getInstance();
	            employeeDate.setTime(employePersistant.getDate_entree());
	            int employeeDay = employeeDate.get(Calendar.DAY_OF_MONTH);
	            int employeeMonth = employeeDate.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

	            int employeeYear;
	            if (employeeMonth > currentMonth || (employeeMonth == currentMonth && employeeDay > currentDate.get(Calendar.DAY_OF_MONTH))) {
	                employeeYear = currentYear - 1;
	            } else {
	                employeeYear = currentYear;
	            }

	            Calendar completeDate = Calendar.getInstance();
	            completeDate.set(Calendar.YEAR, employeeYear);
	            completeDate.set(Calendar.MONTH, employeeMonth - 1); // Calendar.MONTH is zero-based
	            completeDate.set(Calendar.DAY_OF_MONTH, employeeDay);

	            Date date = completeDate.getTime();

	            List<Object[]> resultList = getNomberOfApperciationAndNameEmp(employePersistant, date);
	            if (resultList.size() == 0) {
	                // Add zero value to employeeDataList
	                Object[] data = new Object[] { employePersistant.getId(), 0 };
	                employeeDataList.add(data);
	            } else {
	                employeeDataList.addAll(resultList);
	            }
	        }
	    }
	    
	    return employeeDataList;
	}

	@Override
	public List<Object[]> getNomberOfApperciationAndNameEmp(EmployePersistant emp, Date completeDate) {
	    String jpql = "SELECT a.opc_employe.id, COUNT(a) " +
	                  "FROM EmployeAppreciationPersistant a " +
	                  "WHERE a.type =:type AND a.opc_employe.id =:employeID AND a.date_debut BETWEEN :startDate AND :endDate " +
	                  "GROUP BY a.opc_employe.id";

	    String type = "N"; // Replace with the actual type
	    Date endDate = new Date(); // Current date
	    
	    Query query = getQuery(jpql);
	    query.setParameter("type", type);
	    query.setParameter("employeID", emp.getId());
	    query.setParameter("startDate", completeDate);
	    query.setParameter("endDate", endDate);
	    
	    List<Object[]> resultList = query.getResultList();
	    return resultList;
	}


	@Override
	public SalairePersistant getLastSalaire(Long employeId) {
		List<SalairePersistant> listSalaire = getQuery("from SalairePersistant "
				+ "where date_debut<=:currDate and (date_fin is null or date_fin >=:currDate) "
				+ "and opc_employe.id=:employeId "
				+ "order by date_debut desc")
				.setParameter("currDate", new Date())
				.setParameter("employeId", employeId) 
				.getResultList();
		
		return (listSalaire.size()>0 ? listSalaire.get(0) : null);
	}
	
//	@Override
//	public SalaireDetailPersistant getLastSalaireDet(Long employeId) {
//		List<SalaireDetailPersistant> listSalaire = getQuery("from SalaireDetailPersistant where date_debut<=:currDate and (date_fin is null or date_fin >=:currDate) "
//				+ "and opc_employe.id=:employeId "
//				+ "order by date_debut desc")
//				.setParameter("currDate", new Date())
//				.setParameter("employeId", employeId) 
//				.getResultList();
//		
//		return (listSalaire.size()>0 ? listSalaire.get(0) : null);
//	}
	
	@Override
	public EmployePersistant getEmployeByNumero(String numEmploye) {
		return (EmployePersistant) getSingleResult(getQuery("from EmployePersistant where numero=:numEmpl")
				.setParameter("numEmpl", numEmploye));
	}
	
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(Long emplId) {
		// Suppression des PointageEventPersistant
		getQuery("delete from PointageEventPersistant where opc_employe.id=:emplId")
			.setParameter("emplId", emplId)
			.executeUpdate();
		
		getQuery("delete from PointagePersistant where opc_employe.id=:emplId")
		.setParameter("emplId", emplId)
		.executeUpdate();		
		
		getEntityManager().flush();
		
		super.delete(emplId);
	}
	
	@Override
	public String generateNumero() {
		int maxNum = 0;
		Query query = getNativeQuery("select max(CAST(numero AS UNSIGNED)) from employe");
		BigInteger max_num = (BigInteger)query.getSingleResult();
		if(max_num != null){
			maxNum = max_num.intValue();
		}
		String max = "000001";
		
		if(StringUtil.isNotEmpty(maxNum)){
			max = maxNum+1+"";
		}
		while(max.length() != 6){
			max = "0"+max;
		}
		
		return max;
	}
	
	@Override
	public EmployePersistant getEmployeByCin(String cin){
		EmployePersistant emplP = (EmployePersistant) getSingleResult( getQuery("from EmployePersistant where cin=:cin ")
				.setParameter("cin", cin));

		return emplP;
	}
	
	@Override
	public List<EmployePersistant> getEmployesAutocomplete(String value) {
		String req = "from EmployePersistant where "
				+ "(is_actif is null or is_actif=1) ";
		
		if(StringUtil.isNotEmpty(value)) {
			req = req + "and "
					+ "(UPPER(nom) like :code "
					+ "or UPPER(prenom) like :code) "
					+ "or UPPER(cin) like :code) ";
		}
		Query query = getQuery(req);
		
		if(StringUtil.isNotEmpty(value)) {
			query.setParameter("code", "%"+value.toUpperCase()+"%");
		}
		return query.setMaxResults(50).getResultList();
	}	
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long employeId) {
		EmployePersistant employePersistant = findById(EmployePersistant.class, employeId);
		employePersistant.setIs_disable(BooleanUtil.isTrue(employePersistant.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(employePersistant); 
	}

}
