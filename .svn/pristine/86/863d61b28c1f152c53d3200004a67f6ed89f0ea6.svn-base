package appli.model.domaine.administration.dao;

import java.util.List;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.model.util.IGenericJpaDao;

public interface IValTypeEnumDao extends IGenericJpaDao<ValTypeEnumPersistant, Long>{

	/**
	 * @param typeId
	 * @return
	 */
	List<ValTypeEnumPersistant> getListValeursByType(String codeType);

	/**
	 * @param codeType
	 * @return
	 */
	Integer getMaxOrdreByType(Long typeId);
}
