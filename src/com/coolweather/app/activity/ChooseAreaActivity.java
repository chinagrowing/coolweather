package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.*;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.example.coolweather2.*;

public class ChooseAreaActivity extends Activity {

	public final static int LEVEL_PROVINCE = 0;
	public final static int LEVEL_CITY = 1;
	public final static int LEVEL_COUNTY = 2;

	private TextView titleText;
	private ListView listView;
	private ProgressDialog progressDialog;

	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	private int currentLevel;
	private Province selectedProvince;
	private City selectedCity;
	private CoolWeatherDB coolWeatherDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		// android.R.layout.������R.layout.
		// ���⣬Adapter�ǽӿڣ�ArrayAdapterʵ��Adapter��ArrayAdapter<T>��һ����������T
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				}
			}

		});
		queryProvinces();
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province p : provinceList) {
				dataList.add(p.getProvinceName());
			}
			// ��֪��notify�����ݣ�Data�������ı䣨SetChanged��
			adapter.notifyDataSetInvalidated();
			// ��λ���ʼλ�ã���ǰ��������ʱ��������listView.setSelection(dataList.size())
			// ����λ�����һ�У�
			listView.setSelection(0);
			currentLevel = LEVEL_PROVINCE;
			titleText.setText("�й�");
		} else {// �ʼʲô���ݶ�û�У���Ҫ�ӷ����������磩��������
			queryFromServer(null, "province");
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City c : cityList) {
				dataList.add(c.getCityName());
			}
			adapter.notifyDataSetInvalidated();
			listView.setSelection(0);
			currentLevel = LEVEL_CITY;
			titleText.setText(selectedProvince.getProvinceName());
		} else {
			// selectedProvince.getProvinceCode()�ڲ�ѯ��Ӧʡ�ݵ�citiesʱ�õ�
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadConties(selectedCity.getId());
		if (countyList.size() > 0) {
			Log.d("queryCounties", "cityList.size() > 0");
			dataList.clear();
			for (County c : countyList) {
				dataList.add(c.getCountyName());
			}
			adapter.notifyDataSetInvalidated();
			listView.setSelection(0);
			currentLevel = LEVEL_COUNTY;
			titleText.setText(selectedCity.getCityName());
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ�������ݡ�
	 */
	private void queryFromServer(final String code, final String type) {
		// ����address
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					// �����������ݽ�������뵽DB��
					result = Utility.handleProvincesResponse(coolWeatherDB,
							response);
				} else if ("city".equals(type)) {
					// ��ǰѡ���Province��id��Ϊcity��provinceId,ʵ��Province��City������Ĺ���
					result = Utility.handleCitiesResponse(coolWeatherDB,
							response, selectedProvince.getId());
				} else if ("county".equals(type)) { 
					result = Utility.handleCountiesResponse(coolWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					// ͨ��runOnUiThread�ص����̴߳����߼�
					// ע�⣺���߳�û����start()
					// Android ����UI�����ַ�������handler��runOnUiThread()
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

		});
	}

	/**
	 * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}
	
	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

}
