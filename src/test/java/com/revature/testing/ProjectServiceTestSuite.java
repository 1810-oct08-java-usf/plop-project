package com.revature.testing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.services.FileService;
import com.revature.services.ProjectService;
import com.revature.services.StorageService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTestSuite {
	
	// Do not mock the class you intend to test
	private ProjectService classUnderTest;
	
	// Mock all classes necessary to functionality under test.
	@Mock
	private ProjectRepository testRepo;
	
	@Mock
	private StorageService testStorage;
	
	@Mock
	private FileService testFileService;
	
	@Mock
	private List<Project> dummyList;
	
	@Mock
	private Project dummyProject = new Project();
	
	// Initialize objects and mock method behaviors here, prior to testing.
	@Before
	public void preTestInit() {			
		classUnderTest = new ProjectService(testRepo, testStorage, testFileService);
		
		// Define the behavior of dummyList
		dummyList.add(dummyProject);
		//Mockito.when(dummyList.add()).thenCallRealMethod(dummyProject);
		Mockito.when(dummyList.get(0)).thenReturn(dummyProject);
		
		// Define the relevant behaviors of testRepo 
		Mockito.when(testRepo.findById("floop")).thenReturn(Optional.of(dummyProject));
		Mockito.when(testRepo.findByName("string")).thenReturn(dummyList);
		Mockito.when(testRepo.findByBatch("batchin")).thenReturn(dummyList);
		
		//Define the relevant behaviors of dummyProject
		Mockito.when(dummyProject.getId()).thenReturn("floop");
		Mockito.when(dummyProject.getName()).thenReturn("string");
		Mockito.when(dummyProject.getBatch()).thenReturn("batchin");
	}
	
	
	// findByBatch()
	@Test
	public void shouldReturnTrue() {
		assertThat(classUnderTest.findByBatch("batchin")).contains(dummyProject);
	}
	
	// findByFullName()
	// findByTechStack()
	// findByStatus()
	
	// addProject()
	
	// findById() returns Projects
	@Test
	public void shouldReturnProjectOnGoodIdSearch() {
		assertThat(classUnderTest.findById("floop")).isInstanceOf(Project.class);
	}
	
	@Test
	public void shouldReturnNullOnFailedIdSearch() {
		assertThat(classUnderTest.findById("test")).isNull();
	}
	
	// findByName()
	@Test
	public void shouldReturnArrayListByName() {
		assertThat(classUnderTest.findByName("string")).isInstanceOf(List.class);
	}
	
	// deleteById()
	@Test
	public void shouldReturnTrueOnValidDelete() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}
	
	@Test
	public void shouldReturnTrueOnAnyString() {
		assertThat(classUnderTest.deleteById("floop")).isEqualTo(Boolean.TRUE);
	}
	
	@Test
	public void shouldReturnFalseOnNullParameter() {
		assertThat(classUnderTest.deleteById(null)).isEqualTo(Boolean.FALSE);
	}
	
	// findAllProjects()
	@Test
	public void shouldReturnLinkedList() {
		assertThat(classUnderTest.findAllProjects()).isInstanceOf(LinkedList.class);
	}
}
