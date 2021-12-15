package com.naveenc.orderservice.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.naveenc.orderservice.model.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, Integer> {

}
