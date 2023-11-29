package com.ssf.labtest.repository;

import com.ssf.labtest.entities.LabTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILabTestRepository extends MongoRepository<LabTest, String> {
    @Query("{\"id\" : { \"$in\" : ?0}}")
    Page<LabTest> findAllByIdContaining(List<String> labIds, Pageable paging);

    @Query("{$or : [{parentTestId: ?0}, {id : ?0}]}")
    Page<LabTest> findAllByParentTestId(String testId, Pageable paging);

    @Query("{ $text :{ $search : ?0 }}")
    Page<LabTest> searchByName(String search, Pageable paging);
}
