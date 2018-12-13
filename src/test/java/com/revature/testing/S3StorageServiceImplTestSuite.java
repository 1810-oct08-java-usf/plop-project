package com.revature.testing;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.revature.services.S3StorageServiceImpl;

/**
 * @author Derek Martin
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class S3StorageServiceImplTestSuite {
	
	private S3StorageServiceImpl testS3Service;
	
	// TODO: Mock the necessary classes to implement a comprehensive test suite for the S3StorageService object

	/**
	 * A simulated AWSCredential; not actually necessary as the class under test will have this as a field.
	 */
	@Mock
	private AWSCredentials credentials;
	
	/**
	 * A simulated AmazonS3; also not actually necessary as the class under test will have this as a field.
	 */
	@Mock
	private AmazonS3 s3EndPoint;
	
	// TODO: Write a suitable test assertion
	@Ignore
	@Test
	void test() {
		fail("Not yet implemented");
	}

	// TODO: Write a suitable test assertion
	@Ignore
	@Test
	public void shouldDoSomething() {
		
	}
	
	// TODO: Write a suitable test assertion
	@Ignore
	@Test
	public void shouldAlsoDoSomething() {
		
	}
	
	// TODO: Add additional test suite coverage as necessary

}
