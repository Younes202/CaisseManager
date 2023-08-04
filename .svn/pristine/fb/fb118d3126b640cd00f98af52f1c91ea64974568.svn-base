package framework.model.common.service.audit;

import java.beans.PropertyDescriptor;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StringUtil;

public class AuditUtil {
	/**
	 * @param value
	 * @param separator
	 * @return
	 */
	public static String removeLastToken(String value, String separator){
		StringTokenizer st = new StringTokenizer(value, separator);
		String retour = "";
		int i = 0;
		int count = st.countTokens();
		//
		while(st.hasMoreTokens()){
			String s = st.nextToken();
			if(i < count-1){
				retour = retour + s + separator;
			}
			i++;
		}

		return retour;
	}
	
	/**
	 * @param PropertyDescriptorArray
	 * @param readMethodName
	 * @return
	 */
	public static PropertyDescriptor getPropertyDescriptor(PropertyDescriptor[] PropertyDescriptorArray, String readMethodName){
		for(PropertyDescriptor prop : PropertyDescriptorArray){
			if(prop.getReadMethod().getName().equals(readMethodName)){
				return prop;
			}
		}
		return null;
	}
	
	/**
	 * Définir si la propriété en cours est une entitie
	 * @param objClass
	 * @return
	 */
	public static boolean isEntity(Class objClass){
		return objClass.getSimpleName().endsWith("Persistant");
	}
	
	/**
	 * @param objClass
	 * @return
	 */
	public static boolean isCollection(Class objClass){
		return objClass.equals(Set.class);
	}

	/**
	 * Test la valeur à changé ou nom
	 * @param val1
	 * @param val2
	 * @return
	 */
	public static boolean isVauesChanged(Object val1, Object val2){
		if(StringUtil.isNotEmpty(val1) || StringUtil.isNotEmpty(null)){
			if(StringUtil.isNotEmpty(val1) && !((""+val1).trim()).equals((""+val2).trim())){
				return true;
			} else if(StringUtil.isNotEmpty(val2) && !((""+val2).trim()).equals((""+val1).trim())){
				return true;
			}
		} else{
			return false;
		}

		return false;
	}

	/**
	 * Retourne l'entity d'un Set
	 * @param set
	 * @param element
	 * @return
	 */
	public static Object getEntityFromSet(Set set, Object element) {
		if(set != null){
		    for (Iterator it = set.iterator(); it.hasNext();) {
		        Object current = it.next();
				// If entity was changed
				if(isEntitiesEquals(current, element)){
		        	return current;
			    }
		    }
		}

		return null;
	}
	
	/**
	 * @param entity1
	 * @param entity2
	 * @return
	 */
	public static boolean isEntitiesEquals(Object entity1, Object entity2){
		if(entity2 == null){
			return false;
		}
		
		Long entityId1 = (Long)ReflectUtil.getObjectPropertieValue(entity1, "id");
		Long entityId2 = (Long)ReflectUtil.getObjectPropertieValue(entity2, "id");
		
		return (""+entityId1).equals(""+entityId2);
	}

}
