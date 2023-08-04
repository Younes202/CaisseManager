package framework.controller.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

@Documented
@Inherited
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Service
public @interface WorkValidator {
	String alias();
}