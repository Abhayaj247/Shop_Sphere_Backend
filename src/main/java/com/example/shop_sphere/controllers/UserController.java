	package com.example.shop_sphere.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shop_sphere.entities.User;
import com.example.shop_sphere.services.UserService;

@RestController
@CrossOrigin (origins="http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user){
		try {
			User registeredUser = userService.registerUser(user);
			return ResponseEntity.ok(Map.of("message","User registered successfully", "user", registeredUser));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
		}
	}
}
