package com.revature.services;

import java.util.Arrays;

import com.revature.models.ProjectDTO;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FileServiceImpl implements FileService {
	
	// TODO this is not the best but in the project controller both the response body and a header are needed so
	public ResponseEntity<byte[]> download(String fileURI) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());    
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		return restTemplate.exchange(fileURI, HttpMethod.GET, entity, byte[].class, "1");
	}
}
