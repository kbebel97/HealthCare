package com.healthcare.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.healthcare.model.Enrollee;
import com.healthcare.repository.EnrolleeRepository;



@Service
public class EnrolleeService{
	
	@Autowired
	EnrolleeRepository enrolleeRepo;
	
	public List<Enrollee> findAll(){
		return enrolleeRepo.findAll();
	}
	
	public Optional<Enrollee> findById(Long Id){
		return enrolleeRepo.findById(Id);
	}
	
	public Long deleteById(Long Id) {
		return enrolleeRepo.deleteByid(Id);
	}
	
	public Enrollee patchEnrollee(Map<String, String> map, Enrollee enrollee) throws ParseException, NumberFormatException{
		String firstName = map.get("firstName");
		String lastName = map.get("lastName");
		String birthday = map.get("birthday");
		String phoneNumber = map.get("phoneNumber");
		String isActive = map.get("active");
		if(firstName!=null)
			enrollee.setFirstName(firstName);
		if(lastName!=null)
			enrollee.setLastName(lastName);
		if(birthday!=null) {
			SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");  
			Date date1 = formatter1.parse(birthday); 
			enrollee.setBirthday(date1);
		}
		
		if(phoneNumber!=null) {
			Long number = Long.valueOf(phoneNumber);
			enrollee.setPhoneNumber(number);
		}
		if(isActive!=null) {
			boolean bool = Boolean.valueOf(isActive);
			enrollee.setActive(bool);
		}
		return enrolleeRepo.save(enrollee);	
	}
	
	public Enrollee save(Enrollee enrollee){
		enrollee.setId(-1L);
		return enrolleeRepo.save(enrollee);
	}
	
	public boolean existsById(Long Id) {
		return enrolleeRepo.existsById(Id);
	}
}