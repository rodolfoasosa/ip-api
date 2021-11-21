package com.meli.ipapi.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.meli.ipapi.models.CountryInfo;
import com.meli.ipapi.models.IpInfo;
import com.meli.ipapi.models.db.Blacklist;
import com.meli.ipapi.repositories.BlacklistRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CacheService {
	private final BlacklistRepository blackRepo;
	private Map<String, IpInfo> ipCache;
	private Map<String, CountryInfo> countryCache;
	private Set<String> blackSet; 
	
	public CacheService(BlacklistRepository blackRepo) {
		this.blackRepo = blackRepo;
		this.ipCache = new HashMap<>();
		this.countryCache = new HashMap<>();
		this.blackSet = new HashSet<>();
		loadBlacklistCache();
	}
	
	public void banIP(String ip) {
		log.debug("banIP: " + ip);
		if (blackSet.add(ip)) {
			log.debug("banIP added: " + ip);
			blackRepo.save(Blacklist.builder().ip(ip).build());
		}
	}
	
	public boolean isBanned(String ip) {
		if (blackSet.contains(ip)) {
			return true;
		}
		return false;
	}
	
	private void loadBlacklistCache() {
		log.info("loadBlacklistCache...");
		blackRepo.findAll().forEach(b -> blackSet.add(b.getIp()));
	}
	
	public void addIpCache(String ip, IpInfo ipInfo) {
		ipCache.put(ip, ipInfo);
	}
	
	public IpInfo isIpCached(String ip) {
		return ipCache.get(ip);
	}
	
	public void addCountryCache(CountryInfo countryInfo) {
		countryCache.put(countryInfo.getCountryIsoCode(), countryInfo);
		
	}
	
	public CountryInfo isCountryCached(String countryCode) {
		return countryCache.get(countryCode);
	}
}
