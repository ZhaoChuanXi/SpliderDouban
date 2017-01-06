/**
 * Project Name:SpliderDouban
 * File Name:BookDetailComparator.java
 * Package Name:com.splider.douban.bean
 * Date:2017年1月5日下午10:20:42
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
*/

package com.splider.douban.bean;

import java.util.Comparator;

/**
 * ClassName:BookDetailComparator <br/>
 * Date:     2017年1月5日 下午10:20:42 <br/>
 * @author   zhao.chuanxi
 * @version  1.0
 * @since    JDK 1.6
 */
public class BookDetailComparator implements Comparator<BookDetail> {

	@Override
	public int compare(BookDetail o1, BookDetail o2) {
		return o2.getScore().compareTo(o1.getScore());
	}

}

