package framework.model.common.util.converter;

import org.apache.commons.beanutils.Converter;

import framework.model.common.util.NumericUtil;
import framework.model.common.util.StringUtil;

public class FloatConverter implements Converter {
	public Float convert(Class type, Object value) {
		return (Float)(StringUtil.isEmpty(value) ? null : NumericUtil.toFloat(value));
	}
}
