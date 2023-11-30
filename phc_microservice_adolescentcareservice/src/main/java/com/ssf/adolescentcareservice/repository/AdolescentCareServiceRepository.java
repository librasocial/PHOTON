package com.ssf.adolescentcareservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssf.adolescentcareservice.entities.AdolescentCareService;

public interface AdolescentCareServiceRepository extends MongoRepository<AdolescentCareService, String> {

}
