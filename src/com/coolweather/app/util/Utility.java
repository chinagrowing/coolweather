package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	// response为省份数据信息（xml解析得到01|北京,02|上海,03|天津,04|重庆,...）
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length != 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	// response为市级数据信息（xml解析得到1901|南京,1902|无锡,1903|镇江,1904|苏州,...）
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length != 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据
	 */
	// response为县级数据信息（xml解析得到190401|苏州,190402|常熟,...）
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			// 下面这句if条件判断有必要吗，如果response不为空，那么allCounties必然不为空，长度也不会是0.
			if (allCounties != null && allCounties.length != 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 * 
	 * 相应的json数据 {"weatherinfo":
	 * {"city":"北京","cityid":"101010100","temp1":"24℃","temp2":"11℃",
	 * "weather":"雷阵雨转多云","img1":"d4.gif","img2":"n1.gif","ptime":"11:00" } }
	 */
	/**
	 * JSON 语法规则
	 * 
	 * JSON 语法是 JavaScript 对象表示语法的子集。 数据在键值对中;数据由逗号分隔;花括号保存对象;方括号保存数组;
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			Log.d("handleWeatherResponse", response);
			JSONObject jsonObjext = new JSONObject(response);
			JSONObject weatherInfo = jsonObjext.getJSONObject("weatherinfo");
			
			String cityName = weatherInfo.getString("city");
			
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weather = weatherInfo.getString("weather");
			String publishtime = weatherInfo.getString("ptime");
			saveWeatherTnfo(context, cityName, weatherCode, temp1, temp2,
					weather, publishtime);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeatherTnfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishtime) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishtime);
		// 关于format的用法，传入的就是Date对象，然后将Date时间转化为sdf在初始化时设定的格式x
		SimpleDateFormat formater = new SimpleDateFormat("yyyy年M月d日",
				Locale.CHINA);
		editor.putString("current_date", formater.format(new Date()));
		
		editor.putBoolean("city_selected", true);//用于判断是否将天气信息存储
		editor.commit();
	}
}
