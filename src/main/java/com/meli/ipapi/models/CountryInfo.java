package com.meli.ipapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryInfo {
	private String countryName;
	private String countryIsoCode;
	private Exchange exchange;
}
