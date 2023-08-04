package framework.model.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import framework.model.common.GlobalModelThread;
import framework.model.common.annotation.validator.WorkModelClassValidator;
import framework.model.common.annotation.validator.WorkModelMethodValidator;
import framework.model.common.exception.ActionValidationException;
import framework.model.common.exception.BeanValidationException;
import framework.model.common.exception.ControllerException;
import framework.model.common.service.MessageService;
import framework.model.common.util.ReflectUtil;
import framework.model.common.util.ServiceUtil;
import framework.model.service.GenericJpaService;

@Aspect
public class ModelInterceptorAOP {
	ExecutorService executeService = Executors.newFixedThreadPool(10); 

	@Around("execution(public * *(..))")
	  public Object doAroundActions(ProceedingJoinPoint pjp) throws Throwable {	 
		 Object returnValue = null;
		 Signature signature = pjp.getStaticPart().getSignature();
		 String fullPathCalled = signature.toLongString();	 // Okk par index of IGenericService
		 
		 Class<?> classType = pjp.getSourceLocation().getWithinType(); // Classe appele
		 String methodName =  signature.getName();
		 boolean isGenDaoCall = "getGenriqueDao".equals(methodName);

		 // If is first call
		 if(!GlobalModelThread.getGlobalList().contains(fullPathCalled) && !isGenDaoCall){
			 Object[] args = pjp.getArgs();// Argumens de la mthode appele
//			 Object instanceService = pjp.getThis();// Proxy classe appele 
			 Method calledMethod = null;
//			 RequestAttributes currentReqAttributes = null;
//			 try{
//				 currentReqAttributes = RequestContextHolder.currentRequestAttributes();
//			 } catch(Exception e){
				// Quand on est dans des threads cette excepàtion ce produit donc on l'ignore tout simplement ici 
//			 }
			 WorkModelClassValidator classValidator = (WorkModelClassValidator) classType.getAnnotation(WorkModelClassValidator.class);
			 WorkModelMethodValidator methodValidator = null;

			 MethodSignature signature2 = (MethodSignature) pjp.getSignature();
			 calledMethod = signature2.getMethod();
			 Class<?>[] parameterTypes = calledMethod.getParameterTypes();
			 
			 // Refresh params --> Bug types
			 int idx = 0;
			 for (Class<?> class1 : parameterTypes) {
				if(class1.equals(Serializable.class) || class1.equals(Object.class)){
					if(args[idx] != null){
						parameterTypes[idx] = args[idx].getClass();
					}
				}
				idx++;
			}
			 
			 // Get  called method
			 calledMethod = ReflectUtil.getMethod(classType, methodName, parameterTypes); 
			 // Get method annotation
			 methodValidator = calledMethod.getAnnotation(WorkModelMethodValidator.class);
			 
			 // Add current action
			 if(fullPathCalled.indexOf("service.IGenericJpaService") != -1 && !methodName.equals("delete")){// Pour passer dans l'audit avec delete (bean) 
				 GlobalModelThread.getGlobalList().add(fullPathCalled);
			 }

			 // Validate bean
			 boolean isMethodeToValidate = (methodValidator != null) && methodValidator.methodValidate();
			 
			 // Validator class if methodValidate=true
			 if(isMethodeToValidate && (classValidator == null) && !calledMethod.getDeclaringClass().equals(GenericJpaService.class)){
				 throw new BeanValidationException("Class validator is required if 'methodValidate=true' in methodValidator annotation (" + classType+"."+methodName+")");
			 }

			// Validate action if validation annotation exist
			if(classValidator != null){
				 Class<?> validatorClass = classValidator.validator();
				//
				if(isMethodeToValidate){
					 boolean isValidate = validateMethode(validatorClass, methodName, parameterTypes, args);
					 // Throw exception if message is exist
					 if(!isValidate){
						 GlobalModelThread.getGlobalList().clear();
						 throw new ActionValidationException("Error in bean data !");
					 }
				 }
			 }

			 //
			 returnValue = pjp.proceed();
			
			 // If an error message exist or question message, throw error
			 if(MessageService.isQuestionExist() || MessageService.isError()){				 
				 GlobalModelThread.getGlobalList().clear();
				 throw new ActionValidationException("Question message or error message is exist !");
			 }
		 } else{
			 returnValue = pjp.proceed();
		 }

		 return returnValue;
	  }

	 /**
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ControllerException
	 */
	private boolean validateMethode(Class<?> validator, String methodName, Class<?>[] parameterTypes, Object[] args) throws InstantiationException, IllegalAccessException, ControllerException {
		Object validatorClass = ServiceUtil.getBusinessBean(validator);
		try {
			 Method calledMethod = ReflectUtil.getMethod(validatorClass.getClass(), methodName, parameterTypes); 
			if(calledMethod != null){
				calledMethod.invoke(validatorClass, args);
			}/* else{
				MessageService.addBannerMessage(MSG_TYPE.WARNING, "Method ** "+methodName+" ** not existe in validator ** "+validator.getSimpleName()+" ** !");
			}*/
		} catch (Exception e) {
			if(e.getCause().getClass().equals(ActionValidationException.class)){
				throw new ActionValidationException("Question message or error message is exist !");
			} else{
				throw new ControllerException(e);
			}
		} finally{
			validatorClass = null;
		}

		return !MessageService.isError();
	}
	
	@PersistenceUnit
	EntityManagerFactory emf;

	@After("execution(public * *(..))")
	public void doAfterCall(){
		 GlobalModelThread.getGlobalList().clear();
	}

	@Before("execution(public * *(..))")
	public void doBeforCall(){
		
	}
}
