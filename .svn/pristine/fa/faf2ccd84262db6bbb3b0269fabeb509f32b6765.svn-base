package appli.model.domaine.administration.validator;

import javax.inject.Inject;
import javax.inject.Named;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.model.domaine.administration.dao.IValTypeEnumDao;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.model.common.service.MessageService;

@Named
public class ValTypeEnumValidator {
	@Inject
	private IValTypeEnumDao valTypeEnumDao;

	/* (non-Javadoc)
	 * @see framework.model.service.commun.validator.ActionValidator#validate(java.lang.Object, java.lang.String)
	 */
	public void updateCreateValidator(ValTypeEnumBean valTypeEnumBean) {
		ValTypeEnumPersistant valPersistant = (ValTypeEnumPersistant) valTypeEnumDao.getSingleResult(valTypeEnumDao.getQuery("from ValTypeEnumPersistant where code=:code and opc_typenum.id=:type_id")
				.setParameter("code", valTypeEnumBean.getCode())
				.setParameter("type_id", valTypeEnumBean.getOpc_typenum().getId()));
		
		//
		valTypeEnumBean.setType_enum_id(valTypeEnumBean.getOpc_typenum().getId());
		//
		if(valPersistant != null && (valTypeEnumBean.getId() == null || !valTypeEnumBean.getId().equals(valPersistant.getId()))){
			MessageService.addFieldMessageKey("valTypeEnum.code", "valTypeEnum.code.unique.msg");
		}
		
		valPersistant = (ValTypeEnumPersistant) valTypeEnumDao.getSingleResult(valTypeEnumDao.getQuery("from ValTypeEnumPersistant where libelle=:libelle and opc_typenum.id=:type_id")
				.setParameter("libelle", valTypeEnumBean.getLibelle())
				.setParameter("type_id", valTypeEnumBean.getOpc_typenum().getId()));
		if(valPersistant != null && (valTypeEnumBean.getId() == null || !valTypeEnumBean.getId().equals(valPersistant.getId()))){
			MessageService.addFieldMessageKey("valTypeEnum.libelle", "valTypeEnum.libelle.unique.msg");
		}
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void update(ValTypeEnumBean valTypeEnumBean) {
		updateCreateValidator(valTypeEnumBean);
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void create(ValTypeEnumBean valTypeEnumBean){
		updateCreateValidator(valTypeEnumBean);
	}

	/**
	 * @param viewBean
	 * @throws Exception
	 */
	public void delete(Long id){

	}
	
	/**
	 * @param ids
	 */
	public void delete_group(Long[] ids) {
		for(Long id : ids){
			delete(id);
		}
	}

}
