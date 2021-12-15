package com.naveenc.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naveenc.catalogservice.model.IdGenerator;
import com.naveenc.catalogservice.model.Product;
import com.naveenc.catalogservice.repository.IdRepository;
import com.naveenc.catalogservice.repository.ProductRepository;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
	
	@Autowired
	ProductRepository prodRepo;
	
	@Autowired
	IdRepository idRepo;
	
	@PostMapping("/into")
	public void addProduct(@RequestBody Product product) {
		IdGenerator idGen= idRepo.findById("prodId").get();
		product.setId(idGen.getSeq());
		idGen.setSeq(idGen.getSeq()+1);
		idRepo.save(idGen);
		prodRepo.save(product);
	}
	
	@GetMapping("/net")
	public List<Product> getAllProducts(){
		List<Product> list= new ArrayList<>();
		prodRepo.findAll().stream().forEach(list::add);
		return list;
	}
	
	/*@GetMapping("/{id}")
	public Product getProduct(@PathVariable("id") int id) {
		return prodRepo.findById(id).get();*/
		
		@GetMapping("/{id}")
		public Product getProductById(@PathVariable("id") int id) {
			return prodRepo.findById(id).get();
	}
	
	@PutMapping("")
	public Product updateProduct(@RequestBody Product product) {
		prodRepo.save(product);
		return product;
		
	/*	@PutMapping("")
		public Product updateProduct(@RequestBody Product product) {
			prodRepo.save(product);
			return product;*/
	}
	
	@DeleteMapping("/{id}")
	public Product deleteProduct(@PathVariable("id") int id) {
		prodRepo.deleteById(id);
		return null;
	}
/*	@DeleteMapping("/{id}")
	public Product deleteProduct(@PathVariable("id") int id) {
		prodRepo.deleteById(id);
		return null;
	}*/

	

	public Object deleteById(int i) {
		// TODO Auto-generated method stub
		return "deleted";
	}

	
}
