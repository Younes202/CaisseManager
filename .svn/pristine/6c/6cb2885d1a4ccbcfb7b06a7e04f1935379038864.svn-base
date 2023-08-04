package appli.model.domaine.administration.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.model.domaine.administration.dao.IValTypeEnumDao;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import appli.model.domaine.administration.service.IValTypeEnumService;
import appli.model.domaine.administration.validator.ValTypeEnumValidator;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.service.MessageService;
import framework.model.common.util.BooleanUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;
import framework.model.service.GenericJpaService;

@WorkModelClassValidator(validator=ValTypeEnumValidator.class)
@Named
public class ValTypeEnumService extends GenericJpaService<ValTypeEnumBean, Long> implements IValTypeEnumService{
	@Inject
	private IValTypeEnumDao valTypeEnumDao;

	/* (non-Javadoc)
	 * @see org.metier.domaine.par.service.IValTypeEnumService#getListValeursByType(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ValTypeEnumBean> getListValeursByType(String codeType){
		return (List<ValTypeEnumBean>)persistantToBean(ValTypeEnumBean.class, ((IValTypeEnumDao)valTypeEnumDao).getListValeursByType(codeType));
	}

	/* (non-Javadoc)
	 * @see org.metier.domaine.par.service.IValTypeEnumService#updateRowsOrder(java.lang.String[])
	 */
	@Override
	public void updateRowsOrder(String[] orderArray){
		int orderIdx = 1;
		//
		for(String order : orderArray){
			ValTypeEnumPersistant valPersistant = valTypeEnumDao.findById(NumericUtil.toLong(order));
			valPersistant.setOrdre(orderIdx);
			valTypeEnumDao.update(valPersistant);
			orderIdx++;
		}
	}

	/* (non-Javadoc)
	 * @see org.metier.domaine.par.service.IValTypeEnumService#getMaxOrdreByType(java.lang.Long)
	 */
	@Override
	public Integer getMaxOrdreByType(Long typeId) {
		Integer maxOrder = valTypeEnumDao.getMaxOrdreByType(typeId);
		return (maxOrder == null) ? 1 : (maxOrder+1);
	}

	/* (non-Javadoc)
	 * @see org.metier.domaine.par.service.IValTypeEnumService#getListValeursArray(java.util.List)
	 */
	@Override
	public String[][] getListValeursArray(List<ValTypeEnumBean> listValeur) {
		return StringUtil.listToStringArray(listValeur, "id", "libelle");
	}
	
	@Override
	@Transactional
	public void activerDesactiverElement(Long valTypeEnumId) {
		ValTypeEnumPersistant valTypeEnumP = valTypeEnumDao.findById(valTypeEnumId);
		valTypeEnumP.setIs_disable(BooleanUtil.isTrue(valTypeEnumP.getIs_disable()) ? false : true);
		//
		getEntityManager().merge(valTypeEnumP);
	}
	
	@Transactional
	public void delete(Long id){
		try{
			super.delete(id);
		} catch (Exception e){
			MessageService.addBannerMessage("Cette valeur ne peut pas être supprimée car elle est utilisée.");
		}
	}

	@Override
	public ValTypeEnumPersistant getValeurByCode(String codeType, String codeVal) {
		return (ValTypeEnumPersistant) getSingleResult(getQuery("from ValTypeEnumPersistant where code=:codeVal and opc_typenum.code=:codeType")
				.setParameter("codeType", codeType)
				.setParameter("codeVal", codeVal));
	}
	
	@Override
	public ValTypeEnumPersistant getValTypeEnumByCodeValAndCodeType(String codeValEnum, String codeTypeEnum){
		return (ValTypeEnumPersistant) getSingleResult(getQuery("from ValTypeEnumPersistant where code=:codeVal and opc_typenum.code=:codeType")
				.setParameter("codeVal", codeValEnum)
				.setParameter("codeType", codeTypeEnum));
	}
}
