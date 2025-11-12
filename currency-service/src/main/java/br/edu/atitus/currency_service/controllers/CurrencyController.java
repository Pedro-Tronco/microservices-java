package br.edu.atitus.currency_service.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.currency_service.clients.CurrencyBCClient;
import br.edu.atitus.currency_service.clients.CurrencyBCResponse;
import br.edu.atitus.currency_service.clients.NationalHolidayClient;
import br.edu.atitus.currency_service.clients.NationalHolidayResponse;
import br.edu.atitus.currency_service.entities.CurrencyEntity;
import br.edu.atitus.currency_service.repositories.CurrencyRepository;

@RestController
@RequestMapping("currency")
public class CurrencyController {

	private final CurrencyRepository repository;
	
	private final CurrencyBCClient currencyBCClient;
	
	private final NationalHolidayClient holidayClient;
	
	private final CacheManager cacheManager;
	
	@Value("${server.port}")
	private int serverPort;

	public CurrencyController(
			CurrencyRepository repository, 
			CurrencyBCClient currencyBCClient, 
			CacheManager cacheManager,
			NationalHolidayClient holidayClient) {
		super();
		this.repository = repository;
		this.currencyBCClient = currencyBCClient;
		this.cacheManager = cacheManager;
		this.holidayClient = holidayClient;
	}
	
	@GetMapping("/{value}/{source}/{target}")
	public ResponseEntity<CurrencyEntity> getConversion (
			@PathVariable double value,
			@PathVariable String source,
			@PathVariable String target) throws Exception  {
		
	source = source.toUpperCase();
	target = target.toUpperCase();
	
	String dataSource = "None";
	String keyCache = source + target;
	String nameCache = "CurrencyCache";
	
	CurrencyEntity currency = cacheManager.getCache(nameCache).get(keyCache, CurrencyEntity.class);
	
	if (currency != null) {
		dataSource = "Cache";
	} else {
		currency = new CurrencyEntity();
		currency.setSource(source);
		currency.setTarget(target);
		
		if (source.equals(target)) {
			currency.setConversionRate(1);
		} else {
			try {
				Calendar cal = Calendar.getInstance();
			    cal.setTime(new Date());
			    
			    int dayOfWeek = 0;
			    while (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || isHoliday(cal)) {
			        cal.add(Calendar.DAY_OF_MONTH, -1);
			        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			    }

			    String date = (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR);
				double sourceRate = 1;
				double targetRate = 1;
				
				if (!source.equals("BRL")) {
					CurrencyBCResponse resp = currencyBCClient.getCurrencyBC(source, date);
					if(resp.getValue().isEmpty()) throw new Exception();
					sourceRate = resp.getValue().
							get(resp.getValue().size() - 1).getCotacaoVenda();
				}
				
				if (!target.equals("BRL")) {
					CurrencyBCResponse resp = currencyBCClient.getCurrencyBC(target, date);
					if(resp.getValue().isEmpty()) throw new Exception();
					targetRate = resp.getValue().
							get(resp.getValue().size() - 1).getCotacaoVenda();
				}
				
				currency.setConversionRate(sourceRate / targetRate);
				dataSource = "API BCB (" + date + ")";
			} catch (Exception e) {
				currency = repository.findBySourceAndTarget(source, target)
						.orElseThrow(() -> new Exception("Currency not found"));
						
						dataSource = "Local Database";
			}
		}
	}
	
	
	cacheManager.getCache(nameCache).put(keyCache, currency);
	
	currency.setConvertedValue(value * currency.getConversionRate());
	currency.setEnviroment("Currency running in port:" + serverPort 
							+ " | Source: " + dataSource);
	
	
	return ResponseEntity.ok(currency);
	
	}
	
	private boolean isHoliday(Calendar cal) throws Exception {
		LocalDate targetDate = cal.toInstant()
				  .atZone(ZoneId.systemDefault())
                  .toLocalDate();
		
		String keyCache = "HolidayList";
		String nameCache = "HolidayCache";
		
		ArrayList<NationalHolidayResponse> holidays = cacheManager.getCache(nameCache).get(keyCache, ArrayList.class);
		if(holidays == null || holidays.isEmpty()) {
			holidays = new ArrayList<>(holidayClient.getNationalHolidays(cal.get(Calendar.YEAR)));
			cacheManager.getCache(nameCache).put(keyCache, holidays);
		}
		
		for (NationalHolidayResponse holiday : holidays) {
	    	String dateString = holiday.getDate();
	        LocalDate holidayDate = LocalDate.parse(dateString);
	    	
	    	if (targetDate.equals(holidayDate)) {
	    		return true;
	    	}
	    }
	    
	    return false;
	};
	
}
