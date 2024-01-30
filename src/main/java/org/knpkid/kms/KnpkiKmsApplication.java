package org.knpkid.kms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KnpkiKmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnpkiKmsApplication.class, args);
	}

}
