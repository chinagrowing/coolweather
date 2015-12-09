package com.coolweather.app.model;

public class County {
	// 190401|À’÷›,190402|≥£ Ï,...
	private int id;
	private String countyname;
	private String countycode;
	private int cityid;

	public int getId() {
		return id;
	}

	public String getCountyName() {
		return countyname;
	}

	public String getCountyCode() {
		return countycode;
	}

	public int getCityId() {
		return cityid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCountyName(String countyname) {
		this.countyname = countyname;
	}

	public void setCountyCode(String countycode) {
		this.countycode = countycode;
	}

	public void setCityId(int cityid) {
		this.cityid = cityid;
	}
}
