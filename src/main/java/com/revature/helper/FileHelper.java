package com.revature.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

// https://fastfoodcoding.com/tutorials/1504810616200/how-to-convert-multipartfile-to-java-io-file-in-spring
public class FileHelper {
	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
}
