package org.terning;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.terning.user.config.FcmProperties;

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaAuditing
@EnableConfigurationProperties(FcmProperties.class)
public class TerningApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerningApplication.class, args);
	}

}
