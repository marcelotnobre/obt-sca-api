package br.com.obt.sca.api.config.security.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Profile("oauth-security")
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // @formatter:off
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
                        "/configuration/**", "/swagger-ui.html", "/webjars/**", "/actuator/**",
                        "/usuarios/usuario/senha/**")
                .permitAll()
                // acessos públicos
                .antMatchers("/usuarios/login/", "/usuarios/login/*").permitAll()
                .antMatchers("/permissoes/ativos/", "/permissoes/ativos/*").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
    // @formatter:on

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.stateless(true);
    }

    @Bean
    public MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
