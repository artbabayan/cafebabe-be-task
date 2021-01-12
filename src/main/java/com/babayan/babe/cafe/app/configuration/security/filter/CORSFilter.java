package com.babayan.babe.cafe.app.configuration.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author artbabayan
 */
@Log4j2
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
    private static final String ORIGIN = "Origin";

    @Value("#{'${cors.allowed.origins}'.split(',')}")
    private List<String> allowedOrigins;

    public CORSFilter() {
        log.info("***** CORSFilter init *****");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(request.getHeader(ORIGIN)) ? request.getHeader(ORIGIN) : "");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");

        log.debug("Request method : " + request.getMethod());
        log.debug("Origin: " + request.getHeader(ORIGIN));
        log.debug("Access-Control-Allow-Origin: " + response.getHeader("Access-Control-Allow-Origin"));
        log.debug("Access-Control-Allow-Credentials: " + response.getHeader("Access-Control-Allow-Credentials"));
        log.debug("Access-Control-Allow-Methods: " + response.getHeader("Access-Control-Allow-Methods"));
        log.debug("Access-Control-Max-Age: " + response.getHeader("Access-Control-Max-Age"));
        log.debug("Access-Control-Allow-Headers: " + response.getHeader("Access-Control-Allow-Headers"));

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

}
