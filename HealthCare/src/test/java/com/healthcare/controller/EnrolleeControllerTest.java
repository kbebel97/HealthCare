package com.healthcare.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.healthcare.exception.FieldBlankException;
import com.healthcare.model.Dependent;
import com.healthcare.model.Enrollee;
import com.healthcare.repository.EnrolleeRepository;
import com.healthcare.service.EnrolleeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EnrolleeController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EnrolleeControllerTest {

	private final String STARTING_URI = "http://localhost:8080/api/";
	
	@MockBean
	private EnrolleeRepository repo;
	
	@MockBean 
	private EnrolleeService service;
	
	EnrolleeController controller;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void testGetAllEnrollees() throws Exception, FieldBlankException{
		String uri = STARTING_URI + "enrollees";
		
		List<Dependent> list = null;

		List<Enrollee> allEnrollees = Arrays.asList(
				new Enrollee(1L, "Bebel", "Kacper", new Date(), 3472136196L, true, list),
				new Enrollee(2L, "Bebel", "Anna", new Date(), 9178629690L, true, list)		
		);
		
		when(repo.findAll()).thenReturn(allEnrollees);
		
		mockMvc.perform (MockMvcRequestBuilders.get(uri))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.model().attribute("phone", "iPhone"))
//		.andExpect(jsonPath("$.length()").value(allEnrollees.size()))
//		.andExpect(jsonPath("$[0].id").value(allEnrollees.get(0).getId()))
//		.andExpect(jsonPath("$[0].firstName").value(allEnrollees.get(0).getFirstName()));
		
		verify(repo, times(2)).findAll();
				
	}
	
	

}
