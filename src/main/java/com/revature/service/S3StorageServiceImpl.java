package com.revature.service;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.revature.helper.FileHelper;

@Service
public class S3StorageServiceImpl implements StorageService {
	@Value("${aws.config.aws-access-key-id}")
	private String awsAccessKeyId;

	@Value("${aws.config.aws-secret-access-key}")
	private String awsSecretAccessKey;

	@Value("${aws.config.img-bucket-name}")
	private String bucketName;

	@Value("${aws.config.img-bucket-region}")
	private String bucketRegion;
	
	@Value("${aws.config.s3-endpoint}")
	private String s3EndPoint;

	AWSCredentials credentials;
	AmazonS3 s3Client;

	@PostConstruct
	public void init() {
		credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
		s3Client = AmazonS3ClientBuilder
			.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(bucketRegion)
			.build();
	}

	public String store(MultipartFile multipart) {
		try {
			s3Client.putObject(bucketName, multipart.getOriginalFilename(), FileHelper.convert(multipart));
			return  s3EndPoint + '/' + bucketName + '/' + multipart.getOriginalFilename();
		} catch (IOException e) {
			e.printStackTrace();
			 return "";
			
		}
	
	}
}