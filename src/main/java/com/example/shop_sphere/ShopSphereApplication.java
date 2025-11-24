package com.example.shop_sphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ShopSphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopSphereApplication.class, args);
	}

}
