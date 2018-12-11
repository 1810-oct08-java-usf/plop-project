package com.revature.services;

import java.io.File;
import java.io.IOException;

public interface FileService {
	
	File download(String fileURI) throws IOException;
	
}
