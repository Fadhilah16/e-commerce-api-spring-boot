package com.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ecommerce.models.RoleEntity;
import com.ecommerce.models.enums.RoleTypes;
import com.ecommerce.repository.RoleRepo;

@SpringBootApplication
public class MyEcommerceApplication {
	@Autowired
	private RoleRepo roleRepo;
	public static void main(String[] args) {
		SpringApplication.run(MyEcommerceApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(String[] args){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				if (roleRepo.findByName(RoleTypes.ROLE_ADMIN).isEmpty()) {

					roleRepo.save(new RoleEntity(RoleTypes.ROLE_ADMIN));
				}

				if (roleRepo.findByName(RoleTypes.ROLE_USER).isEmpty()){

					roleRepo.save(new RoleEntity(RoleTypes.ROLE_USER));

				}

				
			}
		};
	}

}
