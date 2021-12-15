package com.naveenc.catalogservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.naveenc.catalogservice.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer>{

	Object findByName(String string);

}
