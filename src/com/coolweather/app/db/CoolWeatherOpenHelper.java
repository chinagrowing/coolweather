package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	// ���û������Ĺ��캯��������֣�û��Ϊȱʡ���캯��������ʽ�����캯�� SQLiteOpenHelper()�����붨����ʽ���캯��
	// ���ͣ�SQLiteOpenHelper�еĹ��캯��ȫ��Ϊ�вΣ����CoolWeatherOpenHelper�����幹�캯������ô����Ĭ��Ϊ
	// ����һ���޲ι��캯�������޲ι��캯���л���ø��ࣨ��SQLiteOpenHelper�����޲ι��캯������Ȼ�ͻ����ì�ܡ�
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/*
	 * Province �������
	 */
	public static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement," + "province_name text,"
			+ "province_code text)";

	/*
	 * City �������
	 */
	public static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "city_code text," + "province_id integer)";

	/*
	 * Country �������
	 */
	public static final String CREATE_COUNTY = "create table County("
			+ "id integer primary key autoincrement," + "county_name text,"
			+ "county_code text," + "city_id integer)";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);//����Province��
		db.execSQL(CREATE_COUNTY);	//����County��
		db.execSQL(CREATE_CITY);	//����City��
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
