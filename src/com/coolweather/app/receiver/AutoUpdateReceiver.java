package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//注意,AutoUpdateReceiver中实例化Intent,参数context不能够用this来表示
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startActivity(i);
	}
}
