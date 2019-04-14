package com.revature.testing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revature.controllers.ProjectController;
import com.revature.models.Project;
import com.revature.repositories.ProjectRepository;
import com.revature.security.CustomAuthenticationFilter;
import com.revature.services.FileServiceImpl;
import com.revature.services.ProjectService;
import com.revature.services.StorageService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProjectController.class }, secure = false)
@WithMockUser(roles="ADMIN")
public class IntegrationTestSuite {
	
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private Project mockProject;
	
	@MockBean
	private ProjectRepository repository;
	
	
	@MockBean
	private ProjectService mockProjectService;
	
	
	
	@Before
	public void setup() {		
		
	}
	/**
	 * This method is going to test if our context loads and is not null.
	 * 
	 * @throws Exception: If the context fails to load or is null, an exception will
	 *                    be thrown.
	 * 
	*/
	@Test
	public void testContextLoads() throws Exception {
		assertThat(this.mockMvc).isNotNull();
	}
	
	@Test
	public void testDelete() throws Exception {
		when(mockProjectService.findById("0")).thenReturn(mockProject);
		this.mockMvc.perform(delete("/id/0")).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteWhenGivenNull() throws Exception {
		this.mockMvc.perform(delete("/id/0")).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void testDeleteWhenReturnedNull() throws Exception {
		when(mockProjectService.findById("0")).thenReturn(null);
		this.mockMvc.perform(delete("/id/0")).andExpect(status().isNotFound());
	}
}
