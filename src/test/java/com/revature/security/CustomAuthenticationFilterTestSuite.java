//package com.revature.security;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import com.revature.security.CustomAuthenticationFilter;
//import com.revature.security.ZuulConfig;
//
///**
// * Test suite for class CustomAuthenticationFilter
// * 
// * @author Alonzo Muncy (190107-Java-Spark-USF)
// *
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class CustomAuthenticationFilterTestSuite {
//	
//	@Mock
//	ZuulConfig mockZuulConfig;
//	
//	@Mock
//	HttpServletRequest mockHttpServletRequest;
//	
//	@Mock
//	HttpServletResponse mockHttpServletResponse;
//	
//	@Mock
//	FilterChain mockFilterChain;
//	
//	@InjectMocks
//	CustomAuthenticationFilter testClass = new CustomAuthenticationFilter(mockZuulConfig);
//	
//	
///**
// * This is for testing the do filter. We are not testing the servlet request or the servlet response, 
// * or the filter chain. So we input mock objects. As we are unit testing, we are going to stub validate 
// * header to return true when our valid response is passed to it.
// */
//	@Test
//	public void testDoFilterActuatorTrue() {
//		
//		//Getting past the first if statement.
//		when(mockHttpServletRequest.getRequestURI()).thenReturn("/project/actuator");
//		
//		String validResponse = testClass.get_SHA_512_SecureHash("Secret", "Salt");
//		
//		//When ZuulConfig asks for header, then respond with our valid response.
//		when(mockZuulConfig.getHeader()).thenReturn(validResponse);
//		when(mockHttpServletRequest.getHeader(validResponse)).thenReturn(validResponse);
//		try {
//		testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
//			verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServletException e) {
//			e.printStackTrace();
//		}
//		
//	}
//	
//	/**
//	 * This is if the actuator is false, on doFilter
//	 */
//@Test
//public void testDoFilterActuatorFalse() {
//		
//		//Getting past the first if statement.
//		when(mockHttpServletRequest.getRequestURI()).thenReturn("/somethingElse");
//		when(mockZuulConfig.getSecret()).thenReturn("Secret");
//		when(mockZuulConfig.getSalt()).thenReturn("Salt");
//		String validResponse = testClass.get_SHA_512_SecureHash("Secret", "Salt");
//		
//		//When ZuulConfig asks for header, then respond with our valid response.
//		when(mockZuulConfig.getHeader()).thenReturn(validResponse);
//		when(mockHttpServletRequest.getHeader(validResponse)).thenReturn(validResponse);
//		try {
//		testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
//			verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ServletException e) {
//			e.printStackTrace();
//		}
//		
//	}
///**
// * Testing for invalid credentials
// */
//@Test
//public void testDoFilterInvalidHeader() {
//	when(mockHttpServletRequest.getRequestURI()).thenReturn("/somethingElse");
//	try {
//		testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
//		verify(mockHttpServletRequest, times(1)).getHeader("X-FORWARDED-FOR");
//	} catch (IOException e) {
//		e.printStackTrace();
//	} catch (ServletException e) {
//		e.printStackTrace();
//	}
//}
//
//}

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.revature.security.CustomAuthenticationFilter;
import com.revature.security.ZuulConfig;

/** Test suite for class CustomAuthenticationFilter */
@RunWith(MockitoJUnitRunner.class)
public class CustomAuthenticationFilterTestSuite {

  @Mock ZuulConfig mockZuulConfig;

  @Mock HttpServletRequest mockHttpServletRequest;

  @Mock HttpServletResponse mockHttpServletResponse;

  @Mock FilterChain mockFilterChain;

  @InjectMocks
  CustomAuthenticationFilter testClass = new CustomAuthenticationFilter(mockZuulConfig);

  /**
   * This is for testing the do filter. We are not testing the servlet request or the servlet
   * response, or the filter chain. So we input mock objects. As we are unit testing, we are going
   * to stub validate header to return true when our valid response is passed to it.
   */
  @Test
  public void testDoFilterActuatorTrue() {

    // Getting past the first if statement.
    when(mockHttpServletRequest.getRequestURI()).thenReturn("/project/actuator");

    String validResponse = testClass.get_SHA_512_SecureHash("Secret", "Salt");

    // When ZuulConfig asks for header, then respond with our valid response.
    when(mockZuulConfig.getHeader()).thenReturn(validResponse);
    when(mockHttpServletRequest.getHeader(validResponse)).thenReturn(validResponse);
    try {
      testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
      verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }

  /** This is if the actuator is false, on doFilter */
  @Test
  public void testDoFilterActuatorFalse() {

    // Getting past the first if statement.
    when(mockHttpServletRequest.getRequestURI()).thenReturn("/somethingElse");
    when(mockZuulConfig.getSecret()).thenReturn("Secret");
    when(mockZuulConfig.getSalt()).thenReturn("Salt");
    String validResponse = testClass.get_SHA_512_SecureHash("Secret", "Salt");

    // When ZuulConfig asks for header, then respond with our valid response.
    when(mockZuulConfig.getHeader()).thenReturn(validResponse);
    when(mockHttpServletRequest.getHeader(validResponse)).thenReturn(validResponse);
    try {
      testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
      verify(mockFilterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }
  /** Testing for invalid credentials */
  @Test
  public void testDoFilterInvalidHeader() {
    when(mockHttpServletRequest.getRequestURI()).thenReturn("/somethingElse");
    try {
      testClass.doFilter(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);
      verify(mockHttpServletRequest, times(1)).getHeader("X-FORWARDED-FOR");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    }
  }
}