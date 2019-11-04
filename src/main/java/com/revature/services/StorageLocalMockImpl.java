package com.revature.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.revature.models.Project;
import com.revature.util.FileHelper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.revature.util.FileHelper;

@Service
@Profile("local")
@PropertySource("classpath:credentials.properties")
public class StorageLocalMockImpl implements StorageService {

  @Value("${ACCESS_KEY_ID}")
  private String awsAccessKeyId;

  @Value("${SECRET_ACCESS_KEY}")
  private String awsSecretAccessKey;

  @Value("${BUCKET_NAME}")
  private String bucketName;

  @Value("${BUCKET_REGION}")
  private String bucketRegion;

  @Value("${S3_ENDPOINT}")
  private String s3EndPoint;

  private AWSCredentials credentials;
  private AmazonS3 s3Client;
  private ProjectService projectService;
 

  Logger logger = Logger.getLogger(StorageLocalMockImpl.class);

  /**
   * init draws on environment variables setting up an s3Client used to store objects
   * Added @Transactional
   */
  @Override
  @Transactional
  @PostConstruct
  public void init() {
    credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
    System.out.println("awsAccessKeyId: " + awsAccessKeyId);
    System.out.println("awsSecretAccessKey: " + awsSecretAccessKey);
    s3Client =
        AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(bucketRegion)
            .build();
  }

  /**
   * store puts an object in the configured s3 bucket Added @Transactional
   *
   * @param multipartFile the file representation of the object desired to store
   * @return the link to the new object
   */
  @Override
  @Transactional
  public String store(MultipartFile multipartFile) {
    System.out.println("inside store(multipartFile) method");
    try {
      s3Client.putObject(
          bucketName, multipartFile.getOriginalFilename(), FileHelper.convert(multipartFile));
      return s3EndPoint + '/' + bucketName + '/' + multipartFile.getOriginalFilename();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * store puts an object in the configured s3 bucket
   *
   * @transactional added
   * @param file the file representation fo the object desired to store
   * @return the link to the new object
   */
  @Override
  @Transactional
  public String store(File file) {
    s3Client.putObject(bucketName, file.getName(), file);
    return s3EndPoint + '/' + bucketName + '/' + file.getName();
  }

  /**
   * Goes to the AWS S3 Bucket to fetch uploaded files @Transactional
   *
   * @param file name to be fetched
   * @return return the file from the S3 bucket
   */
  @Transactional
  @Override
  public ByteArrayOutputStream downloadFile(String keyName) {
    try {
      S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, keyName));

      InputStream is = s3object.getObjectContent();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int len;
      byte[] buffer = new byte[4096];
      while ((len = is.read(buffer, 0, buffer.length)) != -1) {
        baos.write(buffer, 0, len);
      }

      return baos;
    } catch (IOException ioe) {
      System.out.println("IOException: " + ioe.getMessage());
    } catch (AmazonServiceException ase) {
      System.out.println("Caught an AmazonServiceException from GET requests, rejected reasons:");
      System.out.println("Error Message:    " + ase.getMessage());
      System.out.println("HTTP Status Code: " + ase.getStatusCode());
      System.out.println("AWS Error Code:   " + ase.getErrorCode());
      System.out.println("Error Type:       " + ase.getErrorType());
      System.out.println("Request ID:       " + ase.getRequestId());
      throw ase;
    } catch (AmazonClientException ace) {
      System.out.println("Caught an AmazonClientException: ");
      System.out.println("Error Message: " + ace.getMessage());
      throw ace;
    }

    return null;
  }
  
  
}
