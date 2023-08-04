package framework.model.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.Order;

import framework.component.complex.table.RequestTableBean;

/**
 *
 */
public interface IGenericJpaDao<T, I extends Serializable> {
	Query getQuery(String request);
	Query getQuery(EntityManager em, String request);
	Query getNativeQuery(String request);
	Query getNativeQuery(EntityManager em, String query);
	List<T> findAll();
	<B>List<B> findAll(Class<B> entity);
	
	List<T> findAll(Order ... orders);
	<B>List<B> findAll(Class<B> entity, Order[] orders);

	T findById(I id);
	<B> B findById(Class<B> beanClass, I id);
	
	List<?> findByCriteria(RequestTableBean cplxTable, String query);
	List<?> findByCriteria(RequestTableBean cplxTable, String query, boolean addLimit);
	List<?> findByCriteriaQueryId(RequestTableBean cplxTable, String queryId);
	List<?> findByCriteriaQueryId(RequestTableBean cplxTable, String queryId, boolean addLimit);
	
	void create(T e);
	T update(T e);
	List<T> update_rows(List<T> entity, Map<String, List<String>> mapModifiedRows);
	void update_group(T entity, I[] ids, List<String> listModifiedFields);
	void delete(T entity);
	void delete(I id);
	void delete_group(I[] ids);
	EntityManager getEntityManager();
	void setEntityManager(EntityManager entityManager);
	Object getSingleResult(Query query);
	T update(T entity, boolean loadObject);
	boolean isNotUnique(T e, String ... columns);
	String getSocieteEtsCondition(String request, boolean isNative);
	String getEtsCondition(String alias, boolean isNative);
	void updateRowsOrder(String[] orderArray);
}
