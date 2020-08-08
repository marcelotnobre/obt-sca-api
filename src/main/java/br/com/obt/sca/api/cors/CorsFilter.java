package br.com.obt.sca.api.cors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.obt.sca.api.config.property.OuterBoxTechSCAApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Autowired
    private OuterBoxTechSCAApiProperty outerBoxTechApiProperty;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        /**
         * ********** DESATIVAR localhost PARA AMBIENTES DE PRODUÇÃO ***********
         */
        List<String> authorizedHosts = new ArrayList<>();
        authorizedHosts.add("http://localhost:3000"); // locahost
        authorizedHosts.add("https://outerboxtech.com.br"); // kinghost
        authorizedHosts.add("https://obt-clinica-web.herokuapp.com"); // herokuClinica
        authorizedHosts.add("https://obt-sca-web.herokuapp.com/"); // herokuSCA
        String headerOrigin = request.getHeader("Origin");
        String originPermitida = "";

        System.out.println(headerOrigin);

        if (headerOrigin != null && authorizedHosts.contains(headerOrigin)) {
            originPermitida = headerOrigin;
        } else {
            originPermitida = outerBoxTechApiProperty.getOriginPermitida();
        }
        System.out.println(originPermitida);

        response.setHeader("Access-Control-Allow-Origin", originPermitida);
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(request.getMethod())
                && (outerBoxTechApiProperty.getOriginPermitida().equals(request.getHeader("Origin"))
                || authorizedHosts.contains(request.getHeader("Origin")))) {

            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
            response.setHeader("Access-Control-Max-Age", "3600");

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
