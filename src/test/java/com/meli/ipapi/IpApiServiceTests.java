package com.meli.ipapi;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.meli.ipapi.models.CountryInfo;
import com.meli.ipapi.models.Exchange;
import com.meli.ipapi.models.IpInfo;
import com.meli.ipapi.services.CacheService;
import com.meli.ipapi.services.IpApiService;


@SpringBootTest
class IpApiServiceTests {

	@Autowired
	private CacheService cacheService;
	@Autowired
	private IpApiService ipApiService;
	
	@Test
	void testBannedIp() {
		cacheService.banIP("100.200.12.123"); 
		assertEquals("IP was not cached", true, cacheService.isBanned("100.200.12.123"));
		assertEquals("IP was cached", false, cacheService.isBanned("999.999.999.99"));
	}

	@Test
	void testCountryCache() {
		cacheService.addCountryCache(CountryInfo.builder()
				                     .countryIsoCode("AR")
				                     .countryName("Argentina")
				                     .exchange(Exchange.builder()
				                    		           .baseCurrency("USD")
				                    		           .currency("ARS")
				                    		           .rate(200.0)
				                    		           .build())
				                     .build());
		
		CountryInfo countryInfo = cacheService.isCountryCached("AR");
		
		assertEquals("countryName doesnt match","Argentina", countryInfo.getCountryName());
		assertEquals("countryIsoCode doesnt match","AR", countryInfo.getCountryIsoCode());
		assertEquals("baseCurrency doesnt match", "USD", countryInfo.getExchange().getBaseCurrency());
		assertEquals("currency doesnt match", "ARS", countryInfo.getExchange().getCurrency());
		assertEquals("rate doesnt match", 200, countryInfo.getExchange().getRate(), 0);
		assertEquals(null, cacheService.isCountryCached("BR"));
	}
	
	@Test
	void testIpCache() {
		cacheService.addIpCache("100.200.12.123", IpInfo.builder()
				                                        .country_code("AR")
				                                        .country_name("Argentina")
				                                        .build()); 
		assertEquals("IP was not cached", "Argentina", cacheService.isIpCached("100.200.12.123").getCountry_name());
		assertEquals("IP was not null", null, cacheService.isIpCached("999.999.999.99"));
	}
	
	@Test
	void testCountryInfoResponse() {
		cacheService.addIpCache("100.200.12.123", IpInfo.builder()
				                                        .country_code("AR")
				                                        .country_name("Argentina")
				                                        .build()); 
		
		cacheService.addCountryCache(CountryInfo.builder()
                .countryIsoCode("AR")
                .countryName("Argentina")
                .exchange(Exchange.builder()
               		           .baseCurrency("USD")
               		           .currency("ARS")
               		           .rate(200.0)
               		           .build())
                .build());
		
		CountryInfo countryInfo = ipApiService.getCountryInfoByIP("100.200.12.123");
		
		assertEquals("countryName doesnt match","Argentina", countryInfo.getCountryName());
		assertEquals("countryIsoCode doesnt match","AR", countryInfo.getCountryIsoCode());
		assertEquals("baseCurrency doesnt match", "USD", countryInfo.getExchange().getBaseCurrency());
		assertEquals("currency doesnt match", "ARS", countryInfo.getExchange().getCurrency());
		assertEquals("rate doesnt match", 200, countryInfo.getExchange().getRate(), 0);
	}
}
