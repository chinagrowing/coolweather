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
	 * �����ʹ�����������ص�ʡ������
	 */
	// responseΪʡ��������Ϣ��xml�����õ�01|����,02|�Ϻ�,03|���,04|����,...��
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
	 * �����ʹ�����������ص��м�����
	 */
	// responseΪ�м�������Ϣ��xml�����õ�1901|�Ͼ�,1902|����,1903|��,1904|����,...��
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
	 * �����ʹ�����������ص��ؼ�����
	 */
	// responseΪ�ؼ�������Ϣ��xml�����õ�190401|����,190402|����,...��
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			// �������if�����ж��б�Ҫ�����response��Ϊ�գ���ôallCounties��Ȼ��Ϊ�գ�����Ҳ������0.
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
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ��С�
	 * 
	 * ��Ӧ��json���� {"weatherinfo":
	 * {"city":"����","cityid":"101010100","temp1":"24��","temp2":"11��",
	 * "weather":"������ת����","img1":"d4.gif","img2":"n1.gif","ptime":"11:00" } }
	 */
	/**
	 * JSON �﷨����
	 * 
	 * JSON �﷨�� JavaScript �����ʾ�﷨���Ӽ��� �����ڼ�ֵ����;�����ɶ��ŷָ�;�����ű������;�����ű�������;
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
	 * �����������ص�����������Ϣ�洢��SharedPreferences�ļ��С�
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
		// ����format���÷�������ľ���Date����Ȼ��Dateʱ��ת��Ϊsdf�ڳ�ʼ��ʱ�趨�ĸ�ʽx
		SimpleDateFormat formater = new SimpleDateFormat("yyyy��M��d��",
				Locale.CHINA);
		editor.putString("current_date", formater.format(new Date()));
		
		editor.putBoolean("city_selected", true);//�����ж��Ƿ�������Ϣ�洢
		editor.commit();
	}
}
