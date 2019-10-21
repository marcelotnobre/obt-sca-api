package br.com.obt.sca.api.config.swagger;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig { 
	// @formatter:off
	@Bean
	public Docket api_SCA() {
		
//		 //Adding Header
//        ParameterBuilder aParameterBuilder = new ParameterBuilder();
//        new ParameterBuilder().name("tenantId")
//        					  .defaultValue("desenvolvimento")
//        					  .parameterType("header")
//        					  .modelRef(new ModelRef("string"))
//        					  .description("Tenant Identity")
//        					  .required(true).build();
//
//        java.util.List<Parameter> aParameters = new ArrayList<>();
//        aParameters.add(aParameterBuilder.build());             // add parameter
		
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.obt.sca.api.resource")).paths(PathSelectors.any())
				.build()
				.groupName("obt-sca-api")
				.apiInfo(apiInfo())
				.securitySchemes(Arrays.asList(apiKey()))
				.securityContexts(Arrays.asList(securityContext()));
//			    .globalOperationParameters(aParameters);
	}
	

	@Bean
    public SecurityScheme apiKey() {
       return new ApiKey("apiKey", HttpHeaders.AUTHORIZATION, "header");
    }

	private ApiInfo apiInfo() {
		return new ApiInfo("Sistema de Controle de Acesso - API",
				"Sistema de controle de acesso.",
				"1.0.0",
				"Terms of service",
				new Contact("Vinícius Assis", "www.outerboxtech.com.br", "vinicius@outerbox.tech"),
				"CopyRights",
				"API license URL", Collections.emptyList());
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(new SecurityReference("apiKey", scopes())))
				.forPaths(PathSelectors.any()).build();
	}

	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scopes = { 
				new AuthorizationScope("read", "for read operations"),
				new AuthorizationScope("write", "for write operations") 
			};
		return scopes;
	}
	
    @Component
    @Primary
    public class CustomObjectMapper extends ObjectMapper {

        private static final long serialVersionUID = 1L;

		public CustomObjectMapper() {
            setSerializationInclusion(JsonInclude.Include.NON_NULL);
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            enable(SerializationFeature.INDENT_OUTPUT);
        }
    }
	// @formatter:on

}