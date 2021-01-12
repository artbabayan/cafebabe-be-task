package com.babayan.babe.cafe.app;

import com.babayan.babe.cafe.app.util.UserBotService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;

/**
 * @author by artbabayan
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.babayan.babe.cafe.app.repository")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Application {

	//create initial data
	private UserBotService botService;
	@Autowired public void setBotService(UserBotService botService) {
		this.botService = botService;
	}

	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}

	@PostConstruct
	private void populateDb() {
		botService.initUser();
		botService.initWaiter();
		botService.initProduct();
		botService.initTable();
	}

	public static void main(java.lang.String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
