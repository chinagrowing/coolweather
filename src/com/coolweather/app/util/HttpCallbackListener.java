package com.coolweather.app.util;

/*
 * �ص��ӿڴ������ط�����
 */
//ע�����public����Ϊ����������õ�
public interface HttpCallbackListener {
	void onFinish(String response);
 
	void onError(Exception e);
}
