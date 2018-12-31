package com.revature.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.revature.exceptions.SubversionAttemptException;

//public class CustomAuthenticationFilter {}

public class CustomAuthenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// get header and validate from request object.
		System.out.println("Printing the Servlet Request headers");
		Enumeration<String> ems = request.getAttributeNames();
		while (ems.hasMoreElements()) {
			String temp = ems.nextElement();
			System.out.println(temp + "    " + request.getAttribute(temp));
		}
		System.out.println("\n\n\n");
		System.out.println("Printing the HTTP Servlet Request headers");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Enumeration<String> ems2 = httpRequest.getHeaderNames();
		while (ems2.hasMoreElements()) {
			String temp = ems2.nextElement();
			System.out.println(temp + "    " + httpRequest.getHeader(temp));
		}
		System.out.println("URI: " + httpRequest.getRequestURI() + "  " + !httpRequest.getRequestURI().contains("actuator"));
		String headerZuul = httpRequest.getHeader("RPM_ZUUL_ACCESS_HEADER");
		System.out.println("Zuul header     " + headerZuul);
		System.out.println("Forwarded Port     " + httpRequest.getHeader("x-forwarded-port"));
		try {
			if (headerZuul == null
			    	//|| httpRequest.getHeader("x-forwarded-port") == null
				//|| !httpRequest.getHeader("x-forwarded-port").equals("8762")
				|| !headerZuul.equals("Trevin is a meanie")|| !httpRequest.getRequestURI().contains("actuator")) {

				System.out.println("Bad trevin");
				/*
				 * In case of attempted subversion around Zuul we want to invalidate the
				 * session, so we can guarantee that the user will not be authenticated
				 */

				SecurityContextHolder.clearContext();
				((HttpServletResponse) response).setStatus(401);
				// Log this
				throw new SubversionAttemptException("ZUUL header is " + headerZuul);
			} else {
				System.out.println("giving auth");
				Authentication auth = new AccessAuthenticationToken(headerZuul, "ROLE_USER", new ArrayList<>());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (SubversionAttemptException e) {
			/*
			 * TODO This should be refactored to log the failed authentication attempt,
			 * including the IP address of the requester.
			 */

			String ipAddress = ((HttpServletRequest) request).getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}

			String appUrl = request.getScheme() + "://" + request.getLocalAddr();
			System.out.println("URL     " + appUrl);
			// throw new RuntimeException(ipAddress + " " + appUrl, e);
		}
		filterChain.doFilter(request, response);
	}

	// A little bit change in the code :) , just to see if the PC is not connected
	// to any network
	// except that everything is same as before , just an extra 'if-else :)', I have
	// commented everything... Enjoy :) !

	public static void ipFind() throws UnknownHostException {

		Vector<String> Available_Devices = new Vector<>(); // stores the list of available/connected devices
		String myip = InetAddress.getLocalHost().getHostAddress(); // IP of the PC in which the code is
																	// running/localhost
		if (myip.equals("127.0.0.1")) {
			System.out.println("This PC is not connected to any network!");
		} else {
			String mynetworkips = new String(); // just a new string to store currently scanning ip

			// this loop finds the right most '.' of this PC's IP
			// suppose your PC's IP is 192.168.0.101, this loop finds the index of the '.'
			// just before '101'
			// and as soon as it finds the '.', it creates a new string(actually substring
			// of this PC's IP) starting at
			// index 0 and ending at index containing character '.' and exits from the loop
			// So here, if the IP was 192.168.0.101,mynetworkips will have the value
			// "192.168.0."
			for (int i = myip.length() - 1; i >= 0; --i) {
				if (myip.charAt(i) == '.') {
					// .substring(i,j) returns a string starting from index i and ending at index
					// j-1,so in order to
					// include '.' , i put (i+1)
					mynetworkips = myip.substring(0, i + 1);
					break;
				}
			}

			System.out.println("My Device IP: " + myip + "\n"); // Shows this PC's IP

			System.out.println("Search log:");

			// (loop bellow->) just add the string representation of i and add it to
			// mynetworkips to get full IP
			// for example, when i=1 the ip will be(if mynetworkips is "192.168.0.")
			// 192.168.0.1,
			// and then at next iteration it'll be 192.168.0.1
			// this means tis loop iterates over all possible ips and show you which one is
			// available or not.

			// you can change i's range if you know that your network's IPs start from
			// another
			// point(probably for most router(if not customized) , it will start from
			// 192.168.0.100)
			for (int i = 1; i <= 254; ++i) {
				try {
					// Create an InetAddrss object with new IP
					InetAddress addr = InetAddress.getByName(mynetworkips + new Integer(i).toString());

					if (addr.isReachable(1000)) { // See if it is reachable or simply available(check time is 1s=1000ms)
						System.out.println("Available: " + addr.getHostAddress()); // show that it is available
						Available_Devices.add(addr.getHostAddress()); // if available, add it to final list
					} else
						System.out.println("Not available: " + addr.getHostAddress()); // show that it is not available

				} catch (IOException ioex) {
					// nothing to do, just catch it if something goes wrong
				}
			}

			// print the list of available devices
			System.out.println("\nAll Connected devices(" + Available_Devices.size() + "):");
			for (int i = 0; i < Available_Devices.size(); ++i)
				System.out.println(Available_Devices.get(i));
		}
	}
}

