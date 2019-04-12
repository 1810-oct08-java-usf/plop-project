package com.revature.aop;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

/**
 * Currently used to get extra debug information without having
 * to scatter code throughout service.
 * @author Joshua
 *
 */

@Aspect
@Configuration
public class ProjectAspects {
	
	/**
	 * Used for getting any errors thrown from methods
	 * @param joinPoint
	 * @param ex
	 */
	@AfterThrowing(pointcut = "execution(* com.revature..*(..))", throwing = "ex")
	public void errorOcurance(JoinPoint joinPoint, Exception ex){
		System.out.println("An Error Has Occured");
		ex.printStackTrace();
	}
	

}
