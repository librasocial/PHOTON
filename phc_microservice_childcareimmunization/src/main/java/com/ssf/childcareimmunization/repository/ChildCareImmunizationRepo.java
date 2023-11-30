package com.ssf.childcareimmunization.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssf.childcareimmunization.entities.Immunization;

public interface ChildCareImmunizationRepo extends MongoRepository<Immunization, String> {
	List<Immunization> findAllById(String immunizationId);

	List<Immunization> findAllByChildCitizenId(String childCitizenId);

	List<Immunization> findByChildCitizenIdOrderById(String childCitizenId);

}
