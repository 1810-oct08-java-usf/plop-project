package com.revature.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/** init draws on environment variables setting up an s3Client used to store objects */
public interface StorageService {

  /** init draws on environment variables setting up an s3Client used to store objects */
  void init();

  /**
   * store puts an object in the configured s3 bucket
   *
   * @param multipartFile the file representation of the object desired to store
   * @return the link to the new object
   */
  String store(MultipartFile multipartFile);

  /**
   * store puts an object in the configured s3 bucket
   *
   * @param file the file representation fo the object desired to store
   * @return the link to the new object
   */
  String store(File file);

  ByteArrayOutputStream downloadFile(String keyName);
}
