package com.meli.ipapi;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.meli.ipapi.models.CountryInfo;
import com.meli.ipapi.models.Exchange;
import com.meli.ipapi.models.IpInfo;
import com.meli.ipapi.services.CacheService;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = IpApiApplication.class)
//@WebAppConfiguration
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IpApiControllerTests {

	
	@Autowired 
	private MockMvc mvc;
	@Autowired 
	private CacheService cacheService;
	
	@Test
	void testBanIp() throws Exception {
		  mvc.perform(post("/ban/216.58.202.110") .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk()) .andExpect(content()
		  .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		  .andExpect(jsonPath("$.status", is("BANNED")));
	}

	@Test
	void testGetIpInfo() throws Exception {
		cacheService.addIpCache("100.200.12.124", IpInfo.builder()
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
		
		mvc.perform(get("/ip/100.200.12.124") .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk()) .andExpect(content()
		  .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		  .andExpect(jsonPath("$.countryName", is("Argentina")));
		 
	}
	
	@Test
	void testBannedIp() throws Exception {
		  mvc.perform(post("/ban/216.58.202.110") .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isOk()) .andExpect(content()
		  .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		  .andExpect(jsonPath("$.status", is("BANNED")));
		  
		  mvc.perform(get("/ip/216.58.202.110") .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isForbidden());
	}
	
	@Test
	void testIpFormatValidation() throws Exception {		  
		  mvc.perform(get("/ip/216.58.202") .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isBadRequest());
	}
}
