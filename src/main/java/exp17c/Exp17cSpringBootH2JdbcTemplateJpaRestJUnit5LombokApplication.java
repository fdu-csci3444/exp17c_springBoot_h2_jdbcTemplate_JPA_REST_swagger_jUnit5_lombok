package exp17c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableSwagger2	// NOTE ilker this annotation in either the main SpringBootApplication class like below or in a config class like SwaggerConfig enables swagger
public class Exp17cSpringBootH2JdbcTemplateJpaRestJUnit5LombokApplication {

	public static void main(String[] args) {
		SpringApplication.run(Exp17cSpringBootH2JdbcTemplateJpaRestJUnit5LombokApplication.class, args);
	}

}
