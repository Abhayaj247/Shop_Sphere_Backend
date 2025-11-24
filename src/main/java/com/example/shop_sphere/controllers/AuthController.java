package com.example.shop_sphere.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_sphere.dto.LoginRequest;
import com.example.shop_sphere.entities.User;
import com.example.shop_sphere.services.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}
	
	@PostMapping("/login")
	@CrossOrigin(origins = "http://localhost:5173")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
		
		try {
			//Authenticate user and get the role
			User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
			
			// Generate JWT token
			String token  = authService.generateToken(user);
			
			// Set token as HttpOnly cookie
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false); // Set true in production with HTTPS
			cookie.setPath("/");
			cookie.setMaxAge(3600); //1 hour
			response.addCookie(cookie);
			
			// Return user role in response body
			Map<String, String> responseBody =  new HashMap<>();
			responseBody.put("message", "Login Successfull");
			responseBody.put("role", user.getRole().name());
			
			return ResponseEntity.ok(responseBody);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
		}
	}
	@PostMapping("/logout")
	public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
	    try {

	        // ❌ REMOVED (this causes NullPointerException)
	        // User user = (User) request.getAttribute("authenticatedUser");
	        // authService.logout(user);

	        // ✅ NEW: Clear the cookie safely without accessing user
	        Cookie cookie = new Cookie("authToken", null);
	        cookie.setHttpOnly(true);
	        cookie.setMaxAge(0);
	        cookie.setPath("/");

	        response.addCookie(cookie);

	        Map<String, String> responseBody = new HashMap<>();
	        responseBody.put("message", "Logout successful");

	        return ResponseEntity.ok(responseBody);

	    } catch (RuntimeException e) {

	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "Logout failed");

	        return ResponseEntity.status(500).body(errorResponse);
	    }
	}

}
