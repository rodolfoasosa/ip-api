package com.meli.ipapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.ipapi.models.Country;
import com.meli.ipapi.models.CountryInfo;
import com.meli.ipapi.models.Exchange;
import com.meli.ipapi.models.ExchangeRate;
import com.meli.ipapi.models.IpInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IpApiService {
	private final CacheService cacheService;
	private final String ipapiAccessKey;
	private final String forexAccessKey;
	private RestTemplate restTemplate;
	private Map<String, Object> result;
	private List<Entry<String, Object>> list;
	private ObjectMapper mapper;
	
	public IpApiService(@Value("${ipapi.accesskey}") String ipapiAccessKey,@Value("${forex.accesskey}") String forexAccessKey, CacheService cacheService) {
		log.debug("IpApiService...");
		this.ipapiAccessKey=ipapiAccessKey;
		this.forexAccessKey=forexAccessKey;
		this.cacheService = cacheService;
		this.restTemplate = new RestTemplate();
		this.mapper = new ObjectMapper();
	}
	
	public CountryInfo getCountryInfoByIP(String ip) {
		log.debug("IpApiService.getCountryInfoByIP: " + ip);
		
		// IP Info //////////////////////////////////////////////////////////////////////////	
		IpInfo ipInfo = cacheService.isIpCached(ip);
		if (ipInfo==null) {	
				ipInfo = restTemplate.getForObject("http://api.ipapi.com/" + ip + "?access_key=" + ipapiAccessKey + "&format=1", IpInfo.class);
				cacheService.addIpCache(ip, ipInfo);
		}
		
		// Get Country Info //////////////////////////////////////////////////////////////////
		CountryInfo countryInfo = cacheService.isCountryCached(ipInfo.getCountry_code());
		if (countryInfo==null) {
			// Get Country Currency //
			Country[] country = restTemplate
					.getForObject("https://restcountries.com/v3.1/alpha/" + ipInfo.getCountry_code(), Country[].class);		
			String countryCurr = parseCurrency(country[0].getCurrencies());
			
			// Get Exchange Rate //
			ExchangeRate forex = restTemplate
					.getForObject("http://data.fixer.io/api/latest?access_key=" + forexAccessKey + "&symbols=" + countryCurr, ExchangeRate.class);	
			double rate = parseRate(forex.getRates());
			
			// Build Country Info for response //
			countryInfo = CountryInfo.builder().countryName(ipInfo.getCountry_name())
					                    .countryIsoCode(ipInfo.getCountry_code())
					                    .exchange(Exchange.builder().currency(countryCurr).baseCurrency(forex.getBase()).rate(rate).build())
					                    .build();
			cacheService.addCountryCache(countryInfo);
		}
		return countryInfo;
	}
	
	private String parseCurrency(JsonNode jn) {
		result = mapper.convertValue(jn, new TypeReference<Map<String, Object>>(){});
		List<Entry<String, Object>> list = new ArrayList<Entry<String, Object>>(result.entrySet());
		return list.get(0).getKey();
	}
	
	private double parseRate(JsonNode jn) {
		result = mapper.convertValue(jn, new TypeReference<Map<String, Object>>(){});
		list = new ArrayList<Entry<String, Object>>(result.entrySet());
		return (double) list.get(0).getValue();
	}
}
