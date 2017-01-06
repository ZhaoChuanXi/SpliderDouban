/**
 * Project Name:SpliderDouban
 * File Name:DownloadThread.java
 * Package Name:com.splider.douban
 * Date:2017年1月5日上午8:51:58
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.splider.douban.thread;

import com.splider.douban.queue.ContentQueue;
import com.splider.douban.utils.Utils;

/**
 * ClassName:DownloadThread <br/>
 * Date:     2017年1月5日 上午8:51:58 <br/>
 * @author   zhao.chuanxi
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class DownloadThread implements Runnable {
	
	private String url;// 下载地址
	
	public DownloadThread(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		String contnet = Utils.getContent(url);
		if(contnet != null) {
			ContentQueue.putContent(contnet);
		}
		SpliderThread.CURRENT_THREAD_NUM = SpliderThread.CURRENT_THREAD_NUM - 1; 
	}

}

