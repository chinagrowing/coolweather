package com.coolweather.app.model;

public class Province {
	//01|北京,02|上海,03|天津,04|重庆
	private int id; // 区分id和provincecode。id对应数据库Province表中的id，为序号
	private String provincename;
	private String provincecode; // 省份对应的代码

	public int getId() {
		return id;
	}

	public String getProvinceName() {
		return provincename;
	}

	public String getProvinceCode() {
		return provincecode;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setProvinceName(String provincename) {
		this.provincename = provincename;
	}

	public void setProvinceCode(String provincecode) {
		this.provincecode = provincecode;
	}
}
