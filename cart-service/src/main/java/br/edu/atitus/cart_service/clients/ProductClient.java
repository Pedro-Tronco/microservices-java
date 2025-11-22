package br.edu.atitus.cart_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.atitus.cart_service.clients.ProductResponse;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/noconverter/{id}")
    ProductResponse getProductById(@PathVariable Long id);
    
    @GetMapping("/products/{id}/{targetCurrency}")
    ProductResponse getProductByIdWithCurrency(@PathVariable Long id, @PathVariable String targetCurrency);
    
}