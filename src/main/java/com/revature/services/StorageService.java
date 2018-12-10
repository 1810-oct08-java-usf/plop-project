package com.revature.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	void init();
	String store(MultipartFile file, String bucketName);
	String store(byte[] byteArray, String bucketName, String desiredKey);
}
