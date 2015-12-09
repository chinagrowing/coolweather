package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.message.BufferedHeader;

import android.util.Log;

/*
 * �ӷ������˻�ȡ����
 */
public class HttpUtil {
	/*
	 * ����Http����õ����� �ڷ����ڲ�������һ�����̣߳�Ȼ�������߳���ִ�о��������������������߳����޷�ͨ��return��䷵�����ݣ�
	 * ���Խ������������Ӧ�����ݴ��뵽HttpCallbackListener��onfinish�����У��������쳣����onError��
	 * �漰���������⣺1��ΪʲôҪ�����߳� 2����δ����߳��з��ؽ�������
	 */
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		// �����̵߳����ַ������̳�Thread���ʵ��Runnable�ӿ�
		// ͨ��Runnable�ӿڴ����Ĳ����̶߳��󣬱���Ҫ����Thread��
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					// URL ���ӿ����������/��������������ʹ�� URL ���ӽ������룬�� DoInput ��־����Ϊ
					// true�����������ʹ�ã�������Ϊ false��Ĭ��ֵΪ true��
					connection.setDoInput(true);
					// �������ʹ�� URL ���ӽ���������� DoOutput ��־����Ϊ true�����������ʹ�ã�������Ϊ
					// false��Ĭ��ֵΪ false��
					//ע�⣬connection.setDoOutput(true);����д����������java.io.FileNotFoundException�Ĵ���
					//����������⣬����Ϊ������ֻ�������ݲ��������ݣ�����Ϊtrue������Ϊ����������������ݶ�����
					//connection.setDoOutput(true);
					// �õ������е�һ�����������ڵ������ֽ�������InputStream��OutputStream�����ֽ�����
					InputStream in = connection.getInputStream();
					// 1�����ֽ���ת��Ϊ�ַ�����Reader/Writer���������ַ�������InputStreamReader��ʾת��
					// 2�����ڵ�����װ���ܹ��л��幦�ܵĻ�����
					// ��������java����õ�˵��http://www.cnblogs.com/shitouer/archive/2012/12/19/2823641.html
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					// ֻ���ַ�������readLine���ܣ�д��ΪNewLine��
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
