package com.coolweather.app.model;

public class City {
	// 1901|南京,1902|无锡,1903|镇江,1904|苏州,...
	private int id;
	private String cityname;
	private String citycode;
	private int provinceid;

	public int getId() {
		return id;
	}

	public String getCityName() {
		return cityname;
	}

	public String getCityCode() {
		return citycode;
	}

	public int getProvinceId() {
		return provinceid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCityName(String cityname) {
		this.cityname = cityname;
	}

	public void setCityCode(String citycode) {
		this.citycode = citycode;
	}

	public void setProvinceId(int provinceid) {
		this.provinceid = provinceid;
	}
}
