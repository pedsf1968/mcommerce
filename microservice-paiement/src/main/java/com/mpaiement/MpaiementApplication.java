package com.mpaiement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.mpaiement")
public class MpaiementApplication {
	public static void main(String[] args) {
		SpringApplication.run(MpaiementApplication.class, args);
	}
}