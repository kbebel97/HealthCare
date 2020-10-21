package com.healthcare.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.Enrollee;
import com.healthcare.model.Dependent;

import com.healthcare.repository.EnrolleeRepository;
import com.healthcare.service.DependentService;
import com.healthcare.service.EnrolleeService;

//@OverrideAutoConfiguration(enabled=true)
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EnrolleeService.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EnrolleeServiceTest {
	
	
	private final String STARTING_URI = "http://localhost:8080/api";
	
	@MockBean
	private EnrolleeRepository repo;
	
	private EnrolleeService service;
	
	@Autowired
	private MockMvc mockMvc;
//	
//	@MockBean
//	private DependentService dservice;
	
	
	@Test
	void testfindAllEnrollees() throws Exception{
		String uri = STARTING_URI + "/enrollees";
		Enrollee Kacper =  new Enrollee(1L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		Enrollee Anna = new Enrollee(1L, "kacper", "Bebel", new Date(), 917L, true, null);


		List<Enrollee> allEnrollees = new ArrayList<Enrollee>();
		allEnrollees.add(Kacper);
		allEnrollees.add(Anna);
		when(service.findAll()).thenReturn(allEnrollees);
		
		mockMvc.perform(get(uri))
			.andDo(print())
			.andExpect(status().isOk());
//			.andExpect(jsonPath("$.length()").value(allEnrollees.size()))
//			.andExpect(jsonPath("$[0].id").value(allEnrollees.get(0).getId()))
//			.andExpect(jsonPath("$[0].firstName").value(Kacper.getFirstName()))
//			.andExpect(jsonPath("$[0].lastName").value(Kacper.getLastName()))
//			.andExpect(jsonPath("$[0].birthday").value(Kacper.getBirthday()))
//			.andExpect(jsonPath("$[0].phoneNumber").value(Kacper.getPhoneNumber()))
//			.andExpect(jsonPath("$[0].bool").value(Kacper.isActive()))
//			.andExpect(jsonPath("$[0].list").value(Kacper.getDependents()));
//			.andReturn();
			
			verify(repo, times(1)).findAll();
	}
	
	@Test
	void testGetEnrolleeById() throws Exception{
		String uri = STARTING_URI + "/enrollee/{id}";
		long id = 1;
		Enrollee enrollee = new Enrollee(7L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		when(repo.findById(id)).thenReturn(Optional.of(enrollee));
		mockMvc.perform(get(uri, id))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].firstName").value(enrollee.getFirstName()))
		.andExpect(jsonPath("$[0].lastName").value(enrollee.getLastName()))
		.andExpect(jsonPath("$[0].birthday").value(enrollee.getBirthday()))
		.andExpect(jsonPath("$[0].phoneNumber").value(enrollee.getPhoneNumber()))
		.andExpect(jsonPath("$[0].bool").value(enrollee.isActive()))
		.andExpect(jsonPath("$[0].list").value(enrollee.getDependents()));
		
		verify(repo, times(1)).findById(id);
		verifyNoMoreInteractions(repo);
		
	}
	@Test
	void testCarNotFoundById() throws Exception {
		
		String uri = STARTING_URI + "/car/{id}";
		long id = 1;
		
		when( repo.findById(id) ).thenReturn(Optional.empty());
		
		mockMvc.perform(get(uri, id) )
				.andExpect( status().isNotFound() );
		
		verify(repo, times(1)).findById(id);
		verifyNoMoreInteractions(repo);	
	}
	
	@Test
	void testCreateEnrollee() throws Exception {
		
		String uri = STARTING_URI + "/create/enrollee";
		
		Enrollee Kacper =  new Enrollee(1L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		
		when( repo.save( Mockito.any(Enrollee.class) ) ).thenReturn(Kacper);
		
		mockMvc.perform( post(uri)
						.contentType( MediaType.APPLICATION_JSON ) 
						.content( asJsonString(Kacper) ) )
				.andDo(print() )
				.andExpect( status().isCreated() )
				.andExpect( jsonPath("$.firstName").value(Kacper.getFirstName()) );
		
		verify(repo, times(1)).save(Mockito.any(Enrollee.class));
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	void testUpdateEnrollee() throws Exception {
		
		String uri = STARTING_URI + "/update/enrollee";
		long id = 1;
		
		Enrollee enrolleeUpdated = new Enrollee(1L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		
		when( repo.existsById(id) ).thenReturn(true);
		when( repo.save(Mockito.any(Enrollee.class)) ).thenReturn(enrolleeUpdated);
		
		mockMvc.perform( put(uri)
						 .contentType(MediaType.APPLICATION_JSON)
						 .content( asJsonString(enrolleeUpdated) ))
				.andExpect(status().isOk())
				.andExpect( jsonPath("$.id").value(enrolleeUpdated.getId()) );
		
		verify(repo, times(1)).existsById(id);
		verify(repo, times(1)).save(Mockito.any(Enrollee.class));
		verifyNoMoreInteractions(repo);
	}
	
	@Test
	void testUpdateResourceNotFound() throws Exception {
		
		String uri = STARTING_URI + "/update/enrollee";
		long id = 1;
		
		Enrollee enrolleeUpdated = new Enrollee(1L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		
		when( repo.existsById(id) ).thenReturn(false);
		
		mockMvc.perform( put(uri)
						 .contentType(MediaType.APPLICATION_JSON)
						 .content( asJsonString(enrolleeUpdated) ))
				.andExpect(status().isNotFound());
		
		verify(repo, times(1)).existsById(id);
		verifyNoMoreInteractions(repo);
		
	}
	
	@Test
	void testDeleteEnrollee() throws Exception {
		
		long id = 1;
		String uri = STARTING_URI + "/delete/enrollee/{id}";
		
		Enrollee enrollee = new Enrollee(1L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		
		when( repo.existsById(id) ).thenReturn(true);
		when( repo.findById(id) ).thenReturn(Optional.of(enrollee));
		doNothing().when(repo).deleteById(id);
		
		mockMvc.perform( delete(uri, id) )
			.andDo(print())
			.andExpect( status().isOk() );
		
		verify(repo, times(1)).existsById(id);
		verify( repo, times(1) ).findById(id);
		verify(repo, times(1)).deleteById(id);
		verifyNoMoreInteractions(repo);
		
	}
	
	void mockThrow() {
		
		long id = 1;
		String uri = STARTING_URI + "/delete/enrollee/{id}";
		
		Enrollee enrollee = new Enrollee(1L, "kacper", "Bebel", new Date(), 3472136196L, true, null);
		
		// if need a method to throw an exception when calling it for 
		// testing, use thenThrow()
		when( repo.existsById(id) ).thenThrow(Exception.class);
		
		
	}
	
	
	public static String asJsonString(final Object obj) {
		
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch(Exception e) {
			throw new RuntimeException();
		}
		
	}
	
	


}
