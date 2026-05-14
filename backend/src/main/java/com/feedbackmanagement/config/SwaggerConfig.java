package com.feedbackmanagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Feedback Management System API",
                description = "Production-ready REST API for managing user feedbacks with JWT authentication, role-based authorization, and full CRUD operations",
                version = "1.0.0",
                contact = @Contact(
                        name = "Feedback Management System",
                        email = "support@feedbackmanagement.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Development Server")
        }
)
public class SwaggerConfig {
}