package com.ssf.product.repository;

import com.ssf.product.entities.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IInventoryRepository extends MongoRepository<Inventory, String> {
    @Query(value = "{'id' : ?0, 'productId' : ?1 }")
    Optional<Inventory> findByIdAndProductId(String id, String productId);
}
