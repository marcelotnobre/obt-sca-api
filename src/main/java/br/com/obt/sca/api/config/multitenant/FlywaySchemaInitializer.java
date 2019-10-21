package br.com.obt.sca.api.config.multitenant;

import lombok.AllArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

import br.com.obt.sca.api.system.properties.DataSourceProperties;

import javax.annotation.PostConstruct;

@Configuration
@AllArgsConstructor
public class FlywaySchemaInitializer {

    private final TenantRoutingDataSource dataSource;
    private final DataSourceProperties dataSourceProperties;

    @PostConstruct
    public void migrateFlyway() {
        dataSourceProperties.getDatasources().forEach(dataSourceProperty -> {
            if (!dataSourceProperty.getName().contains("desenvolvimento")) {
                TenantLocalStorage.setTenantName(dataSourceProperty.getName());
                Flyway flyway = Flyway.configure().dataSource(dataSource).load();
                flyway.migrate();
            }
        });
        TenantLocalStorage.setTenantName("desenvolvimento");
    }
}