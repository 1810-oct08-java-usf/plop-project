package com.revature.services;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.revature.exceptions.FileSizeTooLargeException;

@RunWith(MockitoJUnitRunner.class)
public class StorageServiceTestSuite {

	// Do not mock the class you intend to test
	private S3StorageServiceImpl classUnderTest = new S3StorageServiceImpl();

//	@Mock
//	private MultipartFile mockMultipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
	@Mock
	private AmazonS3 s3Client;

	@Test
	public void testStoreMethod(){
		MultipartFile mockMultipartFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
		
		//		String zipLink = "https://github.com/supertuxkart/stk-code";
		//	boolean thrown = false;


		
		when(classUnderTest.store(mockMultipartFile)).thenReturn("Test");
		
//		String link = classUnderTest.store(mockMultipartFile);
//		System.out.println("link: ");
//		System.out.println(link);


	}
}