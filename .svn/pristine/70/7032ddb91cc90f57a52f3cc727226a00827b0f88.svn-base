//package model.common.service.audit;
//import java.beans.BeanInfo;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//
//import javax.inject.Named;
//import javax.persistence.EntityManager;
//
//import org.springcontext.annotation.Scope;
//
//import model.common.util.ControllerBeanUtil;
//import model.common.util.ReflectUtil;
//import model.common.util.StrimUtil;
//import model.common.util.StringUtil;
//
//@Named
//@Scope("prototype")// Trés important pour éviter le singleton
//public class EntitiesComparatorService {
//	// Custom config
////	private static final String LABEL_SEPARATOR = "-->";
//	private final String[] excludedMethods = {"getId", "getDate_maj", "getSignature", "getB_left", "getB_right", "getIs_masque"};
//	
////	private List<AuditBean> listDiff = new ArrayList<AuditBean>();
//	private Properties mapLabels = StrimUtil.getLabelsMap();
////	private String attributeName;
//
//	/**
//	 * @param newEntity
//	 * @param oldEntity
//	 * @param mapLabels
//	 */
////	public List<AuditBean> compareEntities(Object newEntity, Object oldEntity, Properties mapLabels){
////		//this.mapLabels = mapLabels;
////		this.listDiff = compareEntities(newEntity, oldEntity);
////		
////		return this.listDiff; 
////	}
//
//	/**
//	 * @param newEnt
//	 * @param oldEnt
//	 * @return
//	 */
////	private List<AuditBean> compareEntities(Object newEntity, Object oldEntity){
////		try {
////			// Si la nouvelle entitie et l'ancienne sont null alors arrêt du traitement
////			if(newEntity == null && oldEntity == null){
////				return listDiff;
////			}
////
////			// Construction des libellés en profondeur
////			String mapLabel = getLabelFromProperties(newEntity);
////			this.attributeName = StringUtil.getValueOrEmpty(this.attributeName) + mapLabel + LABEL_SEPARATOR;
////
////			// Bean info et Bean properties
////			BeanInfo newBeanInfo = Introspector.getBeanInfo(newEntity.getClass());
////			PropertyDescriptor[] newPropertyDescriptors = newBeanInfo.getPropertyDescriptors();
////
////			// Iteration sur les propriétés des entities
////			for (int i = 0; i < newPropertyDescriptors.length; i++) {
////				PropertyDescriptor newPropertyDescriptor = newPropertyDescriptors[i];
////				PropertyDescriptor oldPropertyDescriptor = null;
////				Method newReadMethod = newPropertyDescriptor.getReadMethod();
////
////				// Si le getter et setter sont présent dans l'objet
////				if ((newPropertyDescriptor.getWriteMethod() != null) && (newPropertyDescriptor.getReadMethod() != null)){
////					// If not excluded
////					if(!StringUtil.contains(newReadMethod.getName(), excludedMethods)) {
////						//
////						if(oldEntity != null){
////							BeanInfo oldBeanInfo = Introspector.getBeanInfo(oldEntity.getClass());
////							PropertyDescriptor[] oldPropertyDescriptors = oldBeanInfo.getPropertyDescriptors();
////							oldPropertyDescriptor = AuditUtil.getPropertyDescriptor(oldPropertyDescriptors, newReadMethod.getName());
////						}
////						// Scan and save
////						scanAndGetDiff(newEntity, oldEntity, newPropertyDescriptor, oldPropertyDescriptor);
////					}
////				}
////			}
////
////			// Mise à jour de la profondeur
////			this.attributeName = AuditUtil.removeLastToken(this.attributeName, LABEL_SEPARATOR);
////
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		return listDiff;
////	}
//
//	/**
//	 * @param newEntity
//	 * @param oldEntity
//	 * @param newReadMethod
//	 * @param oldReadMethod
//	 * @throws IllegalArgumentException
//	 * @throws IllegalAccessException
//	 * @throws InvocationTargetException
//	 */
////	private void scanAndGetDiff(Object newEntity, Object oldEntity, PropertyDescriptor newProperty, PropertyDescriptor oldProperty) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
////		Method newReadMethod = newProperty.getReadMethod();
////		Class<?> newReturnType = newReadMethod.getReturnType();
////		Object newReturnValue = newReadMethod.invoke(newEntity, new Object[] {});
////		Object oldReturnValue = null;
////
////		if(oldProperty != null){
////			Method oldReadMethod = oldProperty.getReadMethod();
////			oldReturnValue = oldReadMethod.invoke(oldEntity, new Object[] {});
////		}
////
////		// 1- Id return is an entity type
////		if(AuditUtil.isEntity(newReturnType)){
////			if((newReturnValue == null) && (oldReturnValue == null)){
////				return;
////			}
////
////			// If entity was changed
////			if(!AuditUtil.isEntitiesEquals(newReturnValue, oldReturnValue)){
////				String newDesciption = getEntityDescription(newReturnValue);
////				String oldDesciption = getEntityDescription(oldReturnValue);
////				//
////				String attrName = getAttributeName(newReturnValue);
////				// Add bean
////				addAuditBean(attrName, "["+newDesciption+"]", "["+oldDesciption+"]");
////			} else{
////				compareEntities(newReturnValue, oldReturnValue);
////			}
////		}
////		// 2- Si le retour est un Set d'objets
////		else if(AuditUtil.isCollection(newReturnType)){
////			Set<?> newDataSet = (Set<?>)newReturnValue;
////			Set<?> oldDataSet = (Set<?>)oldReturnValue;
////			//
////			boolean isNewEmpty = newDataSet == null || newDataSet.size()==0;
////			boolean isOldEmpty  = oldDataSet == null || oldDataSet.size()==0;
////			if(isNewEmpty && isOldEmpty ){
////				return;
////			}
////			// New DIFF
////			if(!isNewEmpty){
////				for(Object bEntity : newDataSet){
////					Object sEntity = AuditUtil.getEntityFromSet(oldDataSet, bEntity);
////					if(sEntity == null){
////						String attrName = getAttributeName(bEntity);
////						addAuditBean(attrName, "["+getEntityDescription(bEntity)+"]", null);
////					}
////				}
////			}
////			// Old DIFF
////			if(!isOldEmpty){
////				for(Object bEntity : oldDataSet){
////					Object sEntity = AuditUtil.getEntityFromSet(newDataSet, bEntity);
////					if(sEntity == null){
////						String attrName = getAttributeName(bEntity);
////						addAuditBean(attrName, null, "["+getEntityDescription(bEntity)+"]");
////					}
////				}
////			}
////			// Compare
////			if(!isNewEmpty && !isOldEmpty){
////				for(Object bEntity : newDataSet){
////					Object sEntity = AuditUtil.getEntityFromSet(oldDataSet, bEntity);
////					//
////					if(sEntity != null){
////						compareEntities(sEntity, bEntity);
////					}
////				}
////			}
////		}
////		// 3- Si le retour est un type simple
////		else{
////			AuditBean auditBean = applyFormattersAndBuildAuditBean(newEntity, oldEntity, newProperty, oldProperty);
////			if(auditBean != null){
////				this.listDiff.add(auditBean);
////			}
////		}
////	}
//
//	/**
//	 * @param newEntity
//	 * @param oldEntity
//	 * @param newReadMethod
//	 * @param oldReadMethod
//	 * @return
//	 * @throws InvocationTargetException
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 */
////	private AuditBean applyFormattersAndBuildAuditBean (Object newEntity, Object oldEntity, PropertyDescriptor newProperty, PropertyDescriptor oldProperty) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
////		Method newReadMethod = newProperty.getReadMethod();
////		Method oldReadMethod = oldProperty.getReadMethod();
////		Object newReturnValue = newReadMethod.invoke(newEntity, new Object[] {});
////		Object oldReturnValue = oldReadMethod.invoke(oldEntity, new Object[] {});
////
////		List<DataFormatter> listFormatter = FormattersBuilder.getListFormatter();
////
////		// Apply formatters to new entity
////		for(DataFormatter formatter : listFormatter){
////			String convertedValue = formatter.format(newEntity, newReadMethod);
////			if(convertedValue != null){
////				newReturnValue = convertedValue;
////				break;
////			}
////		}
////		// Apply formatters to old entity
////		if(oldEntity != null){
////			for(DataFormatter formatter : listFormatter){
////				String convertedValue = formatter.format(oldEntity, oldReadMethod);
////				if(convertedValue != null){
////					oldReturnValue = convertedValue;
////					break;
////				}
////			}
////		}
////
////		String oldValue = StringUtil.getValueOrEmpty(newReturnValue);
////		String newValue = StringUtil.getValueOrEmpty(oldReturnValue);
////		
////		if (AuditUtil.isVauesChanged(oldValue, newValue)) {
////			String label = newProperty.getName();
////			String propLabel = (String)mapLabels.get(this.attributeName.replaceAll(LABEL_SEPARATOR, ".") + label);
////			if(StringUtil.isNotEmpty(propLabel)){
////				label = propLabel;
////			}
////			return new AuditBean(label, oldValue, newValue);
////		}
////		
////		return null;
////	}
//
//	/**
//	 * Construire la description de l'entitie
//	 * @return description de l'entitie avec séparateur
//	 */
//	public Map<String, String> getEntityDescription(Object entity, EntityManager em){
//		if(entity == null){
//			return null;
//		}
//		Map<String, String> mapDiff = new HashMap<String, String>();
//
//		try {
//			// Bean info et Bean properties
//			BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
//			PropertyDescriptor[] propertyDescriptors1 = beanInfo.getPropertyDescriptors();
//			// Construction des libellés en profondeur
//			String entityName = ControllerBeanUtil.getAliasBeanByClass(entity.getClass());
//
//			// Iteration sur les propriétés du bean
//			for (int i = 0; i < propertyDescriptors1.length; i++) {
//				PropertyDescriptor prop = propertyDescriptors1[i];
//				Method writeMethod = prop.getWriteMethod();
//				Method readMethod = prop.getReadMethod();
//
//				// Si le getter et setter sont présent dans l'objet
//				if ((writeMethod != null) && (readMethod != null) && !StringUtil.contains(readMethod.getName(), excludedMethods)) {
//					Class<?> returnType = readMethod.getReturnType();
//					// Objet de retour des méthodes
//					Object returnValue = readMethod.invoke(entity, new Object[] {});
//
//					// 1- Si le retour est une entitie on rappel la méthode
//					if(!AuditUtil.isEntity(returnType) && !returnType.equals(Set.class)){
//						String propLabel = (String)mapLabels.get(StringUtil.firstCharToLowerCase(entityName) + "." + prop.getName());
//						if(StringUtil.isEmpty(propLabel)){
//							propLabel = prop.getName();
//						}
//						// Value new
//						String convertedValue = null;
//						List<DataFormatter> listFormatter = FormattersBuilder.getListFormatter();
//						//
//						for(DataFormatter formatter : listFormatter){
//							convertedValue = formatter.format(entity, readMethod);
//							if(convertedValue != null){
//								returnValue = convertedValue;
//								break;
//							}
//						}
//						//-------------------------------------------------------------
//						if(StringUtil.isNotEmpty(returnValue)){
//							mapDiff.put(propLabel, ""+returnValue);
//						}
//					} else if(AuditUtil.isEntity(returnType)){
//						String propLabel = (String)mapLabels.get(StringUtil.firstCharToLowerCase(entityName) + "." + prop.getName());
//						if(StringUtil.isEmpty(propLabel)){
//							propLabel = prop.getName();
//						}
//						returnValue = ReflectUtil.getObjectPropertieValue(returnValue, "id");
//						if(StringUtil.isNotEmpty(returnValue)){
//							// Get from id
//							Object entityReturn = em.find(returnType, returnValue);
//							returnValue = ReflectUtil.getObjectPropertieValue(entityReturn, "libelle");
//							
//							
//							mapDiff.put(propLabel, ""+returnValue);
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return mapDiff;
//	}
//
//	/**
//	 * @param entity
//	 * @return
//	 */
////	private String getAttributeName(Object entity) {
////		if(entity == null){
////			return null;
////		}
////		String currEntityLabel = this.attributeName + getLabelFromProperties(entity);
////		String attrName = "["+currEntityLabel.substring(currEntityLabel.indexOf(LABEL_SEPARATOR))+"]";
////
////		return attrName;
////	}
//
//	/**
//	 * Retourne le libellé à partir des libellés de l'écran ou à partir du nom de l'entitie
//	 * @param dependEntity
//	 * @return
//	 */
////	private String getLabelFromProperties(Object dependEntity) {
////		String depClassName = ControllerBeanUtil.getAliasBeanByClass(dependEntity.getClass());
////		String currMapLabel = (String)mapLabels.get(depClassName);
////		//
////		if(StringUtil.isEmpty(currMapLabel)){
////			currMapLabel = depClassName;
////		}
////		return currMapLabel;
////	}
//
//	/**
//	 * @param label
//	 * @param oldValue
//	 * @param newValue
//	 */
////	private void addAuditBean(String label, Object oldValue, Object newValue){
////		AuditBean auditBean = new AuditBean(label, StringUtil.getValueOrEmpty(oldValue), StringUtil.getValueOrEmpty(newValue));
////		this.listDiff.add(auditBean);
////	}
//
//}