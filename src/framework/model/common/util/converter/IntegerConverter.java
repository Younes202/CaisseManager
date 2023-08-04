package framework.model.common.util.converter;

import org.apache.commons.beanutils.Converter;

public class IntegerConverter implements Converter {
	@SuppressWarnings("unchecked")
	public Integer convert(Class type, Object value) {
		 if(value != null){
			 if("true".equals(""+value)){
				 return new Integer(1);
			 } else if("false".equals(""+value)){
				 return new Integer(0);
			 }
		 }
		 
		 return (Integer)value;
	  }
}
