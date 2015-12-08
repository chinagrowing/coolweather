package com.coolweather.app.model;

public class Country {
	// 190401|À’÷›,190402|≥£ Ï,...
	private int id;
	private String countryname;
	private String countrycode;
	private int cityid;

	public int getId() {
		return id;
	}

	public String getCountryName() {
		return countryname;
	}

	public String getCountryCode() {
		return countrycode;
	}

	public int getCityId() {
		return cityid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCountryName(String countryname) {
		this.countryname = countryname;
	}

	public void setCountryCode(String countrycode) {
		this.countrycode = countrycode;
	}

	public void setCityId(int cityid) {
		this.cityid = cityid;
	}
}
