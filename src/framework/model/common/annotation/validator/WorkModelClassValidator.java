package framework.model.common.annotation.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface WorkModelClassValidator {
	/**
	 * Validator of mmodel
	 * @return
	 */
	Class<?> validator();// default ProjectConstante.DEFAULT.class;
}
