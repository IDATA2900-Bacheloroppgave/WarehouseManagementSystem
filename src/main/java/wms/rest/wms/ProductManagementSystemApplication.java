package wms.rest.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import wms.rest.wms.repository.ProductRepository;

@SpringBootApplication
public class ProductManagementSystemApplication { ;

	public static void main(String[] args) {
		SpringApplication.run(ProductManagementSystemApplication.class, args);
	}

}
