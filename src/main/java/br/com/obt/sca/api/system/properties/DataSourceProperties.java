package br.com.obt.sca.api.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring")
public class DataSourceProperties {
    private List<DataSourceProperty> datasources = new ArrayList<>();
}