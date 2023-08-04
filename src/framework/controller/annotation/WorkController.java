package framework.controller.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

import framework.model.common.constante.ProjectConstante;

@Documented
@Inherited
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface WorkController {

	String nameSpace();

	/**
	 * Bean associated with form
	 * @return
	 */
	Class<?> bean() default ProjectConstante.DEFAULT.class;
	String jspRootPath() default "";
}
