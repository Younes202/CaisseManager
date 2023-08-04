package framework.model.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import appli.controller.domaine.util_erp.ContextAppli;
import framework.model.common.constante.ProjectConstante;
import framework.model.common.spring.ApplicationContextHolder;

public class ServiceUtil {
	/**
	 * @param contexte
	 * @param serviceClass
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	// For Guice
/*	public static Object getBusinessBean(Class<?> serviceClass) {
		//-------------------------- Guice ------------------------------
		Injector injector = GuiceFactory.getInjector();
		//Injector injector = (Injector) request.getServletContext().getAttribute(Injector.class.getName());
		return injector.getInstance(serviceClass);
	}*/

	/**
	 * @param contexte
	 * @param serviceClass
	 * @param beanName
	 * @return
	 * @throws Exception
	 */
	// For Spring
	public static <T>T getBusinessBean(Class<T> serviceClass) {
		String alias = null;
		alias = serviceClass.getSimpleName();
		// Delete first char
		if(alias.startsWith("I") && serviceClass.isInterface()){
			alias = alias.substring(1);
		}
		// First char to lowercase
		alias = StringUtil.firstCharToLowerCase(alias);
		
		// Get spring bean
		Object springBean = getBusinessBean(alias);

		return (T) springBean;
	}

	/** Gets a service by name */
	// For Spring
	public static Object getBusinessBean(String name) {
		Object service = null;
		try {
			name = StringUtil.firstCharToLowerCase(name);
			service = ApplicationContextHolder.getApplicationContext().getBean(name);
		} catch (Exception e) {
			//LOGGER.error("Erreur : ", e);
		}

		return service;
	}
	
	/**
	 * @param persistant
	 * @return
	 * @throws Exception
	 */
	public static Long getIdPersistantValue(Object persistant) throws RuntimeException {
		try{
			String idPersistantName = getIdPersistantName(persistant);
			if(idPersistantName != null){
				String value = BeanUtils.getProperty(persistant, idPersistantName);
				if(StringUtil.isNotEmpty(value) && NumericUtil.isNum(value)){
					return Long.valueOf(value);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;
	}

	/**
	 * @param persistant
	 * @return
	 */

	public static String getIdPersistantName(Object persistant){
		if(persistant == null){
			return null;
		}
		Class<?> myClass = persistant.getClass();
		while(!myClass.equals(Object.class)){
			Field[] fields = myClass.getDeclaredFields();
			for(Field fi : fields){
				Id idAnnotation = fi.getAnnotation(Id.class);
				if(idAnnotation != null){
					return fi.getName();
				}
			}
			//
			myClass = myClass.getSuperclass();
		}
		// Façon plus clean pour recuperer l'id
       // EntityType<? extends Object> entityType = entityManager.getMetamodel().entity(entity.getClass());
       // String idName = entityType.getId(entityType.getIdType().getJavaType()).getName();

		return null;
	}

	/**
	 * Update id of persistant
	 * @param viewBean
	 * @throws Exception
	 */

	public static void setIdPersistantToNull(Object viewBean) throws Exception {
		if(viewBean != null){
			Class<?> myClass = viewBean.getClass();
			while(!myClass.equals(Object.class)){
				Field[] fields = myClass.getDeclaredFields();
				for(Field field : fields){
					Id idAnnotation = field.getAnnotation(Id.class);
					if(idAnnotation != null){
						String setterMethode = "set"+StringUtil.firstCharToUpperCase(field.getName());
						ReflectUtil.invokeMethode(viewBean, setterMethode, new Object[]{null}, new Class[]{field.getType()});
						break;
					}
				}
				//
				myClass = myClass.getSuperclass();
			}
		}
	}

	/**
	 * @param collect
	 * @return
	 */

	public static boolean isEmpty(Collection<?> collect){
		if(collect != null && collect.size() > 0){
			return false;
		}

		return true;
	}


	public static boolean isEmpty(Map<String, ?> map){
		if(map != null && map.size() > 0){
			return false;
		}

		return true;
	}

	/**
	 * @param collect
	 * @return
	 */

	public static boolean isNotEmpty(Collection<?> collect){
		return !isEmpty(collect);
	}

	/**
	 * @param map
	 * @return
	 */

	public static boolean isNotEmpty(Map<String, ?> map){
		return !isEmpty(map);
	}

	/**
	 * @param bean
	 * @param persistant
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws
	 * @throws Exception
	 */

	public static <T>T persistantToBean(Class<T> bean, Object persistant) throws RuntimeException {
		T beanClass = null;
		try{
			if(persistant != null){
				beanClass = (T)bean.newInstance();
				PropertyUtils.copyProperties(beanClass, persistant);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return beanClass;
	}

	/**
	 * @param bean
	 * @param listPersistant
	 * @return
	 * @throws Exception
	 */

	public static <T>List<T> listPersistantToListBean(Class<T> bean, Collection<?> listPersistant) throws RuntimeException  {
		List<T> listBean = null;
		if(listPersistant != null){
			listBean = new ArrayList<T>(listPersistant.size());
			for(Object persistant : listPersistant){
				try {
					listBean.add(persistantToBean(bean, persistant));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}

		return listBean;
	}

	/**
	 * @return
	 */
	public static Class<?> getEntityType(Object instance) {
        Class entityBeanType = null;
        Class<? extends Object> class1 = instance.getClass();
		// For guice
		if (class1.getName().contains("EnhancerByGuice")) {
			class1 = class1.getSuperclass();
		}
		Type type = class1.getGenericSuperclass();
        //
        if(type instanceof ParameterizedType){
             ParameterizedType pt =(ParameterizedType) type;
             Type[] fieldArgTypes = pt.getActualTypeArguments();
             entityBeanType = (Class) fieldArgTypes[0];
       }

       return entityBeanType;
	}
	
	/**
	 * @param date :
	 *            date to convert
	 * @param pattern :
	 *            pattern or format of date
	 * @return date string
	 */
	public static String dateToString(Date date, String pattern) {
		if(date != null){
			/* Formatting Date -> String */
			return (new SimpleDateFormat(pattern)).format(date);
		}

		return null;
	}
	
	/**
	 * @param dbEntitiy
	 */
	public static void setGenericFields(Object dbEntitiy){
		try{
			String login = (ContextAppli.getUserBean()!=null ? ContextAppli.getUserBean().getLogin() : "");
			Date currDate = new Date();
			//
			Object id = ReflectUtil.getObjectPropertieValue(dbEntitiy, "id");
			if(id == null){
				ReflectUtil.setProperty(dbEntitiy, ProjectConstante.DATE_CREATION,  currDate);
			}
			ReflectUtil.setProperty(dbEntitiy, ProjectConstante.DATE_MAJ, currDate);
			ReflectUtil.setProperty(dbEntitiy, ProjectConstante.SIGNATURE, login);
			ReflectUtil.setProperty(dbEntitiy, ProjectConstante.ETABLISSEMENT, ContextAppli.getEtablissementBean());
			ReflectUtil.setProperty(dbEntitiy, ProjectConstante.SOCIETE, ContextAppli.getSocieteBean());
			ReflectUtil.setProperty(dbEntitiy, ProjectConstante.ABONNE, ContextAppli.getAbonneBean());
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
