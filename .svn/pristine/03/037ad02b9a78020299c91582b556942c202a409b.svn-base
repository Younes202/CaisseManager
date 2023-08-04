package framework.model.common.service.audit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import framework.model.common.util.DateUtil;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.StrimUtil;
import framework.model.common.util.StringUtil;

public class FormattersBuilder {

	DataFormatter dateFormatter = new DataFormatter() {
		@Override
		public String format(Object entity, Method method) {
			String fieldName = StringUtil.firstCharToLowerCase(method.getName().substring(3));
			Field field = ReflectUtil.getField(entity, fieldName);

			if(field != null && field.getType().equals(Date.class)){
				Date value = (Date)ReflectUtil.getObjectPropertieValue(entity, fieldName);
				String patternSt = StrimUtil.getGlobalConfigPropertie("ddMMyyyy.format");
				String dateToString = DateUtil.dateToString(value, patternSt);

				return dateToString;
			}
			return null;
		}
	};

	DataFormatter booleanFormatter = new DataFormatter() {
		@Override
		public String format(Object entity, Method method) {
			String fieldName = StringUtil.firstCharToLowerCase(method.getName().substring(3));
			Field field = ReflectUtil.getField(entity, fieldName);

			if(field.getType().equals(Integer.class)){
				String value = ReflectUtil.getStringPropertieValue(entity, fieldName);
				Column columnAnnot = field.getAnnotation(Column.class);
				//
				if(columnAnnot != null){
					if(columnAnnot.length() == 1){
						return "1".equals(value) ? "Oui" : "Non";
					}
				}
			}
			return null;
		}
	};

	DataFormatter typeEnumeFormatter = new DataFormatter() {
		@Override
		public String format(Object entity, Method method) {
			String fieldName = StringUtil.firstCharToLowerCase(method.getName().substring(3));

			if(fieldName.startsWith("te_type")){
				String value = ReflectUtil.getStringPropertieValue(entity, fieldName);
				return "AFAIRE_"+value;
			}
			return null;
		}
	};
	
	/*\****************************** Build formatters ****************************************\*/
	private static List<DataFormatter> listFormatter;
	
	static {
		listFormatter = new ArrayList<DataFormatter>();
		// Add formatters
		FormattersBuilder fl = new FormattersBuilder();
		Field[] fFields = fl.getClass().getDeclaredFields();
		//
		for(Field field : fFields){
			try {
				if(field.getType().equals(DataFormatter.class)){
					listFormatter.add((DataFormatter) field.get(fl));
				}
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public static List<DataFormatter> getListFormatter(){
		return listFormatter;
	}
}
