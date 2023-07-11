package com.diatoz.assignment.swaggerConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name="DiAtoZ Solutions",
                        email="something@diatoz.com",
                        url="www.diatoz.com"
                ),
                description = "Rest API for College Management System",
                title = "College Management System RestAPI specification",
                version = "1.0"
        )
)
@SecurityScheme(
        name="basicAuth",
        scheme = "basic",
        type = SecuritySchemeType.HTTP,
        in= SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

}
