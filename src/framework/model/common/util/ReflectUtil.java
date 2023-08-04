package framework.model.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import framework.component.Component;

public class ReflectUtil {

	private final static Logger LOGGER = Logger.getLogger(ReflectUtil.class);
	private final static int MAX_LEVEL = 2;

	/**
	 * @param myClass
	 * @param methodName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object invokeMethode(Object myObject, String methodName, Object[] objArgs, Class[] types) throws Exception {
		Object result = null;
		Class myClass = myObject.getClass();

		// For guice
		if (myClass.getName().contains("EnhancerByGuice")) {
			myClass = myClass.getSuperclass();
		}
		
		Method method = myClass.getMethod(methodName, types);
		if (method != null)
			result = method.invoke(myObject, objArgs);
		else {
			types = null;
			myClass = null;
		}

		return result;
	}

	/**
	 * @param classInstance
	 * @param fieldName
	 * @param value
	 * @throws Exception
	 */
	public static void invokeField(Object classInstance, String fieldName, Object value) throws Exception {
		Field field = getField(classInstance, fieldName);

		// If field is not null
		if(field != null){
			field.setAccessible(true);
			field.set(classInstance, value);
			field.setAccessible(false);
		}
	}

	/**
	 * Get field from class or parent classes
	 * @param classInstance
	 * @param fieldName
	 * @return
	 */
	public static Field getField(Object classInstance, String fieldName) {
		int maxLevel = 0;
		Class<?> currClass = classInstance.getClass();
		Field field = null;
		//
		while (field == null && maxLevel <= 4){
			try	{
				//currClass.getDeclaredField(fieldName);
				field = currClass.getDeclaredField(fieldName);
			} catch(NoSuchFieldException e){
				currClass = currClass.getSuperclass();
				maxLevel++;
			} catch(NullPointerException n){
				break;
			}
		}
		return field;
	}

	/**
	 * @param myObject
	 * @param methodName
	 * @return
	 */
/*	public static Object invokeGetterMethode(Object myObject, String methodName){
		try {
			return invokeMethode(myObject, methodName, null, null);
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
		}

		return null;
	}*/

	/**
	 * @param bean
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Object setProperty(Object bean, String fieldName, Object value) {
		try {
			PropertyUtils.setProperty(bean, fieldName, value);
		} catch (Exception e) {
//			LOGGER.warn("This field " + fieldName + " has a probleme in " + bean + " object ! ->"+e);
		}

		return bean;
	}

	/**
	 * @param bean
	 * @param fieldName
	 * @return
	 */
	public static String getStringPropertieValue(Object bean, String propertieName) {
		try {
			return BeanUtils.getProperty(bean, propertieName);
		} catch (Exception e) {
//			LOGGER.error("Erreur : ", e);
		}

		return null;
	}

	/**
	 * @param bean
	 * @param propertieName
	 * @return
	 * @throws Exception
	 */
	public static Object getObjectPropertieValue(Object bean, String propertieName) {
		if (bean != null && !StringUtil.isEmpty(propertieName)) {
			try {
				BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
				return wrapper.getPropertyValue(propertieName);
			} catch (Exception e) {
//				LOGGER.error("Erreur : ", e);
			}
		}

		return null;
	}

	/**
	 * @param myObject
	 * @param methodName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Method getMethod(Class myClass, String methodName, Class[] argsType) {
		if ((myClass == null) || StringUtil.isEmpty(methodName)) {
			return null;
		}

		if (argsType == null) {
			argsType = new Class[0];
		}

		Class myItClass = myClass;
		while (!myItClass.equals(Object.class)){
			try {
				return myItClass.getMethod(methodName, argsType);
			} catch (Exception e) {
				// If method not founded, exception
			}			
			myItClass = myItClass.getSuperclass();
		}
		
		// If methose was not founder by calling, find by name
		try {
			// TODO Ameliorer pour appeler aussi avec les argument mais faut resoudre le probleme des generiques
			BeanInfo oldBeanInfo = Introspector.getBeanInfo(myClass);
			MethodDescriptor[] methodes = oldBeanInfo.getMethodDescriptors();
			for(MethodDescriptor methodeDesc : methodes){
				if(methodeDesc.getDisplayName().equals(methodName)){
					return methodeDesc.getMethod();
				}
			}
		} catch (Exception e) {
			// If method not founded, exception
		}
		
		return null;
	}
	
	/**
	 * @param myObject
	 * @param methodName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isMethodExist(Object myObject, String methodName, Class[] argsType) {

		if ((myObject == null) || StringUtil.isEmpty(methodName)) {
			return false;
		}

		try {
			if (argsType == null) {
				argsType = new Class[0];
			}

			Class myClass = myObject.getClass();
			Method method = myClass.getMethod(methodName, argsType);//myClass.getDeclaredMethod(methodName, argsType);
			if (method == null) {
				return false;
			} else {
				myClass = null;
				argsType = null;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/** --------------------------------------------------------------------- * */

	/**
	 * @param instance
	 * @return
	 */
	public static List<String> getFieldsName(Class<?> instance) {
		List<String> listFieldName = new ArrayList<String>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(instance);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor prop = propertyDescriptors[i];
				listFieldName.add(prop.getName());
			}
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
		}

		return listFieldName;
	}

	/**
	 * @param methodeName
	 * @return
	 * @throws Exception
	 */
	public static void callComponentFieldOrMethode(Component component, String methodeName, Object value) throws Exception{
		String name = null;
		if(methodeName.startsWith("set")){
			name = methodeName.substring(3);
		} else if(methodeName.startsWith("get")){
			name = methodeName.substring(3);
		} else if(methodeName.startsWith("is")){
			name = methodeName.substring(2);
		}
		//
		if(name != null){
			name = name.substring(0, 1).toLowerCase() + name.substring(1);
			ReflectUtil.setProperty(component, name, value);
		} else{
			Class<?>[] classArgs = (value==null) ? null : new Class[]{value.getClass()};
			Object[] objectArgs = (value==null) ? null : new Object[]{value};
			// Invoke
			ReflectUtil.invokeMethode(component, methodeName, objectArgs , classArgs);
		}
	}

//------------------------------------------------ List fields by annotation --------------------------------------------------

	/**
	 * @param currClass
	 * @param annotation
	 * @return
	 */
	public static List<Field> getListFieldByAnnotation(Class<?> currClass, Class<?> annotation){
		List<Field> listField = null;
		Map<String, Field> mapField = getMapFieldByAnnotation(currClass, annotation);
		//
		if(ServiceUtil.isNotEmpty(mapField)){
			listField = new ArrayList<Field>();
			for(String key : mapField.keySet()){
				listField.add(mapField.get(key));
			}
		}

		return listField;
	}

	/**
	 * @param interfaces
	 * @return
	 */
//	private static boolean containsIViewBeanOrPersistant(Field field){
//		String endPersistant = StrimUtil.getGlobalConfigPropertie("persistant.end");
//		if(field.getType().getSimpleName().endsWith(endPersistant)){
//			return true;
//		} else{
//			Class<?>[] interfaces = field.getType().getInterfaces();
//			//
//			for(Class<?> currInterface : interfaces){
//				if(currInterface.equals(IViewBean.class)){
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	/**
	 * @param currClass
	 * @param annotation
	 * @return
	 */
	public static Map<String, Field> getMapFieldByAnnotation(Class<?> currClass, Class<?> annotation) {
		String path = "";
		Map<String, Field> listFields = new HashMap<String, Field>();
		return getMapFieldByAnnotation(listFields, currClass, annotation, path);
	}

	/**
	 * @param globalListFields
	 * @param fields
	 * @param annotation
	 */
	@SuppressWarnings("unchecked")
	private static void addMapFieldByAnnotation(Map<String, Field> globalListFields, Field[] fields, Class annotation, String path){
		Map<String, Field> mapFields = new HashMap<String, Field>();
		for(Field field : fields){
//			if(containsIViewBeanOrPersistant(field)){
//				getMapFieldByAnnotation(globalListFields, field.getType(), annotation, (path+field.getName()+"."));
//			}

			if(field.getAnnotation(annotation) != null){
				mapFields.put((path + field.getName()), field);
			}
		}
		//
		if(mapFields.size() == 0){
			mapFields = null;
		} else{
			globalListFields.putAll(mapFields);
		}
	}

	/**
	 * @param globalListFields
	 * @param currClass
	 * @param annotation
	 * @return
	 */
	private static Map<String, Field> getMapFieldByAnnotation(Map<String, Field> globalListFields, Class<?> currClass, Class<?> annotation, String path) {
		int idx = 0;
		// Pour eviter la boucle infinie
		String[] pathArray = StringUtil.getArrayFromStringDelim(path, ".");
		boolean isMaxOK = ((pathArray==null) || (pathArray.length<=MAX_LEVEL));

		//
		while (!currClass.equals(Object.class) && isMaxOK){
			String className = StringUtil.firstCharToLowerCase(currClass.getSimpleName());
			// Get full path
			String endBean = StrimUtil.getGlobalConfigPropertie("bean.end");
			if((idx == 0) && (endBean != null) && (className.indexOf(endBean) != -1)){
				className = className.substring(0, className.indexOf(endBean));
				path = path + className + ".";
			}

			addMapFieldByAnnotation(globalListFields, currClass.getDeclaredFields(), annotation, path);
			currClass = currClass.getSuperclass();
			idx++;
		}
		return globalListFields;
	}

	/**
	 * Return class of generic param
	 * <br> Ex : MyClass<B, T, E>
	 * @param myclass
	 * @param genericIdx : Index of generic parameter. Ex : B = index 0
	 * @return
	 */
	public static Class<?> getGenericArgsClass(Class<?> myclass, int genericIdx){
		Class<?> genericClass = null;
		
		
		// For guice
//		if (myclass.getName().contains("EnhancerByGuice")) {
//			myclass = myclass.getSuperclass();
//		}
		
		
        // Get the class name of this instance's type.
        ParameterizedType pt
            = (ParameterizedType) myclass.getGenericSuperclass();
        // You may need this split or not, use logging to check
        String parameterClassName
            = pt.getActualTypeArguments()[genericIdx].toString().split("\\s")[1];
        // Instantiate the Parameter and initialize it.
        try {
        	genericClass = Class.forName(parameterClassName);
		} catch (ClassNotFoundException e1) {
		}

		return genericClass;
	}

//---------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @param bean
	 * @param fieldName
	 * @param value
	 * @return
	 */
	public static Object cloneBean(Object bean) {
		try {
			return BeanUtils.cloneBean(bean);
		} catch (Exception e) {
			LOGGER.error("Erreur : ", e);
		}

		return null;
	}
	
	public static void copyProperties(Object destination, Object origine) {
		try {
			BeanUtils.copyProperties(destination, origine);
		} catch (Exception e) {
//			LOGGER.error("Erreur : ", e);
		}
	}

	/**
	 * @param millis
	 */
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			LOGGER.error("Erreur : ", e);
		}
	}
}
