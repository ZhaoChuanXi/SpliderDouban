/**
 * Project Name:SpliderDouban
 * File Name:Utils.java
 * Package Name:com.splider.douban.utils
 * Date:2017年1月4日下午10:54:17
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
*/

package com.splider.douban.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ClassName:Utils <br/>
 * Function: 工具类. <br/>
 * Date:     2017年1月4日 下午10:54:17 <br/>
 * @author   zhao.chuanxi
 * @version  1.0
 * @since    JDK 1.6
 */
public class Utils {

	/**
	 * 
	 * getContent:(通过get方式获取页面内容). <br/>
	 * Date:2017年1月4日下午10:57:46
	 * @author zhao.chuanxi
	 * @param urlParams url地址
	 * @return 页面内容
	 * @since JDK 1.6
	 */
	public static String getContent(String urlParams) {
		String result = null;
		OutputStreamWriter out = null;// url输出流
		InputStream is = null;// response输入流
		ByteArrayOutputStream outSteam = null;// 把输入流转换成对应的字符流，最后转换成字符串
		try {
			URL url = new URL(urlParams);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("GET"); // 设置请求方式
			connection.connect();
			out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.flush();
			out.close();
			// 读取响应
			is = connection.getInputStream();
			outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			is.close();
			byte[] data = outSteam.toByteArray();
			result = new String(data, "UTF-8"); // utf-8编码
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outSteam != null) {
				try {
					outSteam.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
}

