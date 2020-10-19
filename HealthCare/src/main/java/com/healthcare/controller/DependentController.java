package com.healthcare.controller;

import java.text.ParseException;
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

import com.healthcare.model.Dependent;
import com.healthcare.model.Enrollee;
import com.healthcare.repository.DependentRepository;
import com.healthcare.repository.EnrolleeRepository;
import com.healthcare.service.DependentService;
import com.healthcare.service.EnrolleeService;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api")
@RestController
public class DependentController {
	
	@Autowired
	DependentService service;
	
	@Autowired
	DependentRepository dependentRepo;
	
	@Autowired
	EnrolleeRepository enrolleeRepo;
	
	@GetMapping("/dependents")
	public List<Dependent> getAllDependents() {
		
		return service.findAll();
	}
	
	@ApiOperation( value = "Find a Dependent by their id",
				   notes = "Provide an id to look up a Dependent in the database. If id not found, will return 404.",
				   response = Dependent.class
			)
	@GetMapping("/dependentBydependentId/{dependentId}")
	public Dependent getDependentBydependentId(@PathVariable long dependentId) throws ResourceNotFoundException {
		
		Optional<Dependent> dependent = service.findByDependentId(dependentId);
		
		if(!dependent.isPresent()) {
			throw new ResourceNotFoundException("Dependent with id = " + dependentId + " not found");
		}
		
		return dependent.get();
	}
	@ApiOperation( value = "Find a Dependent by EnrolleeId",
			   notes = "Provide an EnrolleeId to look up a Dependent in the database. If id not found, will return 404.",
			   response = Dependent.class
		)
	@GetMapping("/dependentByenrolleeId/{enrolleeId}")
	public List<Dependent> getDependentBydependentIdAndenrolleeId(@PathVariable long enrolleeId) throws ResourceNotFoundException{
		
		List<Dependent> dependents = service.findAllByEnrolleeId(enrolleeId);
		
		if(dependents.isEmpty()) {
			throw new ResourceNotFoundException("Dependent list with EnrolleeId = " + enrolleeId + " not found");
		}
		
		return dependents;
	}
	@ApiOperation( value = "Find a Dependent by their Id and their EnrolleeId",
			   notes = "Provide an Id and EnrolleeId to look up a Dependent in the database. If id not found, will return 404.",
			   response = Dependent.class
		)
	@GetMapping("/dependent/{dependentId}/enrollee/{enrolleeId}")
	public Dependent getDependentByDependentIdAndEnrolleeId(@PathVariable Long dependentId, @PathVariable Long enrolleeId ) throws ResourceNotFoundException{
		
		Optional<Dependent> dependent = service.findByEnrolleeIdAndDependentId(enrolleeId, dependentId);
		
		if(!dependent.isPresent()) {
			throw new ResourceNotFoundException("Dependent with DependentId = " + dependentId + " and EnrolleeId = " + enrolleeId + " was not found.");
		}
		return dependent.get();	
	}
	
	@ApiOperation( value = "Create a dependent with an Active EnrolleeId",
			   notes = "Create a dependent with an Active EnrolleeId. If id not found, will return 400.",
			   response = Dependent.class
		)
	@PostMapping("/create/dependent/{enrolleeId}")
	public ResponseEntity<Enrollee> createDependent(@RequestBody Dependent dependent, @PathVariable Long enrolleeId) throws ResourceNotFoundException, FieldBlankException{		
		Enrollee created = service.save(dependent, enrolleeId);
		if(created==null)
		throw new ResourceNotFoundException("Enrollee with id = " + enrolleeId + " was not found");
		return new ResponseEntity<>(created, HttpStatus.CREATED);		

}
	@ApiOperation( value = "Create a dependents with an Active EnrolleeId",
			   notes = "Create dependents with an Active EnrolleeId. If id not found, will return 400.",
			   response = Dependent.class
		)
	@PostMapping("/create/dependents/{enrolleeId}")
	public ResponseEntity<Enrollee> createDependents(@RequestBody List<Dependent> dependents, @PathVariable Long enrolleeId) throws ResourceNotFoundException, FieldBlankException{
		Enrollee enrolleeList = service.saveDependents(dependents, enrolleeId);
		if(enrolleeList!=null)
			return new ResponseEntity<>(enrolleeList, HttpStatus.CREATED);		
		throw new ResourceNotFoundException("Enrollee with id = " + enrolleeId + " was not found");
		
	}
	
	@ApiOperation( value = "Update a dependent",
			   notes = "Update a dependent. If id not found, will return 400.",
			   response = Dependent.class
		)
	@PutMapping("/update/dependent")
	public Dependent update(@RequestBody Dependent dependent) throws ResourceNotFoundException {
		Dependent found = dependentRepo.findByid(dependent.getId()).get();
		if(found == null) {
			throw new ResourceNotFoundException("Dependent with id = " + dependent.getId() + " not found");
		}
		Enrollee enrollee = found.getEnrollee();

		found = dependent;
		found.setEnrollee(enrollee);
		return dependentRepo.save(found);	
	}
	@ApiOperation( value = "Patch a dependent",
			   notes = "Update a dependent with Id. If id not found, will return 400.",
			   response = Dependent.class
		)
	@PatchMapping("/patch/dependent")
	public Dependent patch(@RequestBody Map<String, String> map) throws ResourceNotFoundException, ParseException{
		Dependent found = getDependentBydependentId(Long.valueOf(map.get("id")));
		if(found == null) {
			throw new ResourceNotFoundException("Dependent with id = " + Long.valueOf(map.get("id")) + " not found");
		}
		return service.patchDependent(map, found);
	}
	
	@ApiOperation( value = "Delete a dependent with an id",
			   notes = "Delete a dependent with Id. If id not found, will return 400.",
			   response = Dependent.class
		)
	@Transactional
	@DeleteMapping("/delete/dependent/{dependentId}")
	public ResponseEntity<Dependent> deleteEnrollee(@PathVariable long dependentId) throws ResourceNotFoundException {
		
		if(!service.existsByDependentId(dependentId)) {
			throw new ResourceNotFoundException("Dependent with id = " + dependentId + " not found, cannot be deleted");
		}
		
		Dependent deleted = service.findByDependentId(dependentId).get();
		
		service.deleteById(dependentId);
		
		return new ResponseEntity<>(deleted, HttpStatus.OK);
	}
}
