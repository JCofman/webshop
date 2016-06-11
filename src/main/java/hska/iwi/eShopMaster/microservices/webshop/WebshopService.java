package hska.iwi.eShopMaster.microservices.webshop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hska.iwi.eShopMaster.microservices.products.domain.Product;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.User;

@Service
public class WebshopService {

	@Autowired
	protected RestTemplate restTemplate;

	protected String userServiceUrl;
	protected String categoryServiceUrl;
	protected String productServiceUrl;
	
	public WebshopService(String userServiceUrl,
						  String categoryServiceUrl,
						  String productServiceUrl) {
		
		this.userServiceUrl = userServiceUrl;
		this.categoryServiceUrl = categoryServiceUrl;
		this.productServiceUrl = productServiceUrl;
	}
	
	public User findByUsername(String username) {
		return restTemplate.getForObject(userServiceUrl + "/users/{username}",
				User.class, username);
	}
	
	public ResponseEntity<User> registerUser(User user) {
		return restTemplate.postForEntity(userServiceUrl + "/users", user, User.class);
	}
	
	public ResponseEntity<Category> addCategory(String categoryName) {
		return restTemplate.postForEntity(categoryServiceUrl + "/categories/create", categoryName, Category.class);
	}
	
	public ResponseEntity<Category[]> getCategories() {
		return restTemplate.getForEntity(categoryServiceUrl + "/categories", Category[].class);
	}
	
	public ResponseEntity<String> deleteCategory(Map<String, String> params) {
		ResponseEntity<Product[]> response = restTemplate.getForEntity(productServiceUrl + "/products/byCategory/{categoryid}",
								  Product[].class, Integer.parseInt(params.get("id")));
		if ((response.getStatusCode() == HttpStatus.OK) && (response.getBody().length == 0)) {
			restTemplate.delete(categoryServiceUrl + "/categories/{id}", params);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	public ResponseEntity<Category> getCategory(String categoryName) {
		return restTemplate.getForEntity(categoryServiceUrl + "/categories/{name}",
										 Category.class, categoryName);
	}
	
	public ResponseEntity<Product> createProduct(Product product) {
		return restTemplate.postForEntity(productServiceUrl + "/products",
										  product, Product.class);
	}
	
	public ResponseEntity<String> deleteProduct(int id) {
		restTemplate.delete(productServiceUrl + "/products/{id}", id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	public ResponseEntity<Product> getProduct(int id) {
		return restTemplate.getForEntity(productServiceUrl + "/products/{id}",
										 Product.class, id);
	}
}

//@Component
//class ServiceDiscoveryClient {
//	
//	@Autowired
//	private DiscoveryClient discoveryClient;
//	
//	public String serviceInstancesByApplicationName(String applicationName) {
//		List<ServiceInstance> list = this.discoveryClient.getInstances(applicationName);
//		for (ServiceInstance si: list) {
//			System.out.println(si.getUri().toString());
//			System.out.println(si.getHost() + " " + si.getPort());
//			System.out.println(si.getServiceId());
//			return si.getUri().toString();
//		}
//		return "null";
//	}
//}