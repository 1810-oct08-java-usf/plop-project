package com.revature.services;

import org.springframework.http.ResponseEntity;

public interface FileService {
	ResponseEntity<byte[]> download(String fileURI);
}