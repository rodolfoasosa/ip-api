package com.meli.ipapi.controllers;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.ipapi.exceptions.BadRequestException;
import com.meli.ipapi.exceptions.ForbiddenException;
import com.meli.ipapi.models.CountryInfo;
import com.meli.ipapi.services.CacheService;
import com.meli.ipapi.services.IpApiService;


@RestController
@RequestMapping("/")
public class IpApiController {
	private final IpApiService ipapiService;
	private final CacheService cacheService;
	
	@Autowired
	public IpApiController(IpApiService ipapiService, CacheService cacheService) {
		this.ipapiService = ipapiService;
		this.cacheService = cacheService;
	}
	
	@GetMapping("ip/{ip}")
	public CountryInfo getCountryInfoByIP(@PathVariable String ip) throws ForbiddenException, BadRequestException {
		if (!InetAddressValidator.getInstance().isValid(ip))
			throw new BadRequestException("Ip not valid");
		if (cacheService.isBanned(ip))
			throw new ForbiddenException("Ip banned");
		return ipapiService.getCountryInfoByIP(ip);
	}

	@PostMapping("ban/{ip}")
	public ResponseEntity<JsonNode> banIP(@PathVariable String ip) throws JsonMappingException, JsonProcessingException, BadRequestException {
		if (!InetAddressValidator.getInstance().isValid(ip))
			throw new BadRequestException("Ip not valid");
		cacheService.banIP(ip);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree("{\"status\": \"BANNED\", \"message\": \"Ip " + ip + " has been banned\"}");
        return ResponseEntity.ok(json);
	}
}

