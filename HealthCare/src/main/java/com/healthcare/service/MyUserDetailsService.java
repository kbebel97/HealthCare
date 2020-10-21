//package com.healthcare.service;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import com.healthcare.model.User;
//import com.healthcare.model.MyUserDetails;
//import com.healthcare.repository.UserRepository;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//	@Autowired
//	UserRepository userRepository;
//	
//	@Override
//	public MyUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
//		Optional< User  > user = userRepository.findByuserName(userName);
//		user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
//		user.map(MyUserDetails::new).get();
//		return user.map(MyUserDetails::new).get();
//	}
//}
