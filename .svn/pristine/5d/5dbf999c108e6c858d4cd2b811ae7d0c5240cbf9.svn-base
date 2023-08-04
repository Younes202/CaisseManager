package appli.model.domaine.administration.service;

import java.util.List;

import appli.controller.domaine.administration.bean.ValTypeEnumBean;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.model.service.IGenericJpaService;

public interface IValTypeEnumService extends IGenericJpaService<ValTypeEnumBean, Long> {

	/**
	 * @param typeId
	 * @return
	 */
	List<ValTypeEnumBean> getListValeursByType(String codeType);
	
	/**
	 * @param codeType
	 * @return
	 */
	String[][] getListValeursArray(List<ValTypeEnumBean> listValeur);
	
	/**
	 * @param orderArray
	 */
	void updateRowsOrder(String[] orderArray);

	/**
	 * @param typeId
	 * @return
	 */
	Integer getMaxOrdreByType(Long typeId);

	void activerDesactiverElement(Long workIdLong);

	ValTypeEnumPersistant getValeurByCode(String codeType, String codeVal);

	ValTypeEnumPersistant getValTypeEnumByCodeValAndCodeType(String codeValEnum, String codeTypeEnum);
}
