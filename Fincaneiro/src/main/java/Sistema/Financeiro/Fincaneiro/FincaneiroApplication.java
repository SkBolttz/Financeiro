package Sistema.Financeiro.Fincaneiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FincaneiroApplication {

	public static void main(String[] args) {
		SpringApplication.run(FincaneiroApplication.class, args);
	}

}
