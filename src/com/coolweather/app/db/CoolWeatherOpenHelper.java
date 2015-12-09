package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	// 如果没有下面的构造函数，会出现：没有为缺省构造函数定义隐式超构造函数 SQLiteOpenHelper()。必须定义显式构造函数
	// 解释：SQLiteOpenHelper中的构造函数全部为有参，如果CoolWeatherOpenHelper不定义构造函数，那么它是默认为
	// 存在一个无参构造函数，在无参构造函数中会调用父类（即SQLiteOpenHelper）的无参构造函数，显然就会出现矛盾。
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/*
	 * Province 建表语句
	 */
	public static final String CREATE_PROVINCE = "create table Province("
			+ "id integer primary key autoincrement," + "province_name text,"
			+ "province_code text)";

	/*
	 * City 建表语句
	 */
	public static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "city_code text," + "province_id integer)";

	/*
	 * Country 建表语句
	 */
	public static final String CREATE_COUNTY = "create table County("
			+ "id integer primary key autoincrement," + "county_name text,"
			+ "county_code text," + "city_id integer)";

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);//建立Province表
		db.execSQL(CREATE_COUNTY);	//建立County表
		db.execSQL(CREATE_CITY);	//建立City表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
