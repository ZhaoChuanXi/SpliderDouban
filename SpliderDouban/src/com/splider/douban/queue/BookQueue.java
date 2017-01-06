/**
 * Project Name:SpliderDouban
 * File Name:BookQueue.java
 * Package Name:com.splider.douban
 * Date:2017年1月4日下午10:39:31
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
*/

package com.splider.douban.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import com.splider.douban.bean.BookDetail;

/**
 * BookQueue <br/>
 * Function: 存放图书信息的队列. <br/>
 * Date:     2017年1月4日 下午10:39:31 <br/>
 * @author   zhao.chuanxi
 * @version  1.0
 * @since    JDK 1.6
 */
public class BookQueue {
	/**
	 * 图书信息集合，自带线程安全
	 */
	private static LinkedBlockingDeque<BookDetail> bookQueue = new LinkedBlockingDeque<BookDetail>();
	
	/**
	 * 
	 * putUrl:(把解析好的图书信息放到队列). <br/>
	 * Date:2017年1月4日下午10:52:01
	 * @author zhao.chuanxi
	 * @param book
	 * @since JDK 1.6
	 */
	public static void putBook(BookDetail book) {
		bookQueue.add(book);
	}
	
	/**
	 * 
	 * getBook:(从队列中获取book). <br/>
	 * Date:2017年1月4日下午10:52:09
	 * @author zhao.chuanxi
	 * @return bookDetail，如果发生异常则返null
	 * @since JDK 1.6
	 */
	public static BookDetail getBook() {
		BookDetail result = null;
			try {
				result = bookQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	/**
	 * 
	 * getAllBook:(从队列中获取所有的book). <br/>
	 * Date:2017年1月4日下午10:52:09
	 * @author zhao.chuanxi
	 * @return bookDetail集合，如果发生异常则返null
	 * @since JDK 1.6
	 */
	public static List<BookDetail> getAllBook() {
		List<BookDetail> result = new ArrayList<BookDetail>();
		int maxSize = bookQueue.size();
			try {
				for (int i = 0; i < maxSize; i++) {
					result.add(bookQueue.take());
				}
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
		return bookQueue.size();
	}

}

