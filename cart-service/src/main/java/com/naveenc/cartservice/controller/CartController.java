package com.naveenc.cartservice.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.naveenc.cartservice.model.Cart;
import com.naveenc.cartservice.model.IdGenerator;
import com.naveenc.cartservice.model.Item;
import com.naveenc.cartservice.model.Product;
import com.naveenc.cartservice.repository.CartRepository;
import com.naveenc.cartservice.repository.IdRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartRepository cartRepo;
	
	@Autowired
	IdRepository idRepo;
	
	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/{userId}")
	public Cart getCart(@PathVariable("userId") int userId) {
		List<Cart> carts = new ArrayList<>();
		cartRepo.findAll().stream().forEach(cart -> {
			if(cart.getUserId()==userId)
				carts.add(cart);
		});
		System.out.println(carts);
		return carts.get(0);
	}
	
	@GetMapping("/items/{userId}")
	public Collection<Item> getItems(@PathVariable("userId") int userId){
		return getCart(userId).getItems().values();
	}
	
	/*@PostMapping("/create/{userId}")
	public void createCart(@PathVariable("userId") int userId) {
		IdGenerator idGen= idRepo.findById("cartId").get();
		int id= idGen.getSeq();
		idGen.setSeq(id+1);
		idRepo.save(idGen);
		cartRepo.save(new Cart(id, userId, new HashMap<Integer,Item>(), 0));
	}*/
	
	@PostMapping("/create/{userId}")
	public void addOrder(@RequestBody Cart cart) {
	IdGenerator idGen= idRepo.findById("cartId").get();
	cart.setId(idGen.getSeq());
	idGen.setSeq(idGen.getSeq()+1);
	idRepo.save(idGen);
	cartRepo.save(cart);
	System.out.println(cart.getUserId());
	}
	
	@RequestMapping(value="/add/{userId}/{prodId}", method=RequestMethod.PUT)
	@CircuitBreaker(name = "addToCart", fallbackMethod="cartFallBack")
	public Cart addItem(@PathVariable("prodId") int prodId, @PathVariable("userId") int userId) {
		Product product= restTemplate.getForObject("http://catalog-service/catalog/"+prodId, Product.class);
		System.out.println(product);
		Cart cart= getCart(userId);
		Map<Integer,Item> items= cart.getItems();
		
		Item i= items.get(prodId);
		if(items.containsKey(prodId)) {
			i.setQuantity(i.getQuantity()+1);
			i.setPrice(product.getPrice()*i.getQuantity());
			items.put(prodId, i);
		}
		else {
			items.put(prodId, new Item(prodId, product.getImage(), product.getName(), product.getPrice(), 1));
		}
		cart.setItems(items);
		cart.setTotal(0);
		items.values().forEach(item -> cart.setTotal(cart.getTotal()+item.getPrice()));
		cartRepo.save(cart);
		return cart;
	}
	
	@PutMapping("/remove/{userId}/{prodId}")
	@CircuitBreaker(name = "removeFromCart", fallbackMethod="cartFallBack")
	public Cart removeItem(@PathVariable("prodId") int prodId, @PathVariable("userId") int userId) {
		Product product= restTemplate.getForObject("http://catalog-service/catalog/"+prodId, Product.class);
		System.out.println(product);
		Cart cart= getCart(userId);
		Map<Integer, Item> items= cart.getItems();
		Item i=items.get(prodId);
		if(i.getQuantity()>1) {
			i.setQuantity(i.getQuantity()-1);
			i.setPrice(product.getPrice()*i.getQuantity());
			items.put(prodId, i);
		}
		else {
			items.remove(prodId);
		}
		cart.setItems(items);
		cart.setTotal(0);
		items.values().forEach(item -> cart.setTotal(cart.getTotal()+item.getPrice()));
		cartRepo.save(cart);
		return cart;
	}
	
	@GetMapping("/cartItems/{userId}")
	public int noOfCartItems(@PathVariable("userId") int userId) {
		Cart cart= getCart(userId);
		int total=0;
		for(Item item: cart.getItems().values()) {
			total+=item.getQuantity();
		}
		return total;
	}
	
	
	@GetMapping("/all")
	public List<Cart> getAllCarts(){
		List<Cart> carts= new ArrayList<>();
		cartRepo.findAll().stream().forEach(carts::add);
		return carts;
	}
	
	@PutMapping("/clear/{userId}")
	public void deleteCart(@PathVariable("userId") int userId) {
		Cart cart= getCart(userId);
		cart.setItems(new HashMap<Integer, Item>());
		cart.setTotal(0);
		cartRepo.save(cart);
	}
	
	public Cart cartFallBack(int prodId, int userId, Exception e) {
		return getCart(userId);
	}
}
