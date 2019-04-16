package com.revature.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.revature.aop.ProjectAspects;
import com.revature.repositories.ProjectRepository;

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
	public void testExeptionPrintStackTrace() {
		testProjectAspects.errorOcurance(mockJoinPoint, mockException);
		verify(mockException, times(1)).printStackTrace();
	}
}
