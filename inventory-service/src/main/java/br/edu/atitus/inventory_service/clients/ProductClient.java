package br.edu.atitus.inventory_service.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/noconverter/{id}")
    ProductResponse getProductById(@PathVariable Long id);

    @GetMapping("/products/query")
    List<Long> getProductIdFromQuery(@RequestParam String sqlQuery);
    
}