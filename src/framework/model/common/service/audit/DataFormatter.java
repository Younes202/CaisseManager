package framework.model.common.service.audit;

import java.lang.reflect.Method;

public abstract class DataFormatter {
	public abstract String format(Object entity, Method method);
}