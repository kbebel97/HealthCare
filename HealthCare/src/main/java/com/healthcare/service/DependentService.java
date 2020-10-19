package com.healthcare.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.healthcare.exception.FieldBlankException;
import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.Dependent;
import com.healthcare.model.Enrollee;
import com.healthcare.repository.DependentRepository;
import com.healthcare.repository.EnrolleeRepository;

@Service
public class DependentService {
	
	@Autowired
	DependentRepository dependentRepo;
	
	@Autowired
	EnrolleeRepository enrolleeRepo;
	
	public List<Dependent> findAll(){
		return dependentRepo.findAll();
	}
	public List<Dependent> findAllByEnrolleeId(Long EnrolleeId){
		return dependentRepo.findAllByEnrolleeId(EnrolleeId);
	}
	
	
	public Optional<Dependent> findByEnrolleeIdAndDependentId(Long EnrolleeId, Long DependentId){
		return dependentRepo.findByEnrolleeIdAndId(EnrolleeId, DependentId);
	}
	public Optional<Dependent> findByDependentId(Long DependentId){
		return dependentRepo.findByid(DependentId);
	}
	
	
	public Long deleteByDependentIdAndEnrolleeId(Long DependentId, Long EnrolleeId) {
		return dependentRepo.deleteByEnrolleeIdAndId(EnrolleeId, DependentId);
	}
	public Long deleteById(Long DependentId) {
		return dependentRepo.deleteByid(DependentId);
	}
	
	
	public boolean existsByDependentId(Long DependentId) {
		return dependentRepo.existsByid(DependentId);
	}
	public boolean existsByEnrolleeIdAndDependentId(Long DependentId, Long EnrolleeId) {
		return dependentRepo.existsByEnrolleeIdAndId(EnrolleeId, DependentId);
	}
	
	
	public Dependent patchDependent(Map<String, String> map, Dependent dependent) throws ParseException, NumberFormatException {
		String firstName = map.get("firstName");
		String lastName = map.get("lastName");
		String birthday = map.get("birthday");
		if(firstName!=null)
			dependent.setFirstName(firstName);
		if(lastName!=null)
			dependent.setLastName(lastName);
		if(birthday!=null) {
			SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");  
			Date date1 = formatter1.parse(birthday); 
			dependent.setBirthday(date1);
		}
		return dependentRepo.save(dependent);
		
	}
	

	public Enrollee saveDependents(List<Dependent> list, Long EnrolleeId) throws ResourceNotFoundException, FieldBlankException {
		Enrollee enrollee = enrolleeRepo.findById(EnrolleeId).get();
		if(enrollee!=null) {
			for(int i = 0; i < list.size(); i++ ) {
				if(list.get(i).getFirstName()=="")
					throw new FieldBlankException("First Name field was left blank for one or more dependent entries");
				if(list.get(i).getLastName()=="")
					throw new FieldBlankException("Last Name field was left blank for one or more dependent entries");
				if(list.get(i).getBirthday() == null)
					throw new FieldBlankException("Birthday field was left blank for one or more dependent entries");
				list.get(i).setId(-1L);
				list.get(i).setEnrollee(enrollee);
			}
			enrollee.setDependents(list);
			return enrolleeRepo.save(enrollee);

		}
		else throw new ResourceNotFoundException("Enrollee with id = " + EnrolleeId + " not found");
	}
	
		
	public Enrollee save(Dependent dependent, Long EnrolleeId) throws ResourceNotFoundException, FieldBlankException{
		Enrollee enrollee = enrolleeRepo.findById(EnrolleeId).get();
		List<Dependent> list = new ArrayList<Dependent>();
		if(enrollee!=null) {
			if(dependent.getFirstName()=="")
				throw new FieldBlankException("First Name field was left blank");
			if(dependent.getLastName()=="")
				throw new FieldBlankException("Last Name field was left blank");
			if(dependent.getBirthday() ==null)
				throw new FieldBlankException("Birthday field was left blank");
			dependent.setId(-1L);
			dependent.setEnrollee(enrollee);
			list.add(dependent);
			enrollee.setDependents(list);
			return enrolleeRepo.save(enrollee);
		}
		else throw new ResourceNotFoundException("Enrollee with id = " + EnrolleeId + " not found");

		
	}

}
