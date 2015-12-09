package com.coolweather.app.util;

/*
 * 回调接口从来返回服务结果
 */
//注意加上public，因为其它包会调用到
public interface HttpCallbackListener {
	void onFinish(String response);
 
	void onError(Exception e);
}
