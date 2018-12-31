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



@Aspect
@Configuration
public class ProjectAspects {
	
	//Logger logger =  LoggerFactory.getLogger(this.getClass());
	
//	@Before("execution(public * com.revature..*(..))")
//	public void beforeAnything(JoinPoint joinPoint) {
//		System.out.println("Before Anything" + joinPoint.getKind());
//		System.out.println(joinPoint.getStaticPart() + "\n");
//	}
	
	@AfterThrowing(pointcut = "execution(* com.revature..*(..))", throwing = "ex")
	public void errorOcurance(JoinPoint joinPoint, Exception ex){
		System.out.println("An Error Has Occured");
		ex.printStackTrace();
		//logger.error(ex.getMessage());
	}
	

}
