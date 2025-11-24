package com.example.shop_sphere.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_sphere.entities.User;
import com.example.shop_sphere.repositories.UserRepository;
import com.example.shop_sphere.services.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/cart")
public class CartController {

	private CartService cartService;

	private UserRepository userRepository;

	public CartController(CartService cartService, UserRepository userRepository) {
		super();
		this.cartService = cartService;
		this.userRepository = userRepository;
	}

	// Fetch userId from username coming from the filter and get cart item count
	@GetMapping("/items/count")
	public ResponseEntity<Integer> getCartItemCount(@RequestParam String username) {
		// Fetch user by username to get the userId
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

		// Call the service to get the total cart item count
		int count = cartService.getCartItemCount(user.getUserId());
		return ResponseEntity.ok(count);
	}

	// Fetch all cart items for the user (based on username)
	@GetMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {
		// Fetch user by username to get the userId
		User user = (User) request.getAttribute("authenticatedUser");
		// User user = userRepository.findByUsername(un)
		// .orElseThrow(() -> new IllegalArgumentException("User not found with
		// username: " + username));

		// Call the service to get cart items for the user
		Map<String, Object> cartItems = cartService.getCartItems(user.getUserId());
		return ResponseEntity.ok(cartItems);
	}

	// Add an item to the cart
	@PostMapping("/add")
	@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
	public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
		try {
			// Get authenticated user from request attribute set by filter
			User authenticatedUser = (User) httpRequest.getAttribute("authenticatedUser");
			if (authenticatedUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Unauthorized: User not authenticated"));
			}

			// Extract productId - handle both Integer and Number types
			Object productIdObj = request.get("productId");
			int productId;
			if (productIdObj instanceof Integer) {
				productId = (Integer) productIdObj;
			} else if (productIdObj instanceof Number) {
				productId = ((Number) productIdObj).intValue();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Invalid productId format"));
			}

			// Handle quantity: Default to 1 if not provided
			int quantity = 1;
			if (request.containsKey("quantity")) {
				Object quantityObj = request.get("quantity");
				if (quantityObj instanceof Integer) {
					quantity = (Integer) quantityObj;
				} else if (quantityObj instanceof Number) {
					quantity = ((Number) quantityObj).intValue();
				}
			}

			// Add the product to the cart
			cartService.addToCart(authenticatedUser.getUserId(), productId, quantity);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Internal server error: " + e.getMessage()));
		}
	}

	// Update Cart Item Quantity
	@PutMapping("/update")
	public ResponseEntity<?> updateCartItemQuantity(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
		try {
			// Get authenticated user from request attribute set by filter
			User authenticatedUser = (User) httpRequest.getAttribute("authenticatedUser");
			if (authenticatedUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Unauthorized: User not authenticated"));
			}

			// Extract productId and quantity - handle both Integer and Number types
			Object productIdObj = request.get("productId");
			int productId;
			if (productIdObj instanceof Integer) {
				productId = (Integer) productIdObj;
			} else if (productIdObj instanceof Number) {
				productId = ((Number) productIdObj).intValue();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Invalid productId format"));
			}

			Object quantityObj = request.get("quantity");
			int quantity;
			if (quantityObj instanceof Integer) {
				quantity = (Integer) quantityObj;
			} else if (quantityObj instanceof Number) {
				quantity = ((Number) quantityObj).intValue();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Invalid quantity format"));
			}

			// Update the cart item quantity
			cartService.updateCartItemQuantity(authenticatedUser.getUserId(), productId, quantity);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Internal server error: " + e.getMessage()));
		}
	}

	// Delete Cart Item
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCartItem(@RequestBody Map<String, Object> request, HttpServletRequest httpRequest) {
		try {
			// Get authenticated user from request attribute set by filter
			User authenticatedUser = (User) httpRequest.getAttribute("authenticatedUser");
			if (authenticatedUser == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("error", "Unauthorized: User not authenticated"));
			}

			// Extract productId - handle both Integer and Number types
			Object productIdObj = request.get("productId");
			int productId;
			if (productIdObj instanceof Integer) {
				productId = (Integer) productIdObj;
			} else if (productIdObj instanceof Number) {
				productId = ((Number) productIdObj).intValue();
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("error", "Invalid productId format"));
			}

			// Delete the cart item
			cartService.deleteCartItem(authenticatedUser.getUserId(), productId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Map.of("error", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Internal server error: " + e.getMessage()));
		}
	}

}
