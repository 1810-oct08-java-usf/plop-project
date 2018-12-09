package com.revature.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

// https://fastfoodcoding.com/tutorials/1504810616200/how-to-convert-multipartfile-to-java-io-file-in-spring
public class FileHelper {
	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File("temp");
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static File convert(byte[] byteArray, String fileName) throws IOException {
		File convFile = new File(fileName);
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(byteArray);
		fos.close();
		return convFile;
	}
}
