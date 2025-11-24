package com.example.shop_sphere.adminservices;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop_sphere.entities.Role;
import com.example.shop_sphere.entities.User;
import com.example.shop_sphere.repositories.JWTTokenRepository;
import com.example.shop_sphere.repositories.UserRepository;

@Service
public class AdminUserServiceImplementation implements AdminUserService{
	
	private final UserRepository userRepository;
	
    private final JWTTokenRepository jwtTokenRepository;
    
	public AdminUserServiceImplementation(UserRepository userRepository, JWTTokenRepository jwtTokenRepository) {
		super();
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
	}

	@Override
	@Transactional
	public User modifyUser(Integer userId, String username, String email, String role) {
		 // Check if the user exists
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User existingUser = userOptional.get();
        // Update user fields
        if (username != null && !username.isEmpty()) {
            existingUser.setUsername(username);
        }
        if (email != null && !email.isEmpty()) {
            existingUser.setEmail(email);
        }
        if (role != null && !role.isEmpty()) {
            try {
                existingUser.setRole(Role.valueOf(role));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role);
            }
        }

        // Delete associated JWT tokens
        jwtTokenRepository.deleteByUserId(userId);

        // Save updated user
        return userRepository.save(existingUser);
	}

	@Override
	public User getUserById(Integer userId) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

}
