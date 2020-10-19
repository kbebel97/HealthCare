package com.healthcare.controller;


import java.text.ParseException;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.healthcare.exception.FieldBlankException;
import com.healthcare.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.model.Enrollee;
import com.healthcare.repository.EnrolleeRepository;
import com.healthcare.service.EnrolleeService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api")
@RestController
public class EnrolleeController {
	
	@Autowired
	EnrolleeService service;
	
	@Autowired
	EnrolleeRepository enrolleeRepo;
	
	@ApiOperation( value = "Find All Enrollees",
			   notes = "List of Enrollees will be displayed",
			   response = Enrollee.class
		)
	@GetMapping("/enrollees")
	public List<Enrollee> getAllEnrollees() {
		
		return service.findAll();
	}
	
	@ApiOperation( value = "Find an Enrollee by their id",
				   notes = "Provide an id to look up an Enrollee in the database. If id not found, will return 404.",
				   response = Enrollee.class
			)
	@GetMapping("/enrollee/{id}")
	public Enrollee getEnrolleeById(@PathVariable("id") long id) throws ResourceNotFoundException {
		
		Optional<Enrollee> enrollee = service.findById(id);
		
		if(!enrollee.isPresent()) {
			throw new ResourceNotFoundException("Enrollee with id = " + id + " not found");
		}
		
		return enrollee.get();
	}
	
	@ApiOperation( value = "Create Enrollee",
			   notes = "Create Enrollee or return 400 if bad request",
			   response = Enrollee.class
		)
	@PostMapping("/create/enrollee")
	public ResponseEntity<Enrollee> createEnrollee(@RequestBody Enrollee enrollee) throws FieldBlankException{
		if(enrollee.getFirstName()=="") {
			throw new FieldBlankException("First Name was left Blank");
		}
		if(enrollee.getLastName()=="") {
			throw new FieldBlankException("Last Name was left Blank");
		}
		if(enrollee.getBirthday()==null) {
			throw new FieldBlankException("Birthday was left Blank");
		}
		Enrollee created = service.save(enrollee);
		
		return new ResponseEntity<>(created, HttpStatus.CREATED);		
	}
	
	@ApiOperation( value = "Update Enrollee",
			   notes = "Update Enrollee if found, otherwise return 404 if not found or 400 if bad request",
			   response = Enrollee.class
		)
	@PutMapping("/update/enrollee")
	public ResponseEntity<Enrollee> updateEnrollee(@RequestBody Enrollee enrollee) throws ResourceNotFoundException {
		Enrollee updateEnrollee = service.findById(enrollee.getId()).get();
		if(updateEnrollee!=null){
			updateEnrollee = enrollee;
			enrolleeRepo.save(updateEnrollee);
			return new ResponseEntity<>(updateEnrollee, HttpStatus.CREATED);
		}
		else throw new ResourceNotFoundException("Enrollee with id = " + enrollee.getId() + " was not found.");
	}
	
	@ApiOperation( value = "Patch Enrollee",
			   notes = "Patch Enrollee if found, otherwise return 404 if not found or 400 if bad request",
			   response = Enrollee.class
		)
	@PatchMapping("/patch/enrollee")
	public ResponseEntity<Enrollee> editEnrolleeInfo(@RequestBody Map<String, String> map) throws ResourceNotFoundException, ParseException, NumberFormatException {
		Long id  = Long.valueOf(map.get("id"));
		Optional<Enrollee> enrollee = service.findById(id);
		if(enrollee.isEmpty())
			throw new ResourceNotFoundException("Enrollee with id = " + id + " was not found.");
		return new ResponseEntity<>(service.patchEnrollee(map, enrollee.get()), HttpStatus.CREATED);
		}
	 
	@ApiOperation( value = "Delete Enrollee by Id",
			   notes = "Delete Enrollee by Id, otherwise return 404",
			   response = Enrollee.class
		)
	@Transactional
	@DeleteMapping("/delete/enrollee/{id}")
	public ResponseEntity<Enrollee> deleteEnrollee(@PathVariable long id) throws ResourceNotFoundException {
		
		if(!service.existsById(id)) {
			throw new ResourceNotFoundException("Enrollee with id = " + id + " not found, cannot be deleted");
		}
		
		Enrollee deleted = service.findById(id).get();
		
		service.deleteById(id);
		
		return new ResponseEntity<>(deleted, HttpStatus.OK);
	}
}

