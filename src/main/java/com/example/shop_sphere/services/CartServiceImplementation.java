package com.example.shop_sphere.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shop_sphere.entities.CartItem;
import com.example.shop_sphere.entities.Product;
import com.example.shop_sphere.entities.ProductImage;
import com.example.shop_sphere.entities.User;
import com.example.shop_sphere.repositories.CartRepository;
import com.example.shop_sphere.repositories.ProductImageRepository;
import com.example.shop_sphere.repositories.ProductRepositoy;
import com.example.shop_sphere.repositories.UserRepository;

@Service
public class CartServiceImplementation implements CartService {
	
	private CartRepository cartRepository;
	
	private UserRepository userRepository;
	
	private ProductRepositoy productRepository;
	
	private ProductImageRepository productImageRepository;

	public CartServiceImplementation(CartRepository cartRepository, UserRepository userRepository,
			ProductRepositoy productRepository, ProductImageRepository productImageRepository) {
		super();
		this.cartRepository = cartRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
	}

	@Override
	public int getCartItemCount(int userId) {
		return cartRepository.countTotalItems(userId);
	}

	@Override
	public void addToCart(int userId, int productId, int quantity) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

		// Fetch cart item for this userId and productId
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);

		if (existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartRepository.save(cartItem);
		} else {
			CartItem newItem = new CartItem(user, product, quantity);
			cartRepository.save(newItem);
		}
	}

	@Override
	public Map<String, Object> getCartItems(int userId) {
		// Fetch the cart items for the user with product details
		List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetails(userId);

		// Create a response map to hold the cart details
		Map<String, Object> response = new HashMap<>();
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

		response.put("username", user.getUsername());
		response.put("role", user.getRole().toString());

		// List to hold the product details
		List<Map<String, Object>> products = new ArrayList<>();
		int overallTotalPrice = 0;

		for (CartItem cartItem : cartItems) {
			Map<String, Object> productDetails = new HashMap<>();

			// Get product details
			Product product = cartItem.getProduct();

			// Fetch product images from the ProductImageRepository
			List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = null;

			if (productImages != null && !productImages.isEmpty()) {
				// If there are images, get the first image's URL
				imageUrl = productImages.get(0).getImageUrl();
			} else {
				// Set a default image if no images are available
				imageUrl = "default-image-url"; // You can replace this with your default image URL
			}

			// Populate product details into the map
			productDetails.put("product_id", product.getProductId());
			productDetails.put("image_url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());

			// Add the product details to the products list
			products.add(productDetails);

			// Add to the overall total price
			overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
		}

		// Prepare the final cart response
		Map<String, Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);

		// Add the cart details to the response
		response.put("cart", cart);

		return response;
	}

	@Override
	public void updateCartItemQuantity(int userId, int productId, int quantity) {
		// Validate user exists
		userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// Validate product exists
		productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		// Fetch cart item for this userId and productId
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);

		if (existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			if (quantity == 0) {
				deleteCartItem(userId, productId);
			} else {
				cartItem.setQuantity(quantity);
				cartRepository.save(cartItem);
			}
		}
	}

	@Override
	public void deleteCartItem(int userId, int productId) {
		// Validate user exists
		userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));

		// Validate product exists
		productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));

		cartRepository.deleteCartItem(userId, productId);
	}
}