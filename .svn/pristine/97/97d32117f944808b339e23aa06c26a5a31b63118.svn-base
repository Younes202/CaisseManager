/**
 *
 */
package framework.model.common.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import framework.model.common.util.converter.DateConverter;
import framework.model.common.util.converter.FloatConverter;

/**
 * @author 
 *
 */
public class ControllerBeanUtil {

	private final static Logger LOGGER = Logger.getLogger(ControllerBeanUtil.class);

	/**
	 * @param data
	 * @param beanPath
	 * @return
	 */
	public static <T> T mapToBean(Class<T> bean, Map<String, Object> data) { 
		T viewBean = null;

		try {
			viewBean = bean.newInstance();
			// Iterate maps for get value of action
			String aliasBean = getAliasBeanByObject(viewBean) + ".";
			Map<String, Object> tempMap = new HashMap<String, Object>();
			//
			for(String key : data.keySet()) {
				// Remove alias bean in fields names
				
				// if null continue 
				if(key.equals(null)){
					continue;
				}
				
				int idxAlias = key.indexOf(aliasBean);
				boolean isWork = (key.indexOf("work") != -1);
				//
				if(!isWork){
					boolean isToadd = true;
					//
					if(isToadd){
						String shortKey = (idxAlias == -1) ? key : key.substring(idxAlias+aliasBean.length());
						tempMap.put(shortKey, data.get(key));
						// Instaciate dependancies
						if ((shortKey.indexOf(".") != -1)) {
							if(StringUtil.isNotEmpty(data.get(key))){
								insatnceAttribute(viewBean, key);
							} else {
								tempMap.remove(shortKey);
							}
						}
					}
				}
			}
			populate(viewBean, tempMap);
			//tempMap = null;
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
			throw new RuntimeException(e);
		}

		return viewBean;
	}

	/**
	 * Populate bean from data and build bean util with necessary converters
	 * See example at : http://www.java2s.com/Code/Java/Apache-Common/ConvertUtilsDemo.htm
	 * @param bean
	 * @param data
	 * @throws Exception
	 */
	private static void populate(Object bean, Map<String, Object> data) throws Exception{
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
		// Date converter
	    convertUtilsBean.deregister(Date.class);
	    convertUtilsBean.register(new DateConverter(), Date.class);
	    // Long converter
	   // convertUtilsBean.deregister(Long.class);
	   // convertUtilsBean.register(new FloatConverter(), Long.class);
		 convertUtilsBean.deregister(Float.class);
		 convertUtilsBean.register(new FloatConverter(), Float.class);

	    // Integer converter for boolean
	   // convertUtilsBean.deregister(Integer.class);
	   // convertUtilsBean.register(new IntegerConverter(), Integer.class);
	    // Populate
	    BeanUtilsBean beanUtil = new BeanUtilsBean (convertUtilsBean, new PropertyUtilsBean());// Changement suite au probl�mes de conversion

		//BeanPopulator.newBeanPopulator(b1, b2).populate();

	    //
	    for(String key : data.keySet()){
	    	Object valueObject = data.get(key);
	    	// --------------------------------------- Correction integer à 0
	    	if(StringUtil.isNotEmpty(valueObject)){
		    	if(valueObject instanceof Long[] 
		    	                       || valueObject instanceof String[]
		    	                       || valueObject instanceof Integer[]){
		    		// Populate collections
		    	    populateBeanCollectionField((Object[]) valueObject, bean, key);
		    	} else if(key.indexOf("[]") == -1){
		    		beanUtil.copyProperty(bean, key, valueObject);
		    	}
	    	}
	    }

	    //beanUtil.populate(bean, data);
	}

	/**
	 * @param viewBean
	 * @param fieldsName
	 * @return
	 */
	private static Object insatnceAttribute(Object viewBean, String fieldsName) {
		String[] fieldsArray = getObjectArray(fieldsName);

		if ((viewBean != null) && fieldsArray.length > 1) {
			BeanInfo beanInfo;
			try {
				beanInfo = Introspector.getBeanInfo(viewBean.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				// Array of fields
				for (int j = 0; j < fieldsArray.length; j++) {
					if (j == (fieldsArray.length - 1)) {
						break;
					}
					// Iterate property bean
					for (int i = 0; i < propertyDescriptors.length; i++) {
						PropertyDescriptor prop = propertyDescriptors[i];
						Method writeMethod = prop.getWriteMethod();
						Method readMethod = prop.getReadMethod();
						// If getter and setter are present
						if (writeMethod != null && readMethod != null) {
							String attributeName = prop.getName().toLowerCase();
							String fieldName = fieldsArray[j].toLowerCase();

							if (attributeName.equals(fieldName)) {
								Object instance = null;
								Object retour = readMethod.invoke(viewBean, new Object[] {});

								// Index of "."
								int idx = fieldsName.indexOf(".") + 1;
								// Field name
								fieldsName = fieldsName.substring(idx, fieldsName.length());

								if (retour == null) {
									// Return type
									Class<?> returnType = readMethod.getReturnType();
									// New instance of object
									instance = returnType.newInstance();
									writeMethod.invoke(viewBean, new Object[] { instance });
								} else {
									instance = retour;
								}
								// Inancate attribute
								insatnceAttribute(instance, fieldsName);
								break;
							}
						}
					}
				}
			} catch (IntrospectionException e) {
				LOGGER.error("Erreur : ", e);
			} catch (InstantiationException e) {
				LOGGER.error("Erreur : ", e);
			} catch (IllegalAccessException e) {
				LOGGER.error("Erreur : ", e);
			} catch (IllegalArgumentException e) {
				LOGGER.error("Erreur : ", e);
			} catch (InvocationTargetException e) {
				LOGGER.error("Erreur : ", e);
			}
		}

		return viewBean;
	}

	/**
	 * @param idsArray
	 * @param bean
	 * @param field
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void populateBeanCollectionField(Object[] idsArray, Object bean, String field) throws Exception{
		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor prop = propertyDescriptors[i];
			String propName = prop.getName();
			if(propName.equals(field)){
				Method writeMethod = prop.getWriteMethod();
				Method readMethod = prop.getReadMethod();
				// If getter and setter are present
				if (writeMethod != null && readMethod != null) {
					Class returnType = readMethod.getReturnType();
					boolean isSet = returnType.getSimpleName().equals("Set");
					// If set or list, instance parameterized object and set value
					if(isSet || returnType.getSimpleName().equals("List")){
						Type type = readMethod.getGenericReturnType();
						try{
							ParameterizedType paramType = (ParameterizedType) type;
							Class genericType = (Class)paramType.getActualTypeArguments()[0];
							Collection list = null;
							// Build Set orList
							if(isSet){
								list = new HashSet();
							} else{
								list = new ArrayList();
							}
							// Build object persitant
							for(Object id : idsArray){
								Object persistant = genericType.newInstance();
								String idName = ServiceUtil.getIdPersistantName(persistant);
								// Set id
								ReflectUtil.setProperty(persistant, idName, id);
								// Add to collection
								list.add(persistant);
							}
							// Instantiate collection field
							ReflectUtil.setProperty(bean, field, list);

						} catch(ClassCastException e){
							throw new Exception("The field -"+propName+"- must have a generic type declared.\n" +
									"Ex: Set<MyPersistant> "+propName+" or List<MyPersistant> "+propName+" : \n"+e);
						}
					} else {
						ReflectUtil.setProperty(bean, field, idsArray);
					}
				}
				break;
			}
		}
	}

	/**
	 * Get fileds array from field name
	 *
	 * @param fieldName
	 *            separator = "."
	 * @return Array
	 */
	private static String[] getObjectArray(String fieldName) {
		StringTokenizer st = new StringTokenizer(fieldName, ".");

		int count = st.countTokens();
		if (count > 0) {
			String[] elements = new String[count];
			int idx = 0;
			while (st.hasMoreTokens()) {
				elements[idx] = st.nextToken();
				idx++;
			}

			return elements;
		}

		return null;
	}

	/**
	 * @param viewBean
	 * @return
	 */
	public static String getAliasBeanByObject(Object viewBean){
		return getAliasBeanByClass(viewBean.getClass());
	}

	/**
	 * @param viewBean
	 * @return The alias of bean (name with the first char to lower case) the
	 * bean or persistant attachement is removed
	 */
	public static String getAliasBeanByClass(Class<?> viewBean){
		if(viewBean != null){
			String nameBean = StringUtil.firstCharToLowerCase(viewBean.getSimpleName());
			String endBean = StrimUtil.getGlobalConfigPropertie("bean.end");
			int idxBean = nameBean.indexOf(endBean.toLowerCase());
			//
			if(idxBean == -1){
				idxBean = nameBean.indexOf(endBean);
				String endPersistant = StrimUtil.getGlobalConfigPropertie("persistant.end");
				//
				if(idxBean == -1){
					idxBean = nameBean.indexOf(endPersistant.toLowerCase());
				}
				if(idxBean == -1){
					idxBean = nameBean.indexOf(endPersistant);
				}
			}
			if(idxBean != -1){
				nameBean = nameBean.substring(0, idxBean);
			}

			return nameBean;
		}

		return "";
	}

}
