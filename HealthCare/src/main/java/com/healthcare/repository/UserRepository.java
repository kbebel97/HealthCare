package com.healthcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByuserName(String userName);
	
}
