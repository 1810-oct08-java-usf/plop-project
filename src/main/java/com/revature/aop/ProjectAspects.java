package com.revature.aop;

import java.time.LocalTime;
import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Currently used to get extra debug information without having
 * to scatter code throughout service.
 * @author Joshua (unknown batch)
 *
 */

@Aspect
@Component
public class ProjectAspects {
	
	long now;
	
    @Pointcut("execution(* com.revature..*(..))")
    public void logAll() {
    }
	
	@Before("logAll()")
	public void beforeExec(JoinPoint jp) {
		this.now = System.currentTimeMillis();
		System.out.println("[LOG] - [project-service]");
		System.out.println("	Before method: 		"+jp.getClass()+" "+jp.getSignature().getName());
		System.out.println("	Timestamp: 		"+LocalTime.now());
		System.out.println("	Input arguments: 	"+Arrays.toString(jp.getArgs()));
	}
	
	@AfterReturning(pointcut="logAll()",returning="result")
	public void afterReturn(JoinPoint jp, Object result) {
		System.out.println("[LOG] - [project-service]");
		System.out.println("	After method: 			"+jp.getClass()+" "+jp.getSignature().getName());
		System.out.println("	Milliseconds to complete: 	"+(this.now-System.currentTimeMillis()));
		System.out.println("	Returned values: 		"+result+"\n");
	}
	
	
	/**
	 * Used for getting any errors thrown from methods
	 * @param joinPoint
	 * @param ex
	 * @author Joshua (unknown batch)
	 */
	@AfterThrowing(pointcut = "logAll()", throwing = "ex")
	public void errorOcurance(JoinPoint joinPoint, Exception ex){
		System.out.println("[ERROR][project-service] - Error caught: "+ex.getMessage());
	}
	

}
