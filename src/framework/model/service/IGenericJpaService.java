package framework.model.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.Order;

import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.component.complex.table.RowBean;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.util.IGenericJpaDao;

public interface IGenericJpaService <B, I extends Serializable> {
	Query getQuery(String request);
	
	List<B> findAll();
	<T>List<T> findAll(Class<T> entity); 
	
	List<B> findAll(Order ... orders);
	<T>List<T> findAll(Class<T> entity, Order ... orders); 
	<T>List<T> findAllNoFilter(Class<T> entity, Order ... orders);
	List<?> findByCriteria(RequestTableBean cplxTable, String query);
	List<?> findByCriteria(RequestTableBean cplxTable, String query, boolean addLimit);
	
	List findByCriteriaByQueryId(RequestTableBean cplxTable, String queryId);
	List<?> findByCriteriaByQueryId(RequestTableBean cplxTable, String queryId,	boolean addLimit);
	
	B findById(I id);
	<T> T findById(Class<T> beanClass, I id);
	
	B merge(B e);
	<T>void mergeEntity(T entity);
	
	void create(B e);
	B update(B e);
	void delete(B e);
	void delete(I id);
	void delete_group(I[] ids);
	void update_group(B e, I[] ids, List<String> listModifiedFields);
	void update_rows(List<RowBean> listRows, Map<String, List<String>> mapModifiedRows);
	void create_rows(List<RowBean> listRows);
	IGenericJpaDao getGenriqueDao();
	void asyncSetServiceId(String srvJobId);
	void refreshEntity(Object e);
	void refreshEntities(Collection<?> listE);
	<T>T getOneByField(Class<T> entity, String field, Object value);
	<T>List<T> findByField(Class<T> entity, String field, Object value, Order ... orders);
	
//	public Object getSingleResult(String request, Map<String, Object> criters);
	public Object getSingleResult(Query query);
		
	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#getEntityManager()
	 */
	EntityManager getEntityManager();

	Map<String, byte[]> getDataImage(Long elementId, String type);

	<T>List<T> getListData(Class<T> persistant, String order);

	Query getNativeQuery(String request);

	<T>T getEntityByCodeFunc(String classType, String code_func);
	<T>T getEntityByCodeFunc(EntityManager em, String classType, String code_func);

	String generateCodeBarre(String type);

	void mergeDataForm(List<DataValuesPersistant> listDataValue, Long elmntId, String groupe);

	List<DataValuesPersistant> loadDataForm(Long elmntId, String groupe);

	void deleteDataForm(Long elmntId, String groupe);

	Map<String, List<ValTypeEnumPersistant>> loadDataENumForm(String groupe);

	Query getNativeQuery(EntityManager em, String request);

	Query getQuery(EntityManager em, String request);

	String getEtsCondition(String alias, boolean isNative);

	void updateRowsOrder(String[] orderArray);
}
