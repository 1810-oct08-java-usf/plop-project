package com.revature.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/** The FileHelper class simply define static convenience methods for working with File objects */
// https://fastfoodcoding.com/tutorials/1504810616200/how-to-convert-multipartfile-to-java-io-file-in-spring
public class FileHelper {

  /**
   * overloaded version of FileHelper.convert that accepts a MultipartFile and returns a
   * corresponding File
   *
   * @param multipartFile A MultipartFile to convert to a file
   * @return a File corresponding to the argued byte[]
   * @throws IOException in the event of failures writing to or reading from the fs
   */
  public static File convert(MultipartFile multipartFile) throws IOException {
    File convFile = new File(multipartFile.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(multipartFile.getBytes());
    fos.close();
    return convFile;
  }

  /**
   * overloaded version of FileHelper.convert that accepts a byteArray and returns a corresponding
   * File
   *
   * @param byteArray a byte[] to convert to a file
   * @param fileName the fileName for the resulting file
   * @return a File corresponding to the argued byte[]
   * @throws IOException in the event of failures writing to or reading from the fs
   */
  public static File convert(byte[] byteArray, String fileName) throws IOException {
    // TODO this appears to write the file to the server's filesystem--should be avoided if possible
    // TODO since we only want to make the conversion in application memory
    File convFile = new File(fileName);
    convFile.createNewFile();
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(byteArray);
    fos.close();
    return convFile;
  }
}
