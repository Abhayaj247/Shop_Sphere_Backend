package com.example.shop_sphere.adminservices;

import com.example.shop_sphere.entities.User;

public interface AdminUserService {
	public User modifyUser(Integer userId, String username, String email, String role);
	public User getUserById(Integer userId);
}
