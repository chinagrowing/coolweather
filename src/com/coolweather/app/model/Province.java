package com.coolweather.app.model;

public class Province {
	//01|����,02|�Ϻ�,03|���,04|����
	private int id; // ����id��provincecode��id��Ӧ���ݿ�Province���е�id��Ϊ���
	private String provincename;
	private String provincecode; // ʡ�ݶ�Ӧ�Ĵ���

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
