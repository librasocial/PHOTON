package com.ssf.product.repository;

import com.ssf.product.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IProductRepository extends MongoRepository<Product, String> {
    Page<Product> findAllByNameLikeIgnoreCase(String name, Pageable pageable);
}
