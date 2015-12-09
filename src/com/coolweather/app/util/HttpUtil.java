package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.message.BufferedHeader;

import android.util.Log;

/*
 * 从服务器端获取数据
 */
public class HttpUtil {
	/*
	 * 发送Http请求得到数据 在方法内部开启了一个子线程，然后在子线程中执行具体的网络操作。由于子线程是无法通过return语句返回数据，
	 * 所以将网络服务器响应的数据传入到HttpCallbackListener的onfinish方法中，若出现异常则传入onError中
	 * 涉及到两个问题：1、为什么要用子线程 2、如何从子线程中返回接收数据
	 */
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		// 定义线程的两种方法：继承Thread类和实现Runnable接口
		// 通过Runnable接口创建的不是线程对象，必须要借助Thread类
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
					// URL 连接可用于输入和/或输出。如果打算使用 URL 连接进行输入，则将 DoInput 标志设置为
					// true；如果不打算使用，则设置为 false。默认值为 true。
					connection.setDoInput(true);
					// 如果打算使用 URL 连接进行输出，则将 DoOutput 标志设置为 true；如果不打算使用，则设置为
					// false。默认值为 false。
					//注意，connection.setDoOutput(true);不能写。否则会出现java.io.FileNotFoundException的错误
					//出现这个问题，我认为是由于只接收数据不传输数据，设置为true反而因为不能向服务器发数据而出错
					//connection.setDoOutput(true);
					// 得到网络中的一段输入流（节点流，字节流——InputStream和OutputStream都是字节流）
					InputStream in = connection.getInputStream();
					// 1、将字节流转换为字符流（Reader/Writer的流都是字符流），InputStreamReader表示转换
					// 2、将节点流包装成能够有缓冲功能的缓冲流
					// 见过关于java流最好的说明http://www.cnblogs.com/shitouer/archive/2012/12/19/2823641.html
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					// 只有字符流才有readLine功能（写入为NewLine）
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
