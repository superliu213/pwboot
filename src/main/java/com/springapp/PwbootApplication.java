package com.springapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.springapp.*.dao.impl")
@SpringBootApplication
public class PwbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(PwbootApplication.class, args);
	}
}
