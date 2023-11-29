package com.ssf.adolescentcare.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ssf.adolescentcare.entities.AdolescentCare;

public interface AdolescentCareRepository extends MongoRepository<AdolescentCare, String> {

}
