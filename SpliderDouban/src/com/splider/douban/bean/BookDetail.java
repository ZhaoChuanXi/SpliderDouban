/**
 * Project Name:SpliderDouban
 * File Name:BookDetail.java
 * Package Name:com.splider.douban
 * Date:2017年1月4日下午10:43:50
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
 */

package com.splider.douban.bean;


/**
 * ClassName:BookDetail <br/>
 * Function: 书籍详细内容. <br/>
 * Date: 2017年1月4日 下午10:43:50 <br/>
 * @author zhao.chuanxi
 * @version 1.0
 * @since JDK 1.6
 */
public class BookDetail {
	/**
	 * 书名
	 */
	private String bookName;
	/**
	 * 评价得分
	 */
	private String score;
	/**
	 * 评价人数
	 */
	private String scorePerson;
	/**
	 * 书籍作者
	 */
	private String bookAuthor;
	/**
	 * 出版社
	 */
	private String publisher;
	/**
	 * 出版日期
	 */
	private String publishedDate;

	/**
	 * 图书价格
	 */
	private String price;

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getScorePerson() {
		return scorePerson;
	}

	public void setScorePerson(String scorePerson) {
		this.scorePerson = scorePerson;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public void setBookAuthor(String bookAuthor) {
		this.bookAuthor = bookAuthor;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
