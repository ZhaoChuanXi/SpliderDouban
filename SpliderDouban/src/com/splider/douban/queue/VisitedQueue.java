/**
 * Project Name:SpliderDouban
 * File Name:VisitedQueue.java
 * Package Name:com.splider.douban
 * Date:2017年1月4日下午10:39:31
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.splider.douban.queue;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * ClassName:VisitedQueue <br/>
 * Function: 存放访问地址url的队列. <br/>
 * Date:     2017年1月4日 下午10:39:31 <br/>
 * @author   zhao.chuanxi
 * @version  1.0
 * @since    JDK 1.6
 */
public class VisitedQueue {
	/**
	 * 访问url集合，自带线程安全
	 */
	private static LinkedBlockingDeque<String> visitUrl = new LinkedBlockingDeque<String>();
	
	/**
	 * 
	 * putUrl:(把带访问的url放到队列). <br/>
	 * Date:2017年1月4日下午10:52:01
	 * @author zhao.chuanxi
	 * @param url
	 * @since JDK 1.6
	 */
	public static void putUrl(String url) {
		visitUrl.add(url);
	}
	
	/**
	 * 
	 * getUrl:(从队列中获取url). <br/>
	 * Date:2017年1月4日下午10:52:09
	 * @author zhao.chuanxi
	 * @return url，如果发生异常则返null
	 * @since JDK 1.6
	 */
	public static String getUrl() {
		String result = null;
			try {
				result = visitUrl.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	/**
	 * 
	 * getQueueSize:(获取队列数量). <br/>
	 * Date:2017年1月5日上午11:36:39
	 * @author zhao.chuanxi
	 * @return 该队列元素数量
	 * @since JDK 1.6
	 */
	public static int getQueueSize() {
		return visitUrl.size();
	}

}

