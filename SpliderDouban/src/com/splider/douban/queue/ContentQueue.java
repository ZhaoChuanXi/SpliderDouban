/**
 * Project Name:SpliderDouban
 * File Name:ContentQueue.java
 * Package Name:com.splider.douban
 * Date:2017年1月4日下午10:39:31
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
*/

package com.splider.douban.queue;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * ClassName:ContentQueue <br/>
 * Function: 存放网上下载下来的网页内容的队列. <br/>
 * Date:     2017年1月4日 下午10:39:31 <br/>
 * @author   zhao.chuanxi
 * @version  1.0
 * @since    JDK 1.6
 */
public class ContentQueue {
	/**
	 * 下载信息集合，自带线程安全
	 */
	private static LinkedBlockingDeque<String> contentQueue = new LinkedBlockingDeque<String>();
	
	/**
	 * 
	 * putUrl:(把下载下来的网页内容放到队列). <br/>
	 * Date:2017年1月4日下午10:52:01
	 * @author zhao.chuanxi
	 * @param contnet
	 * @since JDK 1.6
	 */
	public static void putContent(String contnet) {
		contentQueue.add(contnet);
	}
	
	/**
	 * 
	 * getUrl:(从队列中获取网页下载的内容). <br/>
	 * Date:2017年1月4日下午10:52:09
	 * @author zhao.chuanxi
	 * @return content，如果发生异常则返null
	 * @since JDK 1.6
	 */
	public static String getContent() {
		String result = null;
			try {
				result = contentQueue.take();
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
		return contentQueue.size();
	}

}

