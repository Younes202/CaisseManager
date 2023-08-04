package appli.model.domaine.administration.dao.impl;

import java.util.List;

import javax.inject.Named;
import javax.persistence.Query;

import appli.model.domaine.administration.dao.IValTypeEnumDao;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.model.util.GenericJpaDao;


@Named
public class ValTypeEnumDao extends GenericJpaDao<ValTypeEnumPersistant, Long> implements IValTypeEnumDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<ValTypeEnumPersistant> getListValeursByType(String codeType){
		return getQuery("from ValTypeEnumPersistant val where val.opc_typenum.code = :codeType "
				+ "order by val.ordre asc, val.code asc")
				.setParameter("codeType", codeType)
				.getResultList();
				
	}

	@Override
	public Integer getMaxOrdreByType(Long typeId) {
		Query query = getQuery("select max(e.ordre) from ValTypeEnumPersistant e where e.opc_typenum.id = :typeId ")
				.setParameter("typeId", typeId);
		return (Integer)getSingleResult(query);
	}

}
