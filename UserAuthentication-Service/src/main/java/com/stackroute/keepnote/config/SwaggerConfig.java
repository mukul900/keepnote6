package com.stackroute.keepnote.config;

import static springfox.documentation.builders.PathSelectors.regex;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/*As in this class we are implementing Swagger So annotate the class with @Configuration and 
 * @EnableSwagger2
 * 
 */

public class SwaggerConfig {


	/*
	 * Annotate this method with @Bean . This method will return an Object of Docket.
	 * This method will implement logic for swagger
	 */
    
    public Docket productApi() {
    	return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.stackroute.keepnote"))
                .paths(regex("/api/v1/auth.*")).build();
    }


}
