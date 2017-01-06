/**
 * Project Name:SpliderDouban
 * File Name:Test.java
 * Package Name:com.splider.douban
 * Date:2017年1月5日上午11:27:00
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
*/

package com.splider.douban;

import com.splider.douban.queue.VisitedQueue;
import com.splider.douban.thread.SpliderThread;

/**
 * ClassName:Test <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年1月5日 上午11:27:00 <br/>
 * @author   zhao.chuanxi
 * @version  1.0
 * @since    JDK 1.6
 */
public class Test {
	
	public static long timestamp = 0;
	
	public static void main(String[] args) {
		timestamp = System.currentTimeMillis();
		System.out.println("起始时间：" + timestamp);
		VisitedQueue.putUrl("https://book.douban.com/tag/编程");
		new Thread(new SpliderThread()).start();
	}
}

