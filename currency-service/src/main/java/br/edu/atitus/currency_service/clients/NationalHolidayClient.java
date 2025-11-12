package br.edu.atitus.currency_service.clients;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "NationalHoliday",
			url = "https://brasilapi.com.br/api/feriados/v1")
public interface NationalHolidayClient {

	@GetMapping("/{year}")
	ArrayList<NationalHolidayResponse> getNationalHolidays(
			@PathVariable int year
	);
}
