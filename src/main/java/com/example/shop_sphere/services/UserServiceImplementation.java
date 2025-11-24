package com.example.shop_sphere.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shop_sphere.entities.User;
import com.example.shop_sphere.repositories.UserRepository;

@Service
public class UserServiceImplementation implements UserService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	
	public UserServiceImplementation(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}


	@Override
	public User registerUser(User user) {
		
		//Check if username or email already exists
		if(userRepository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username is already exists");
		}
		
		if(userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already exists");
		}
		
		// Encode password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		// Save the user
		return userRepository.save(user);
	}

}
