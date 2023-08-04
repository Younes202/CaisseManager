package framework.controller.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import framework.model.common.constante.ProjectConstante;

@Documented
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface WorkForward {
	boolean useBean() default false;
	boolean useFormValidator() default true;
	Class bean() default ProjectConstante.DEFAULT.class;
}