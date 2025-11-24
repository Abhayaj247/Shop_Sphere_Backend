package com.example.shop_sphere.services;

import java.util.Map;

import com.example.shop_sphere.entities.User;

public interface OrderService {
	public Map<String, Object> getOrdersForUser(User user);
}
