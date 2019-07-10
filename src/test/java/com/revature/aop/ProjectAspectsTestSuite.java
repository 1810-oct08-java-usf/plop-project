package com.revature.aop;

import org.aspectj.lang.JoinPoint;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test suite for ProjectAspects.
 * 
 * @author Alonzo Muncy (190107-Java-Spark-USF)
 */

@RunWith(MockitoJUnitRunner.class)
public class ProjectAspectsTestSuite {
	
	/**
	 * JoinPoint and Exception mocks
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	
	@Mock
	JoinPoint mockJoinPoint;
	
	@Mock
	Exception mockException;
	
	/**
	 * Don't mock the thing you are supposed to be testing.
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	@InjectMocks
	private ProjectAspects testProjectAspects;
	
	/**
	 * Assert that when called, the exception prints stack trace.
	 * 
	 * @author Alonzo Muncy (190107-Java-Spark-USF)
	 */
	@Test
	@Ignore
	public void testExeptionPrintStackTrace() {
		testProjectAspects.errorOccurance(mockJoinPoint, mockException);
	}
}