package org.terning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TerningApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerningApplication.class, args);
	}

}
