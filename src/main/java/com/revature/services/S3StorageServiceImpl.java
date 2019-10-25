package com.revature.services;

import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
@Profile("!local")
public class S3StorageServiceImpl implements StorageService {

  @Value("${aws.config.aws-access-key-id}")
  private String awsAccessKeyId;

  @Value("${aws.config.aws-secret-access-key}")
  private String awsSecretAccessKey;

  @Value("${aws.config.bucket-name}")
  private String bucketName;

  @Value("${aws.config.bucket-region}")
  private String bucketRegion;

  @Value("${aws.config.s3-endpoint}")
  private String s3EndPoint;

  private AWSCredentials credentials;
  private AmazonS3 s3Client;

  /**
   * init draws on environment variables setting up an s3Client used to store objects
   * Added @Transactional
   */
  @Transactional
  @PostConstruct
  public void init() {
    System.out.println(awsAccessKeyId);
    System.out.println(awsSecretAccessKey);
    credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
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
  @Transactional
  public String store(MultipartFile multipartFile) {

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
  @Transactional
  public String store(File file) {
    s3Client.putObject(bucketName, file.getName(), file);
    return s3EndPoint + '/' + bucketName + '/' + file.getName();
  }
}
