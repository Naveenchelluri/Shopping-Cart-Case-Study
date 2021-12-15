package com.naveenc.catalogservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.naveenc.catalogservice.controller.CatalogController;
import com.naveenc.catalogservice.model.Product;
import com.naveenc.catalogservice.repository.ProductRepository;

@SpringBootTest
class CatalogServiceApplicationTests {
	
	@MockBean
	private ProductRepository prodRepo;

	@Autowired
	private CatalogController contr;
	
	@Test
	void getAllProductsTest() {
		when(prodRepo.findAll()).thenReturn(Stream
				.of(new Product(2, "Note 8", "Electronics", 65465, "data.jpg", "Samsung galaxy note 8"),
					new Product(64, "Levi", "Clothing", 3599, "levi.jpg", "Printed Tshirt")).collect(Collectors.toList()));
		assertEquals(2, contr.getAllProducts().size());
	}
	
	@Test 
	@Order(2)
	public void getProductByIdTest(){
		Product product = new Product (2, "realme 8", "mobile", 15465, "data.jpg", "dare to leap");
		Optional<Product> op = Optional.of(product);
		when(prodRepo.findById(2)).thenReturn(op);
		assertEquals(2,op.get().getId());

}
	
	@Test 
	@Order(3)
	public void TestFindByName(){
		List<Product> product = new ArrayList<Product>();
		product.add(new Product (3, "AC", "Electronics", 65000, "data.jpg", "Air Conditioner"));
		when(prodRepo.findByName("Note 8")).thenReturn(product);
		assertEquals(1, ((List<Product>) prodRepo.findByName("Note 8")).size());
}
	
	@Test 
	@Order(4)
	public void addProduct(){

		Product product = new Product (4, "airdopes", "earphones", 600, "data.jpg", "Earphones");
		when(( prodRepo).insert(product)).thenReturn(product);
        assertEquals(product, ( prodRepo).insert(product));
}
	
	@Test 
	@Order(5)
	public void updateProduct(){
		Product product = new Product (5, "laptop", "Electronics", 55465, "data.jpg", "storage 1 Tb");
		when(contr.updateProduct( product)).thenReturn(product);
		assertEquals(product,contr.updateProduct(product));
}
	/*@Test 
	@Order(6)
	public void deleteProduct()
	{
		Product product = new Product (2, "Note 8", "Electronics", 65465, "data.jpg", "Samsung galaxy note 8");
		when(( contr).deleteById(2)).thenReturn("deleted");
        assertEquals("deleted", ( contr).deleteById(2));*/

}

