package com.pismo.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CustomerBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerBankingApplication.class, args);
	}

}
