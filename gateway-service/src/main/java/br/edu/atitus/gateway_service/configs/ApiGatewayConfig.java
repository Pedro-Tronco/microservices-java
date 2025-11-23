package br.edu.atitus.gateway_service.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.ws.rs.ServiceUnavailableException;

@Configuration
public class ApiGatewayConfig {

	@Bean
	RouteLocator getRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/get")
						.filters(f -> f)
						.uri("http://httpbin.org:80"))
				.route(p -> p
						.path("/products/**")
						.uri("lb://product-service"))
				.route(p -> p
						.path("/ws/products/**")
						.uri("lb://product-service"))
				.route(p -> p
						.path("/currency/**")
						.uri("lb://currency-service"))
				.route(p -> p
						.path("/ws/currency/**")
						.uri("lb://currency-service"))
				.route(p -> p
						.path("/greeting/**")
						.uri("lb://greeting-service"))
				.route(p -> p.
						path("/auth/**").
						uri("lb://auth-service"))
				.route(p -> p.
						path("/ws/orders/**")
						.uri("lb://order-service"))
				.route(p -> p.
						path("/ws/inventory/**").
						uri("lb://inventory-service"))
				.route(p -> p.
						path("/ws/cart/**").
						uri("lb://cart-service"))
				.build();
	}
	
}
