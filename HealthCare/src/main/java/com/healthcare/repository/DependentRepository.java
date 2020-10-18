package com.healthcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.model.Dependent;


@Repository
public interface DependentRepository extends JpaRepository<Dependent, Long> {
	
	List<Dependent> findAllByEnrolleeId(Long EnrolleeId);
	Optional<Dependent> findByEnrolleeIdAndId(Long EnrolleeId, Long DependentId);
	Optional<Dependent> findByid(Long DependentId);
	
	Long deleteByEnrolleeIdAndId(Long EnrolleeId, Long DependentId);
	Long deleteByid(Long Id);
	
	boolean existsByEnrolleeIdAndId(Long EnrolleeId, Long DependentId);
	boolean existsByid(Long Id);
}
