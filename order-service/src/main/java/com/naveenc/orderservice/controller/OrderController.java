package com.naveenc.orderservice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.naveenc.orderservice.Repository.IdRepository;
import com.naveenc.orderservice.Repository.OrderRepository;
import com.naveenc.orderservice.model.IdGenerator;
import com.naveenc.orderservice.model.Item;
import com.naveenc.orderservice.model.Order;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	IdRepository idRepo;
	
	@PostMapping("/post")
	public void addOrder(@RequestBody Order order) {
		IdGenerator idGen= idRepo.findById("orderId").get();
		order.setId(idGen.getSeq());
		idGen.setSeq(idGen.getSeq()+1);
		idRepo.save(idGen);
		order.setDate(LocalDate.now());
		orderRepo.save(order);
		System.out.println(order.getCustomerId());
	}
	
	@GetMapping("/get")
	public List<Order> getAllOrders(){
		List<Order> orders= new ArrayList<>();
		orderRepo.findAll().forEach(orders::add);
		return orders;
	}
	
	@GetMapping("/all/{userId}")
	public List<Order> getAllOrders(@PathVariable("userId") int userId) {
		List<Order> orders = new ArrayList<>();
		orderRepo.findAll().stream().forEach(order ->{
			if(order.getCustomerId()==userId)
				orders.add(order);
		});
		Collections.reverse(orders);
		return orders;
	}
	
	@GetMapping("/{orderId}")
	public Order getOrder(@PathVariable("orderId") int orderId) {
		return orderRepo.findById(orderId).get();
	}
	
	@GetMapping("/items/{orderId}")
	public Collection<Item> getItems(@PathVariable("orderId") int orderId) {
		return getOrder(orderId).getItems().values();
	}
	
	@GetMapping("/recent/{userId}")
	public Order getRecentOrder(@PathVariable("userId") int userId) {
		
		List<Order> orders= getAllOrders(userId);
		return orders.get(0);
	}
	
	@GetMapping("/recent/items/{userId}")
	public Collection<Item> getRecentItems(@PathVariable("userId") int userId){
		return getRecentOrder(userId).getItems().values();
	}
	
}
