package framework.model.common.spring;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <code>ApplicationContextHolder</code> class.
 *
 * Important: this bean must be declared in a spring config file
 *
 * @author Foudil
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext context;

	public void setApplicationContext(ApplicationContext applicationcontext)
			throws BeansException {
		context = applicationcontext;
	}

	/**
	 * gets the spring application context
	 *
	 * The context will be initialized (using spring).
	 *
	 *
	 * @return application context
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}

}
