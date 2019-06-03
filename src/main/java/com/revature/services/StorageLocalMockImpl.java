package com.revature.services;

import java.io.File;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("local")
public class StorageLocalMockImpl implements StorageService {

	Logger logger = Logger.getLogger(StorageLocalMockImpl.class);
	
	@Override
	public void init() {
		logger.warn("Running with mock S3 implementation. This should only be used for a local development environment.");
	}

	@Override
	public String store(MultipartFile multipartFile) {
		return "localhost:8080/unpersisted";
	}

	@Override
	public String store(File file) {
		return "localhost:8080/unpersisted";
	}

}
