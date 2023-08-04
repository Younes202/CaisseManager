package framework.model.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.query.internal.QueryImpl;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.component.complex.table.RequestTableBean;
import framework.controller.Context;
import framework.model.beanContext.EtablissementPersistant;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.service.MessageService;
import framework.model.common.util.ControllerBeanUtil;
import framework.model.common.util.EncryptionUtil;
import framework.model.common.util.NumericUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.SqlParser;
import framework.model.common.util.StringUtil;
import framework.model.util.audit.ReplicationGenerationEventListener;

/**
 *
 */
@Transactional
@Named
public abstract class GenericJpaDao<T, I extends Serializable> implements IGenericJpaDao<T, I> {
	// For Spring
	@PersistenceContext(/*type=PersistenceContextType.EXTENDED*/)
	private EntityManager em;
	
	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#getEntityManager()
	 */
	public EntityManager getEntityManager(){
		return em;
	}
	@Override
	public void setEntityManager(EntityManager em){
		this.em = em;
	}

	public Query getQueryById(String queryId){
		String query = getQueryString(queryId);
		return getQuery(query);
	}
//	@Override
//	public Query getSocEtsFilterParam(Query query){
//		if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
//			return query;
//		}
//		
//		EtablissementPersistant etsP = (EtablissementPersistant)MessageService.getGlobalMap().get("GLOBAL_ETABLISSEMENT");
//		String etsId = (String) MessageService.getGlobalMap().get("CURRENT_ETS");
//		String[] socIds = (String[]) MessageService.getGlobalMap().get("CURRENT_SOC");
		
//		boolean isSocFilter = socIds != null && socIds.length > 0;
//		boolean isEtsFilter = etsId != null;// && etsIds.length > 0;
		
//		if(isEtsFilter){
//			List<Long> listIds = new ArrayList<>();
//			for(String ets : etsIds){
//				listIds.add(etsP.getId());
//			}
//			query.setParameter("etsId", etsP.getId());
//		} 
//		else if(isSocFilter){
//			List<Long> listIds = new ArrayList<>();
//			for(String soc : socIds){
//				listIds.add(Long.valueOf(soc));
//			}
//			query.setParameter("socIds", listIds);
//		} else{
//			AbonnePersistant abonneP = ContextAppli.getAbonneBean();
//			query.setParameter("abnId", abonneP.getId());
//		}
		
//		return query;
//	}

	
	@Override
	public String getEtsCondition(String alias, boolean isNative){
		if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
			return "";
		}
		
		alias= (StringUtil.isEmpty(alias) ? "":alias+".");
		
		if(isNative){
			return " and "+alias+"etablissement_id="+ContextAppli.getEtablissementBean().getId() + " ";
		} else{
			return " and "+alias+"opc_etablissement.id="+ContextAppli.getEtablissementBean().getId() + " ";
		}
	}
	
	@Override
	public String getSocieteEtsCondition(String request, boolean isNative){
		if(!ReplicationGenerationEventListener._IS_CLOUD_SYNCHRO_INSTANCE){
			return request;
		}
		EtablissementPersistant etablissementBean = ContextAppli.getEtablissementBean();
		if(etablissementBean == null) {
			throw new RuntimeException("Etablissement non trouvé dans la session.");
		}
		
		if(request.toLowerCase().indexOf("insert") != -1){
			return request;
		}
		if(request.toLowerCase().indexOf("etablissement_id") != -1 || request.toLowerCase().indexOf("opc_etablissement.id") != -1){
			return request;
		}
		if(MessageService.getGlobalMap().get("NO_ETS") != null) {
			return request;
		}
		
		String finalReq = "";
		String alias = "";
		int idxOrder = request.toLowerCase().indexOf("order");
		int idxGrp = request.toLowerCase().indexOf("group");
		String fromStr = "";
		
		String posWhereStr = "";
		if(request.toLowerCase().indexOf("where") == -1){
			if(idxOrder != -1){
				fromStr = request.substring(0, idxOrder);
				posWhereStr = request.substring(idxOrder);
			} else if(idxGrp != -1){
				fromStr = request.substring(0, idxGrp);
				posWhereStr = request.substring(idxGrp);
			} else{
				fromStr = request;
			}
			request = fromStr + " where 1=1 "+posWhereStr;
		}
		
		int idxWhere = request.toLowerCase().lastIndexOf("where");
		fromStr = request.substring(0, idxWhere);
		
		if(fromStr.indexOf(".") != -1){
			alias = (fromStr.indexOf(" ") != -1 ? fromStr.substring(fromStr.indexOf(" "), fromStr.indexOf("."))+"." : "").trim();
			if(alias.indexOf("(") != -1){
				alias = alias.substring(alias.lastIndexOf("(")+1);
			}
		} else if(fromStr.trim().indexOf(" ", fromStr.trim().indexOf("from")+5) != -1){
			alias = (fromStr.indexOf(" ") != -1 ? fromStr.trim().substring(fromStr.trim().lastIndexOf(" "))+"." : "").trim();
		}
		
		if(alias.toLowerCase().indexOf("persistant") != -1) {
			alias = "";
		}
		
		posWhereStr = request.substring(idxWhere+5);
		
		if(StringUtil.isNotEmpty(posWhereStr)){
			posWhereStr = " and "+posWhereStr;
		}

		if(alias != null && alias.indexOf("distinct") != -1) {
			alias = alias.replace("distinct", "");
		}
		
		boolean isView = fromStr.indexOf("View") != -1;
		if(isNative || isView){
			finalReq = fromStr + " where "+alias+"etablissement_id="+etablissementBean.getId() + posWhereStr;
		} else{
			finalReq = fromStr + " where "+alias+"opc_etablissement.id="+etablissementBean.getId() + posWhereStr;
		}
		
		return finalReq;
	}
	
	/**
	 * @param em
	 * @param request
	 * @param isNative
	 * @return
	 */
	private Query  getSocieteEtsConditionQuery(EntityManager em, String request, boolean isNative){
		request = getSocieteEtsCondition(request, isNative);
		if(isNative){
			return em.createNativeQuery(request);
		} else{
			return em.createQuery(request);
		}
	}
	
	@Override
	public Query getQuery(String query){
		return getQuery(getEntityManager(), query);
	}
	@Override
	public Query getQuery(EntityManager em, String request){
		return getSocieteEtsConditionQuery(em, request, false);
	}
	@Override
	public Query getNativeQuery(String query){
		return getNativeQuery(getEntityManager(), query);
	}
	@Override
	public Query getNativeQuery(EntityManager em, String request){
		return getSocieteEtsConditionQuery(em, request, true);
	}
	
	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return (List<T>) findAll(ServiceUtil.getEntityType(this));
	}
	
	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> findAll(Class entityClass) {
//		Session session = getHibernateSession();
//		Criteria crit1 = session.createCriteria(entityClass);
//		crit1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        //
//		return crit1.list();
		String req = "from "+entityClass.getSimpleName();
		return getSocieteEtsConditionQuery(em, req, false).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Order ... orders) {
		return (List<T>) findAll(ServiceUtil.getEntityType(this), orders);
	}
	
	@Override
	public List<?> findAll(Class entity, Order ... orders) {
//		Session session = getHibernateSession();
//		Criteria crit1 = session.createCriteria(entity);
//        crit1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        
//        if(orders != null){
//        	for(Order order : orders){
//        		crit1.addOrder(order);
//        	}
//        }
//        //
//		List<?> data = crit1.list();
//
//		return data;
		
		String orderStr = "";
		if(orders != null){
			for (Order order : orders) {
				orderStr += order.getPropertyName()+" "+(order.isAscending() ? "asc":"desc")+",";
			}
			orderStr = orderStr.substring(0, orderStr.length()-1);
			orderStr = " order by " + orderStr;
		}
		
		String req = "from "+entity.getSimpleName() + orderStr;
		return getSocieteEtsConditionQuery(em, req, false).getResultList();
	}

	@Override
	public <B> B findById(Class<B> entity, I id) {
		return getEntityManager().find(entity, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(I id) {
		return (T) findById((Class<T>)ServiceUtil.getEntityType(this), id);
	}

	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#update(java.lang.Object)
	 */
	@Override
	public T update(T entity) {
		return update(entity, true);
	}
	
	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#create(java.lang.Object)
	 */
	@Override
	public void create(T entity) { 
		try{
			Object persistant = ServiceUtil.getEntityType(this).newInstance();
			// Copy properties
			PropertyUtils.copyProperties(persistant, entity);
			// Id to null
			ServiceUtil.setIdPersistantToNull(persistant);
			// Init user and updated date
			ReflectUtil.setProperty(persistant, ProjectConstante.DATE_MAJ, new Date());
			ReflectUtil.setProperty(persistant, ProjectConstante.DATE_CREATION, new Date());
			String userName = Context.getUserLogin();
			if(userName != null) {
				ReflectUtil.setProperty(persistant, ProjectConstante.SIGNATURE, userName);
			}
			if(ContextAppli.getEtablissementBean() != null) {
				ReflectUtil.setProperty(persistant, ProjectConstante.ETABLISSEMENT, ContextAppli.getEtablissementBean());
			}
			//
			EntityManager entityManager = getEntityManager();
			entityManager.persist(persistant);
			// Flush
			entityManager.flush();
			// Copy new persistant
			PropertyUtils.copyProperties(entity, persistant);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T update(T entity, boolean loadEntityFromDB) {
		Object dbEntitiy = entity;
		String userName = Context.getUserLogin();
		Object dateCreation = null;
		Object codeFunc = null;
		//
		EntityManager entityManager = getEntityManager(); 
		try {
			Object persistant = ServiceUtil.getEntityType(this).newInstance();
			// Copy properties
			PropertyUtils.copyProperties(persistant, entity); 

			if(loadEntityFromDB){
				Session session = getHibernateSession(entityManager);
				// Load persistant from database
				Long idPersistant = ServiceUtil.getIdPersistantValue(entity);
				if(idPersistant != null){
					dbEntitiy = session.get(ServiceUtil.getEntityType(this), idPersistant);
					// Données immuables
					dateCreation = ReflectUtil.getObjectPropertieValue(dbEntitiy, ProjectConstante.DATE_CREATION);
					codeFunc = ReflectUtil.getObjectPropertieValue(dbEntitiy, "code_func");
					// Copy properties
					PropertyUtils.copyProperties(dbEntitiy, entity);
				} else{
					dbEntitiy = ServiceUtil.getEntityType(this).newInstance();
					// Copy properties
					PropertyUtils.copyProperties(dbEntitiy, entity);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// restaurer les infos non modifiables
		ReflectUtil.setProperty(dbEntitiy, ProjectConstante.DATE_CREATION,  dateCreation);
		ReflectUtil.setProperty(dbEntitiy, "code_func",  codeFunc);
		
		if(ContextAppli.getEtablissementBean() != null) {
			ReflectUtil.setProperty(dbEntitiy, ProjectConstante.ETABLISSEMENT, ContextAppli.getEtablissementBean());
		}
		// Init user and updated date
		ReflectUtil.setProperty(dbEntitiy, ProjectConstante.DATE_MAJ, new Date());
		ReflectUtil.setProperty(dbEntitiy, ProjectConstante.SIGNATURE, userName);
		// Update
		entity = entityManager.merge((T)dbEntitiy);
		// Fush all updaties
		entityManager.flush();

		return entity;
	}

	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#update(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> update_rows(List<T> listEntity, Map<String, List<String>> mapModifiedRows) {
		Session session = getHibernateSession();

		for(T entity : listEntity){
			// Load persistant from database
			Long idPersistant = ServiceUtil.getIdPersistantValue(entity);
			List<String> listFields = mapModifiedRows.get(""+idPersistant);
			//
			if((listFields != null) && (listFields.size() > 0)){
				// Load persistant from data base
				Object dbEntitiy = session.get(ServiceUtil.getEntityType(this), idPersistant);
				//
				for(String fieldName : listFields){
					fieldName = fieldName.replaceAll((ControllerBeanUtil.getAliasBeanByObject(entity) + "."), "");
					Object value = ReflectUtil.getObjectPropertieValue(entity, fieldName);
					ReflectUtil.setProperty(dbEntitiy, fieldName, value);
				}
				// Update
				entity = getEntityManager().merge((T)dbEntitiy);
				// Fush all updaties
				getEntityManager().flush();
			}
		}

		return listEntity;
	}
	
	private Session getHibernateSession() {
		return getHibernateSession(getEntityManager());
	}
	private Session getHibernateSession(EntityManager entityManager) {
		Session session = (Session) entityManager.getDelegate();
		return session;
	}

	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#update_group(java.lang.Object, java.lang.Long[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update_group(T entity, I[] ids, List<String> listModifiedFields) {
		if((listModifiedFields != null) && (listModifiedFields.size() > 0)){
			Session session = getHibernateSession();
			Long[] longIds = (Long[])ids;
			//
			for(Long id : longIds){
				if(id != null){
					Object dbEntitiy = session.get(ServiceUtil.getEntityType(this), id);
					//
					for(String fieldName : listModifiedFields){
						fieldName = fieldName.replaceAll((ControllerBeanUtil.getAliasBeanByObject(entity) + "."), "");
						Object value = ReflectUtil.getObjectPropertieValue(entity, fieldName);
						//
						ReflectUtil.setProperty(dbEntitiy, fieldName, value);
					}
					// Update
					getEntityManager().merge((T)dbEntitiy);
					// Fush all updaties
					getEntityManager().flush();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(T entity) {
		getEntityManager().remove(entity);
		getEntityManager().flush();
	}

	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#delete(java.io.Serializable)
	 */
	@Override
	public void delete(I id) {
		T entity = findById(id);
		delete(entity);
		getEntityManager().flush();
	}

	/* (non-Javadoc)
	 * @see model.dao.IGenericDao#delete_group(I[])
	 */
	@Override
	public void delete_group(I[] ids) {
		for(I id : ids){
			delete(findById(id));
			getEntityManager().flush();
		}
	}

	/**
	 * @param cplxBean
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private RequestTableBean executeQuerySize(RequestTableBean cplxBean, Map<String, Object> fullCriterion, String finalRequest){
		finalRequest = getSocieteEtsCondition(finalRequest, false);
		
		String finalSqlRequest = null;
		org.hibernate.Session session = getHibernateSession();
		SessionFactory sessionFact = session.getSessionFactory();

		// Tranform hql to sql for execute size request
		String sqlRequest = toSql(finalRequest, sessionFact, fullCriterion);
		// Change request if distinct is found
		int indexOfDistict = finalRequest.toLowerCase().indexOf("distinct");
 		if(indexOfDistict == -1){
 			if(sqlRequest.toLowerCase().indexOf("order by") != -1) {
 				finalSqlRequest = "select count(0) " + sqlRequest.substring(sqlRequest.toLowerCase().indexOf("from"), sqlRequest.toLowerCase().lastIndexOf("order by"));
 			} else {
 				finalSqlRequest = "select count(0) " + sqlRequest.substring(sqlRequest.toLowerCase().indexOf("from"));
 			}
		} else{
			finalSqlRequest = "select count(0) from (" + sqlRequest + ") as size";
		}
		
		// Manage group by
		int indexOfGroupBy = finalSqlRequest.toLowerCase().indexOf("group by");
		if(indexOfGroupBy != -1){
			finalSqlRequest = "select count(0) from ("+sqlRequest+") as subq";
		}
		
		// Create native query
		Map<Integer, Object> mapParams = buildSqlNatifParams(finalSqlRequest, finalRequest, fullCriterion);
		finalSqlRequest = (String)mapParams.get(9999);
		mapParams.remove(9999);
		
		// On transforme car native sql ne supporte pas une liste ou set dans in clause
		for(Integer key : mapParams.keySet()){
			Object param = mapParams.get(key);
			if(param instanceof Set){
				Set dataSet = (Set)param;
				if(dataSet != null && dataSet.size() > 0){
					if(NumericUtil.isNum(dataSet.iterator().next())) {
						param = StringUtil.getStringDelimFromStringArray(((Set)mapParams.get(key)).toArray(), ",", false);
					} else {
						param = StringUtil.getStringDelimFromStringArray(((Set)mapParams.get(key)).toArray(), ",", true);
					}
					finalSqlRequest = finalSqlRequest.replaceFirst("in \\(\\?\\)", "in ("+param+")");
				}
			}
		}
		
		//
		Query query = /*getSocEtsFilterParam(*/getNativeQuery(finalSqlRequest);
		//
		int idx = 1;
		for(Integer key : mapParams.keySet()){
			Object param = mapParams.get(key);
			if(!(param instanceof Set)){
				query.setParameter(idx, param);
				idx++;
			}
		}

		// Execute
		Object result = query.getSingleResult(); 
		
		Integer size = NumericUtil.toInteger(result);
		// Start paggination
		cplxBean = cplxBean.startPagger(size);
		cplxBean.setDataSize(size);

		return cplxBean;
	}

	/**
	 * @param query
	 * @param params
	 */
	private Query buildHqlParams(String finalRequest, Query query, Map<String, Object> params){
		if(params != null){
			for(String st : params.keySet()){
				if(finalRequest.indexOf(":"+st+" ") != -1){
					Object value = params.get(st);
					//
					if(value instanceof Integer[] || value instanceof String[] || value instanceof Double[] || value instanceof Long[]){
						Object[] objArray = (Object[])value;
						query.setParameter(st, Arrays.asList(objArray));
					} else{
						query.setParameter(st, value);
					}
				}
			}
		}

		return query;
	}

	/**
	 * @param sqlRequest
	 * @param hqlRequest
	 * @param params
	 * @return
	 */
	private static Map<Integer, Object> buildSqlNatifParams(String sqlRequest, String hqlRequest, Map<String, Object> params){
		Map<String, Object> orderingParams = new LinkedHashMap<String, Object>();
		Map<Integer, Object> finalMap = new LinkedHashMap<Integer, Object>();
		//
		while(hqlRequest.indexOf(":") != -1){
			int idx = hqlRequest.indexOf(":")+1;
			int idxSpace = hqlRequest.indexOf(" ", idx);
			String param = StringUtil.replaceAll(hqlRequest.substring(idx, idxSpace), ')', "");
			hqlRequest = hqlRequest.substring(idxSpace);
			orderingParams.put(param, params.get(param));
		}
		//
		int idx = 1;
		for(String st : orderingParams.keySet()){
			Object value = orderingParams.get(st);
			finalMap.put(idx, value);
			idx++;
		}

		//
		finalMap.put(9999, sqlRequest);

		return finalMap;
	}

	/**
	 * @param hqlQueryText
	 * @param sessionFactory
	 * @return
	 */
	private String toSql(String hqlQueryText, SessionFactory sessionFactory, Map<String, Object> params){
	    if (hqlQueryText!=null && hqlQueryText.trim().length()>0){
	      final QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
	      final SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
	      final QueryTranslator translator = translatorFactory.createQueryTranslator(
															          hqlQueryText,
															          hqlQueryText,
															          Collections.EMPTY_MAP,
															         // params,
															          factory,
															          null
															        );
	      translator.compile(Collections.EMPTY_MAP, false);
	      //translator.compile(params, false);
	      String sqlString = translator.getSQLString();

	      return sqlString;
	    }
	    return null;
	  }

	@Override
	@Transactional(readOnly=true)
	public boolean isNotUnique(T e, String ... columns){
		if(e != null){
			Long id = ServiceUtil.getIdPersistantValue(e);
			String req = "from "+ServiceUtil.getEntityType(this).getSimpleName()+" where 1=1 ";
			
			int idx = 0;
			for(String col : columns){
				req += " and "+(col+"=:col"+idx);
				idx++;
			}
			
			if(id != null){
				req += " and id!=:id";
			}
			
			Query query = getQuery(req);
			
			if(id != null){
				query.setParameter("id", id);
			}
			
			idx = 0;
			for(String col : columns){
				query.setParameter("col"+idx, ReflectUtil.getObjectPropertieValue(e, col));
				idx++;
			}
			
			return query.getResultList().size() > 0;
		}
		return false;
		
//		if(e != null){
//			Long id = ServiceUtil.getIdPersistantValue(e);
//			Criteria crit1 = getHibernateSession().createCriteria(ServiceUtil.getEntityType(this));
//			// If update
//			if(id != null){
//				crit1.add(Restrictions.not(Restrictions.idEq(id)));
//			}
//			//
//			for(String col : columns){
//				crit1.add(Restrictions.eq(col, ReflectUtil.getObjectPropertieValue(e, col)));
//			}
//			// Get list
//			List<T> data = crit1.list();
//
//			return ((data != null) && (data.size() > 0));
//		}
//
//		return false;
		
	}

	/* 
	 *
	 */
	@Override
	public List<?> findByCriteria(RequestTableBean cplxTable, String query){
		return (List<?>)executeCriterionRequest(cplxTable, query, true);
	}

	/* (non-Javadoc)
	 * @see model.hibernate.dao.IGenericJpaDao#findByCriteria(front.component.complex.table.RequestTableBean, java.lang.String, boolean)
	 */
	public List<?> findByCriteria(RequestTableBean cplxTable, String query, boolean addLimit){
		return (List<?>)executeCriterionRequest(cplxTable, query, addLimit);
	}

	/* (non-Javadoc)
	 * @see model.hibernate.dao.IGenericJpaDao#work_findByCriteria(front.component.complex.table.RequestTableBean, java.lang.String)
	 */
	@Override
	public List<?> findByCriteriaQueryId(RequestTableBean cplxTable, String queryId){
		return (List<?>)executeCriterionRequestQueryId(cplxTable, queryId, true);
	}

	/* (non-Javadoc)
	 * @see model.hibernate.dao.IGenericJpaDao#work_findByCriteria(front.component.complex.table.RequestTableBean, java.lang.String)
	 */
	@Override
	public List<?> findByCriteriaQueryId(RequestTableBean cplxTable, String queryId, boolean addLimit){
		return (List<?>)executeCriterionRequestQueryId(cplxTable, queryId, addLimit);
	}

	/**
	 * @param critRequest
	 * @param cplxTable
	 * @return
	 * @throws Exception
	 */
	private List<?> executeCriterionRequest(RequestTableBean cplxBean, String critRequest, boolean limit){
		Map<String, Object> fullCriterion = new HashMap<String, Object>();
		SqlParser sqlParser = new SqlParser(critRequest, cplxBean);
		String finalRequest = sqlParser.getFinalQuery(); 
		// Build full criterion
		fullCriterion.putAll(cplxBean.getQueryCriteria());
		fullCriterion.putAll(cplxBean.getFormBean().getQueryCriterion());
		fullCriterion.putAll(cplxBean.getFormBean().getFormCriterion());
		
		// Pour éviter l'erreur de "ambigus column"		
		String shortReq = finalRequest.substring(0, finalRequest.indexOf("."));
		String alias = shortReq.substring(shortReq.lastIndexOf(" ")).trim();
		// Si parenthèse
		alias = alias.replaceAll("\\(", "");
		alias = alias.replace("CAST", "");
		
		if(finalRequest.toUpperCase().indexOf("ORDER BY") != -1){ 
			finalRequest = finalRequest + ", "+alias+".id desc";
		} else{
			finalRequest = finalRequest + " order by "+alias+".id desc";
		}
		
		// Size request
		cplxBean = executeQuerySize(cplxBean, fullCriterion, finalRequest);
		// Export request
		if(cplxBean.isExport()){
			executeQueryExport(cplxBean, finalRequest);
		}
		// Critrion request
		
		finalRequest = getSocieteEtsCondition(finalRequest, false);
		Query query = getEntityManager().createQuery(finalRequest);
		query = buildHqlParams(finalRequest, query, fullCriterion);
		// Start and end parms
		if(limit){
			query.setFirstResult(cplxBean.getStartIndex());
			query.setMaxResults(cplxBean.getPageSize());
		}

		return query.getResultList();
	}


	/**
	 * @param cplxTable
	 * @param queryId
	 * @param limit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<T> executeCriterionRequestQueryId(RequestTableBean cplxTable, String queryId, boolean limit) {
		String query = getQueryString(queryId);
		return (List<T>)executeCriterionRequest(cplxTable, query, limit);
	}

	/**
	 * @param queryId
	 * @return
	 */
	private String getQueryString(String queryId){
		QueryImpl hbQuery = (QueryImpl)getEntityManager().createNamedQuery(queryId);
		//org.hibernate.ejb.QueryImpl hbQuery = (org.hibernate.ejb.QueryImpl)emQuery;
		String query = hbQuery.getQueryString();//HibernateQuery().getQueryString();

		return query;
	}

	/**
	 * @param cplxBean
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private void executeQueryExport(RequestTableBean cplxBean, String request){
		// Request pager
		Query query = getSocieteEtsConditionQuery(getEntityManager(), request, false);
		query = buildHqlParams(request, query, cplxBean.getFormBean().getQueryCriterion());
		// List
		List dataExport = query.getResultList();
		cplxBean.setDataExport(dataExport);
	}

	/**
	 * @param request
	 * @param criters
	 * @return
	 */
	@Override
	public Object getSingleResult(Query query){
		try {
			return query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	@Override
	@Transactional
	public void updateRowsOrder(String[] orderArray){
		int orderIdx = 1;
		//
		for(String order : orderArray){
			Long id = Long.valueOf(EncryptionUtil.decrypt(order));
			 T ordrePersistant = findById((I) id);
			ReflectUtil.setProperty(ordrePersistant, "ordre", orderIdx);
			update(ordrePersistant);
			orderIdx++;
		}
	}
}