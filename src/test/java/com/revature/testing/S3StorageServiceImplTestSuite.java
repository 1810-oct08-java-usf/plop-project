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

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class S3StorageServiceImplTestSuite {
	
	// Class under test
	private S3StorageServiceImpl testS3Service;
	
	@Mock
	private AWSCredentials credentials;
	
	@Mock
	private AmazonS3 s3EndPoint;
	
	@Ignore
	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	// store(MultipartFile, String)
	@Ignore
	@Test
	public void shouldDoSomething() {
		
	}
	
	// store(byte[], String, String)
	@Ignore
	@Test
	public void shouldAlsoDoSomething() {
		
	}

}
