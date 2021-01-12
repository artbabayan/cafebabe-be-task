package com.babayan.babe.cafe.app.configuration.swager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author by artbabayan
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("cafebabe-be-api")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.babayan.babe.cafe.app.controller.rest.v1"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    /**
     * Provides basic information about API.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Cafe Babe application API")
                .description("REST API v1.")
                .termsOfServiceUrl("http://artbabayan.domain/#contact")
                .contact(new Contact("Art Babayan Development", "http://artbabayan.domain/", "support@artbabayan.domain"))
                .version("1.0")
                .build();
    }

}
