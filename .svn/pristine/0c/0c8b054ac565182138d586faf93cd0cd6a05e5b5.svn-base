package appli.model.domaine.administration.service;

import java.util.List;

import appli.controller.domaine.administration.bean.TypeEnumBean;
import framework.model.beanContext.DataFormPersistant;
import framework.model.service.IGenericJpaService;

public interface ITypeEnumService extends IGenericJpaService<TypeEnumBean, Long> {

	List<DataFormPersistant> findAllForm(String groupe);

	void mergeDataForm(List<DataFormPersistant> listDataForm);

}
