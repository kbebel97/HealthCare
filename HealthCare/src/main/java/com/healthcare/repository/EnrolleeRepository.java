package com.healthcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.model.Enrollee;


@Repository
public interface EnrolleeRepository extends JpaRepository<Enrollee, Long> {
	List<Enrollee> findAll();
	Optional<Enrollee> findById(Long Id);
	Long deleteByid(Long Id);
	boolean existsById(Long Id);
}
