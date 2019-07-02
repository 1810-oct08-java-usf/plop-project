package com.revature.services;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.revature.helpers.FileHelper;

/**
 * FileServiceImpl implements FileService
 */
@Service
public class FileServiceImpl implements FileService {
	
	/**
	 * FileServiceImpl.download simply implements FileService.download
	 * Added @Transactional
	 * 
	 * @param fileURI URI to the file to be downloaded
	 * @return the downloaded File
	 * @throws IOException if there is an issue downloading the specified file
	 * @author Stuart Pratuch (190422-JAVA-SPARK-USF)
	 */ 
	@Transactional
	public File download(String fileURI) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());    
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		ResponseEntity<byte[]> response = restTemplate.exchange(fileURI, HttpMethod.GET, entity, byte[].class, "1");
		
		String fileName = response.getHeaders().getContentDisposition().getFilename();

		return FileHelper.convert(response.getBody(), fileName);
	}

}
