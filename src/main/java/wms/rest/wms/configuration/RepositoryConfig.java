package wms.rest.wms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import wms.rest.wms.model.Category;
import wms.rest.wms.model.Product;

/**
 * Class expose ID of entities for easier access during development.
 * Spring Data REST hides the ID's. "Exposing our internal ID's, in most cases,
 * isn't ideal because they mean nothing to external systems.
 */
@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Product.class);
        config.exposeIdsFor(Category.class);

    }
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> {
            config.exposeIdsFor(Product.class);
            config.exposeIdsFor(Category.class);
        });
    }
}