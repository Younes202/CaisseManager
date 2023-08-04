package framework.model.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.hibernate.criterion.Order;
import org.springframework.transaction.annotation.Transactional;

import appli.controller.domaine.util_erp.ContextAppli;
import appli.model.domaine.administration.persistant.TypeEnumPersistant;
import appli.model.domaine.administration.persistant.ValTypeEnumPersistant;
import framework.component.complex.table.RequestTableBean;
import framework.component.complex.table.RowBean;
import framework.controller.ContextGloabalAppli;
import framework.controller.bean.action.IViewBean;
import framework.model.beanContext.DataFormPersistant;
import framework.model.beanContext.DataValuesPersistant;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;
import framework.model.util.FileUtil;
import framework.model.util.IGenericJpaDao;
import framework.model.util.audit.ReplicationGenerationEventListener;

@Named
@Transactional
public abstract class GenericJpaService<B, I extends Serializable> implements IGenericJpaService<B, I> {
	@Override
	public Query getQuery(String request) {
		return getGenriqueDao().getQuery(request);
	}
	@Override
	public Query getQuery(EntityManager em, String request) {
		return getGenriqueDao().getQuery(em, request);
	}
	@Override
	public Query getNativeQuery(String request) {
		return getGenriqueDao().getNativeQuery(request);
	}
	@Override
	public Query getNativeQuery(EntityManager em, String request) {
		return getGenriqueDao().getNativeQuery(em, request);
	}
	
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

	/**
	 * @return
	 */
	// For spring
	public IGenericJpaDao getGenriqueDao() {
		// name of dao must be same name of service
		String serviceNameEnd = StrimUtil.getGlobalConfigPropertie("service.end");
		String daoNameEnd = StrimUtil.getGlobalConfigPropertie("dao.end");
		String serviceName = this.getClass().getSimpleName();
		String daoName = serviceName.substring(0, serviceName.indexOf(serviceNameEnd)) + daoNameEnd;
		// Get bean
		IGenericJpaDao currentDao = (IGenericJpaDao) ServiceUtil.getBusinessBean(daoName);

		return currentDao;
	}

	@WorkModelMethodValidator
	public B merge(B e) {
		if (ReflectUtil.getObjectPropertieValue(e, "id") == null) {
			create(e);
		} else {
			return update(e);
		}
		return e;
	}
	
	@Transactional
	public <T> void mergeEntity(T e) {
		e = getEntityManager().merge(e);
		ReflectUtil.setProperty(e, "id", ReflectUtil.getObjectPropertieValue(e, "id"));
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(readOnly=true)
	public B findById(I id) {
		Class beanClass = ReflectUtil.getGenericArgsClass(this.getClass(), 0);
		return (B) ServiceUtil.persistantToBean(beanClass, getGenriqueDao().findById(id));
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public <T> T findById(Class<T> beanClass, I id) {
		return (T) getGenriqueDao().findById(beanClass, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<B> findAll() {
		return getGenriqueDao().findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAll(Class<T> beanClass) {
		return getGenriqueDao().findAll(beanClass);
	}

	@Override
	@Transactional(readOnly = true)
	public List<B> findAll(Order... orders) {
		return getGenriqueDao().findAll(orders);
	}

	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAll(Class<T> beanClass, Order... orders) {
		return getGenriqueDao().findAll(beanClass, orders);
	}
	@Override
	@Transactional(readOnly = true)
	public List<?> findAllNoFilter(Class beanClass, Order... orders) {
		String orderStr = "";
		if(orders != null){
			for (Order order : orders) {
				orderStr += order.getPropertyName()+" "+(order.isAscending() ? "asc":"desc")+",";
			}
			if(StringUtil.isNotEmpty(orderStr)) {
				orderStr = orderStr.substring(0, orderStr.length()-1);
				orderStr = " order by " + orderStr;
			}
		}
		
		String req = "from "+beanClass.getSimpleName() + orderStr;
		return getEntityManager().createQuery(req).getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<?> findByCriteria(RequestTableBean cplxTable, String query) {
		return getGenriqueDao().findByCriteria(cplxTable, query);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public List<?> findByCriteria(RequestTableBean cplxTable, String query, boolean addLimit) {
		return getGenriqueDao().findByCriteria(cplxTable, query, addLimit);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public List<?> findByCriteriaByQueryId(RequestTableBean cplxTable, String queryId) {
		List listData = getGenriqueDao().findByCriteriaQueryId(cplxTable, queryId);

		// Refresh data (Correction bug données de jointures qui ne s'affichent
		// pas)
		// refreshEntities(listData);

		return listData;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public List<?> findByCriteriaByQueryId(RequestTableBean cplxTable, String queryId, boolean addLimit) {
		List listData = getGenriqueDao().findByCriteriaQueryId(cplxTable, queryId, addLimit);
		return listData;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create(B e) {
		getGenriqueDao().create(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public B update(B e) {
		e = (B) ServiceUtil.persistantToBean(e.getClass(), getGenriqueDao().update(e));

		return e;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void update_group(B e, I[] ids, List<String> listModifiedFields) {
		getGenriqueDao().update_group(e, ids, listModifiedFields);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(B e) {
		getGenriqueDao().delete(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete(I id) {
		B entity = (B) getGenriqueDao().findById(id);
		if(entity != null) {
			delete(entity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void delete_group(I[] ids) {
		getGenriqueDao().delete_group(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.hibernate.service.IGenericJpaService#refresh(java.lang.Object)
	 */
	@Override
	public void refreshEntity(Object e) {
		getGenriqueDao().getEntityManager().refresh(e, LockModeType.READ);
	}

	/**
	 * @param listE
	 */
	@Override
	public void refreshEntities(Collection<?> listE) {
		if (listE != null) {
			EntityManager entityManager = getGenriqueDao().getEntityManager();
			//
			for (Object e : listE) {
				entityManager.refresh(e, LockModeType.READ);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void update_rows(List<RowBean> listRows, Map<String, List<String>> mapModifiedRows) {
		if (listRows != null) {
			List<B> listB = (List<B>) listRowsToListBean(listRows);
			getGenriqueDao().update_rows((List) listB, mapModifiedRows);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@WorkModelMethodValidator
	public void create_rows(List<RowBean> listRows) {
		if (listRows != null) {
			List<B> listB = (List<B>) listRowsToListBean(listRows);
			//
			for (B e : listB) {
				getGenriqueDao().create(e);
			}
		}
	}

	/**
	 * @param listRows
	 * @return
	 */
	private List<IViewBean> listRowsToListBean(List<RowBean> listRows) {
		if (listRows != null) {
			List<IViewBean> listBean = new ArrayList<IViewBean>();
			//
			for (RowBean rowBean : listRows) {
				listBean.add(rowBean.getBean());
			}
			return listBean;
		}

		return null;
	}

	// ------------------------------------ Asynchron service
	// -----------------------------------------------
	public void asyncSetServiceId(String srvJobId) {
	}

	/**
	 * @param total
	 */
	protected void asyncSetTotalElement(int total) {
	}

	/**
	 * @param beanClass
	 * @param listPersistant
	 * @return
	 * @throws Exception
	 */
	protected <T> List<T> persistantToBean(Class<T> beanClass, Collection<?> listPersistant) {
		return ServiceUtil.listPersistantToListBean(beanClass, listPersistant);
	}

	/**
	 * @param listPersistant
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<B> persistantToBean(Collection<?> listPersistant) {
		return (List<B>) ServiceUtil.listPersistantToListBean(ServiceUtil.getEntityType(this), listPersistant);
	}

	/**
	 * @param beanClass
	 * @param persistant
	 * @return
	 * @throws Exception
	 */
	protected <T> T persistantToBean(Class<T> beanClass, Object persistant) {
		return ServiceUtil.persistantToBean(beanClass, persistant);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.dao.IGenericDao#getEntityManager()
	 */
	public EntityManager getEntityManager() {
		return getGenriqueDao().getEntityManager();
	}

	public Object getSingleResult(Query query) {
		return getGenriqueDao().getSingleResult(query);
	}

	// ----------------------------------gestion des
	// images------------------------------------------------
	@Override
	public List<?> getListData(Class persistant, String order) {
		String request = "from " + persistant.getSimpleName() + " order by " + order;
		IGenericJpaDao genriqueDao = getGenriqueDao();
		request = genriqueDao.getSocieteEtsCondition(request, false);
		Query query = /*genriqueDao.getSocEtsFilterParam(*/getQuery(request);
		
		return  query.getResultList();
	}

	@Override
	public Map<String, byte[]> getDataImage(Long elementId, String path) {
		path = ContextGloabalAppli.getEtablissementBean().getId() + "/" + path + "/" + elementId;

		return FileUtil.getListFilesByte(path);
	}

	public static void mergeDataFile(Long elementId, String path, Map<String, byte[]> images,
			Map<String, String> mapFilesUnloaded, Integer width, Integer height) {
		String startChemin = ContextGloabalAppli.getEtablissementBean().getId() + "/" + path;

		if (images == null) {// Il s'agit dans ce cas d'une suppression
			FileUtil.clearDir(startChemin + "/" + elementId);
			return;
		}
		// Images du formulaire
		for (String imageName : images.keySet()) {
			if (images.get(imageName) == null) {
				continue;
			}

			String chemin = startChemin + "/" + elementId + "/" + imageName;
			FileUtil.uploadFilesToDir(images.get(imageName), chemin, width, height);
		}

		// Purger les fichiers innexistants
		if (mapFilesUnloaded != null && !mapFilesUnloaded.isEmpty()) {
			for (String name : FileUtil.getListFiles(startChemin + "/" + elementId)) {
				if (!images.containsKey(name) && mapFilesUnloaded.containsKey(name)) {
					FileUtil.deleteFile(startChemin + "/" + elementId + "/" + name);
				}
			}
		}
	}

	@Override
	public <T> T getEntityByCodeFunc(String classType, String code_func) {
		return getEntityByCodeFunc(getEntityManager(), classType, code_func);
	}

	@Override
	public <T> T getEntityByCodeFunc(EntityManager em, String classType, String code_func) {
		if(classType.indexOf("$") != -1) {
			classType = classType.substring(0, classType.indexOf("$"));
		}
		if(classType.indexOf("Proxy") != -1) {
			return null;
		}
		
		List<T> lisEntity = em.createQuery("from " + classType + " where code_func=:code_func")
				.setParameter("code_func", code_func)
				.getResultList();

		return (lisEntity.size() > 0 ? lisEntity.get(0) : null);

//		if (entity == null && NumericUtil.isLong(code_func)) {
//			lisEntity = em.createQuery("from " + classType + " where id=:id")
//					.setParameter("id", Long.valueOf(code_func))
//					.getResultList();
//
//			entity = (lisEntity.size() > 0 ? lisEntity.get(0) : null);
//		}

		//return entity;
	}
	
	@Override
	public <T>T getOneByField(Class<T> entity, String field, Object val) {
		return (T) getSingleResult(getEntityManager().createQuery("from "+((Class<T>) entity).getName()+" where "
				+ field+"=:field")
				.setParameter("field", val));
	}
	@Override
	public <T>List<T> findByField(Class<T> entity, String field, Object val, Order ... orders) {
		return getEntityManager().createQuery("from "+((Class<T>) entity).getName()+" where "
				+ field+"=:field")
				.setParameter("field", val)
				.getResultList();
	}

	/**
	 * @param type
	 * @return
	 */
	@Override
	public String generateCodeBarre(String type) {
		IGenericJpaService clientService = (IGenericJpaService) ServiceUtil.getBusinessBean(IGenericJpaService.class);

		String startCode = null;
		String table = null;
		switch (type) {
		case "ART": {
			startCode = "991";
			table = "article";
		}
			;
			break;
		case "CLI": {
			startCode = "992";
			table = "client";
		}
			;
			break;
		case "CARTE": {
			startCode = "993";
			table = "carte_fidelite_client";
		}
		}

		//
		if (StringUtil.isEmpty(startCode)) {
			return null;
		}

		IGenericJpaDao genriqueDao = getGenriqueDao();
		String codeBarre = "";
		EntityManager em = clientService.getEntityManager();
//		Query query = getNativeQuery(em, "select max(CAST(code_barre AS UNSIGNED)) from "+table);
		
		String request = "select max(CAST(code_barre AS UNSIGNED)) from "+table;
		request = genriqueDao.getSocieteEtsCondition(request, true);
		Query query = /*genriqueDao.getSocEtsFilterParam(*/getQuery(request);
		
		
		BigInteger max_num = (BigInteger) query.getSingleResult();
		if (max_num != null) {
			codeBarre = "" + (max_num.intValue() + 1);
		} else {
			codeBarre = "0000000001";
		}

		if (codeBarre.length() < 10) {
			codeBarre = startCode + codeBarre;
			int codeCtrl = CHECK_SUM(codeBarre) - 2;
			codeBarre = codeBarre + codeCtrl;
		}

		return codeBarre;
	}

	private int CHECK_SUM(String Input) {
		int evens = 0; // initialize evens variable
		int odds = 0; // initialize odds variable
		int checkSum = 0; // initialize the checkSum
		for (int i = 0; i < Input.length(); i++) {
			// check if number is odd or even
			if ((int) Input.charAt(i) % 2 == 0) { // check that the character at
													// position "i" is divisible
													// by 2 which means it's
													// even
				evens += (int) Input.charAt(i);// then add it to the evens
			} else {
				odds += (int) Input.charAt(i); // else add it to the odds
			}
		}
		odds = odds * 3; // multiply odds by three
		int total = odds + evens; // sum odds and evens
		if (total % 10 == 0) { // if total is divisible by ten, special case
			checkSum = 0;// checksum is zero
		} else { // total is not divisible by ten
			checkSum = 10 - (total % 10); // subtract the ones digit from 10 to
											// find the checksum
		}
		return checkSum;
	}

	/*
	 * ----------------------------- DATA FORM
	 * ----------------------------------------
	 */
	@Override
	public Map<String, List<ValTypeEnumPersistant>> loadDataENumForm(String groupe) {
		EntityManager em = getEntityManager();
		
		
		String request = "from DataFormPersistant where data_group=:groupe";
		request = getGenriqueDao().getSocieteEtsCondition(request, false);
		Query query = getQuery(request);
		
		List<DataFormPersistant> listType = query.setParameter("groupe", groupe)
													.getResultList();

		// Charger les enums
		Map<String, List<ValTypeEnumPersistant>> mapData = new HashMap<>();
		//
		for (DataFormPersistant dataFormP : listType) {
			if (dataFormP.getData_type().equals("ENUM")) {
				TypeEnumPersistant typeEnumP = em.find(TypeEnumPersistant.class, dataFormP.getData_enum());
				List<ValTypeEnumPersistant> listValeurs = typeEnumP.getList_valeur();
				mapData.put("listEnumValue_" + dataFormP.getId(), listValeurs);
			}
		}

		return mapData;
	}

	@Override
	public List<DataValuesPersistant> loadDataForm(Long elmntId, String groupe) {
		String request = "from DataFormPersistant where data_group=:groupe order by id";
		request = getGenriqueDao().getSocieteEtsCondition(request, false);
		Query query = getQuery(request);
		
		List<DataFormPersistant> listType = query.setParameter("groupe", groupe)
				.getResultList();

		List<DataValuesPersistant> listValues = null;
		if (elmntId != null) {
			
			request = "from DataValuesPersistant "
					+ "where element_id=:elmntId and data_group=:groupe "
					+ "order by opc_data_form.data_order, opc_data_form.id";
			request = getGenriqueDao().getSocieteEtsCondition(request, false);
			query = getQuery(request);
			
			listValues = query.setParameter("elmntId", elmntId)
							.setParameter("groupe", groupe).getResultList();
		} else {
			listValues = new ArrayList<>();
		}
		// Ajouter tous les champs
		for (DataFormPersistant dataFormP : listType) {
			DataValuesPersistant dataValP = null;

			for (DataValuesPersistant dataValuesP : listValues) {
				if (dataValuesP.getOpc_data_form().getId().equals(dataFormP.getId())) {
					dataValP = dataValuesP;
					break;
				}
			}

			if (dataValP == null) {
				dataValP = new DataValuesPersistant();
				dataValP.setData_group(groupe);
				dataValP.setElement_id(elmntId);
				dataValP.setOpc_data_form(dataFormP);
				//
				listValues.add(dataValP);
			}
		}

		return listValues;
	}

	@Override
	@Transactional
	public void deleteDataForm(Long elmntId, String groupe) {
		String request = "delete from DataValuesPersistant "
				+ "where element_id=:elmntId and data_group=:groupe";
		request = getGenriqueDao().getSocieteEtsCondition(request, false);
		Query query = getQuery(request);
		
		// Purger
		query.setParameter("elmntId", elmntId)
				.setParameter("groupe", groupe)
				.executeUpdate();
	}

	@Override
	@Transactional
	public void mergeDataForm(List<DataValuesPersistant> listDataValue, Long elmntId, String groupe) {
		
		String request = "from DataFormPersistant " + "where data_group=:groupe";
		request = getGenriqueDao().getSocieteEtsCondition(request, false);
		Query query = getQuery(request);
		
		EntityManager em = getEntityManager();
		List<DataFormPersistant> listType = query.setParameter("groupe", groupe).getResultList();

		for (DataFormPersistant dataTypeP : listType) {
			for (DataValuesPersistant dataValuesP : listDataValue) {
				if (dataValuesP.getId() == null || !dataValuesP.getId().equals(dataTypeP.getId())) {
					continue;
				}
				
				dataValuesP = (DataValuesPersistant) ReflectUtil.cloneBean(dataValuesP);
				dataValuesP.setId(null);// Dans cet id on stock l'info du data
										// type
				dataValuesP.setData_group(groupe);
				dataValuesP.setElement_id(elmntId);
				dataValuesP.setOpc_data_form(dataTypeP);
				dataValuesP.setData_code("" + System.currentTimeMillis());
				//
				em.merge(dataValuesP);
			}
		}
	}
	
	@Override
	@Transactional
	public void updateRowsOrder(String[] orderArray){
		getGenriqueDao().updateRowsOrder(orderArray);
	}
}
