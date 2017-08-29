package com.basarbk.gameofthree;

import org.h2.server.web.WebServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Go3BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Go3BackendApplication.class, args);
	}
	
	@Bean
	ServletRegistrationBean h2(){
		ServletRegistrationBean s = new ServletRegistrationBean(new WebServlet(), "/h2/*");
		s.addInitParameter("webAllowOthers", "true");
		return s;
	}
	
}
