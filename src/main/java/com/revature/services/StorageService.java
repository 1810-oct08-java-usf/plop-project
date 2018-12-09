package com.revature.services;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
	void init();
	String store(MultipartFile multipartFile);
	String store(File file);
}
