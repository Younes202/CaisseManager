package framework.model.common.annotation.job;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface Job {
	String code();
	String label() default "";
	String labelKey() default "";
	/**
	 * x_H=Hour  x_D=Day  x_W=Week  x_M=Month x_Y=Year (default no limit)
	 */
	String maxInstanceTime() default "-1";
	/**
	 * Simultaneously threads count (default 3)
	 */
	int maxInstance() default 2;
	
	String scriptExecuteAtEnd() default "";
}
