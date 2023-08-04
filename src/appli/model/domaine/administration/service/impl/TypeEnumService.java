package appli.model.domaine.administration.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.TypeEnumBean;
import appli.model.domaine.administration.service.ITypeEnumService;
import appli.model.domaine.administration.validator.TypeEnumValidator;
import framework.controller.Context;
import framework.model.beanContext.DataFormPersistant;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=TypeEnumValidator.class)
@Named
public class TypeEnumService extends GenericJpaService<TypeEnumBean, Long> implements ITypeEnumService{

	@Override
	public List<DataFormPersistant> findAllForm(String groupe) {
		List<DataFormPersistant> listDataForm = getQuery("from DataFormPersistant where data_group=:data_group "
				+ "order by data_order")
				.setParameter("data_group", groupe)
			.getResultList();
		
		return listDataForm;
	}

	@Override
	@Transactional
	public void mergeDataForm(List<DataFormPersistant> listDataForm) {
		EntityManager em = getEntityManager();
		String userLogin = Context.getUserLogin();
		
		List<Long> ids = new ArrayList<>();
		for (DataFormPersistant dataFormP : listDataForm) {
			if(dataFormP.getId() == null){
				continue;
			}
			ids.add(dataFormP.getId());			
		}
		// Supprimer les écrats
		if(ids.size() > 0){
			getQuery("delete from DataValuesPersistant where opc_data_form.id not in (:formIds) and data_group=:group")
				.setParameter("formIds", ids)
				.setParameter("group", listDataForm.get(0).getData_group())
				.executeUpdate();
			em.flush();
			getQuery("delete from DataFormPersistant where id not in (:formIds) and data_group=:group")
				.setParameter("formIds", ids)
				.setParameter("group", listDataForm.get(0).getData_group())
				.executeUpdate();
			em.flush();
		}
		//
		for (DataFormPersistant dataFormP : listDataForm) {
			if(StringUtil.isEmpty(dataFormP.getData_type())){
				dataFormP.setData_type("STRING");			
			}
			if(dataFormP.getId() != null){
			} else{
				dataFormP.setData_code(""+System.currentTimeMillis());
				dataFormP.setSignature(userLogin);
			}
			//
			em.merge(dataFormP);
		}
	}
}
