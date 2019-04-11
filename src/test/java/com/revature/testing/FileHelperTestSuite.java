package com.revature.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import com.revature.helpers.FileHelper;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.FileService;
import com.revature.services.ProjectService;
import com.revature.services.StorageService;

/**
 * Test suite for FileHelper
 * @author Alonzo Muncy (190107-Java-Spark-USF)
 */

@RunWith(MockitoJUnitRunner.class)
public class FileHelperTestSuite {
	
	@Spy
	private File spyFile = new File("temp");
	
	@Mock
	MultipartFile mockMultipartFile;
	
	@InjectMocks
	private FileHelper testFileHelper;
	
	@Test
	public void testConvertMultipartFile() {
		
	}
}
