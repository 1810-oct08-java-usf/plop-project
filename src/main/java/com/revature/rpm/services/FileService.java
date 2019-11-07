package com.revature.rpm.services;

import java.io.File;
import java.io.IOException;

/** FileService provides an interface to download a file from an undetermined source */
public interface FileService {

  /**
   * FileService.download accepts a fileURI and produces the file downloaded from that URI
   *
   * @param fileURI the URI from which to download
   * @return the downloaded file
   * @throws IOException if there is an issue downloading the specified file
   */
  File download(String fileURI) throws IOException;
}
