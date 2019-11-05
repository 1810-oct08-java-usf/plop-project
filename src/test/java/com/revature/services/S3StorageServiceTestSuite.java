package com.revature.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

/** S3StorageServiceTestSuite */
@SpringBootTest
@RunWith(Theories.class)
public class S3StorageServiceTestSuite {
  private S3StorageServiceImpl sut = new S3StorageServiceImpl();

  // ${aws.config.aws-access-key-id}
  private String awsAccessKeyId = "testKey";

  // ${aws.config.aws-secret-access-key}
  private String awsSecretAccessKey = "testSecretKey";

  // ${aws.config.bucket-name}
  private String bucketName = "bucket";

  // ${aws.config.bucket-region}
  private String bucketRegion = "region";

  // ${aws.config.s3-endpoint}
  private String s3EndPoint = "endpoint";

  // A mock file
  private File mockFile = Mockito.mock(File.class);

  // A mock multipart file
  private MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);

  // Mock of AWSCredentials object
  private AWSCredentials credentials = Mockito.mock(BasicAWSCredentials.class);

  // Mock of Amazon Client
  private AmazonS3 s3Client = Mockito.mock(AmazonS3.class);

  @Before
  public void preTestInit() {}

  @Ignore(
      "Currently Unable to test due to the Static method FileHelper.convert and Environment Variables")
  @Test
  public void T_store_MultiPartFileValid() {
    when(mockMultipartFile.getOriginalFilename()).thenReturn("filename");
    when(s3Client.putObject(bucketName, "filename", mockFile)).thenReturn(null);
    String expected = s3EndPoint + '/' + bucketName + '/' + "filename";
    assertEquals("", expected, eq(sut.store(mockMultipartFile)));
  }

  @Ignore(
      "Currently Unable to test due to the Static method FileHelper.convert and Environment Variables")
  @Test
  public void T_store_FileValid() {
    when(mockFile.getName()).thenReturn("filename");
    when(s3Client.putObject(bucketName, "filename", mockFile)).thenReturn(null);
    String expected = s3EndPoint + '/' + bucketName + '/' + "filename";
    assertEquals("", expected, eq(sut.store(mockFile)));
  }
}
