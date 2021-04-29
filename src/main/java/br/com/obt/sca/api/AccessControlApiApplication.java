package br.com.obt.sca.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import br.com.obt.sca.api.config.property.OuterBoxTechSCAApiProperty;

@SpringBootApplication
// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaAuditing
@EnableConfigurationProperties(OuterBoxTechSCAApiProperty.class)
public class AccessControlApiApplication {
	// teste commit development
	// teste commit development - usuario configurado
	// teste commit development - teste

	private static ApplicationContext APPLICATION_CONTEXT;
	private static final Logger logger = LoggerFactory.getLogger(AccessControlApiApplication.class);

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(AccessControlApiApplication.class, args);
		logger.debug("-- Aplicação de controle de acesso iniciada com sucesso --");

	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}

	// @Bean
	// public DataSource dataSource(){
	// CustomRoutingDataSource customDataSource=new CustomRoutingDataSource();
	// customDataSource.setTargetDataSources(MasterService.getDataSourceHashMap());
	// return customDataSource;
	// }
}
