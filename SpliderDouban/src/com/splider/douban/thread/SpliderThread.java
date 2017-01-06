/**
 * Project Name:SpliderDouban
 * File Name:SpliderThread.java
 * Package Name:com.splider.douban
 * Date:2017年1月5日上午8:43:14
 * Copyright (c) 2017, zhao.chuanxi All Rights Reserved.
 *
 */

package com.splider.douban.thread;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.splider.douban.Test;
import com.splider.douban.bean.BookDetail;
import com.splider.douban.bean.BookDetailComparator;
import com.splider.douban.contants.Contants;
import com.splider.douban.queue.BookQueue;
import com.splider.douban.queue.ContentQueue;
import com.splider.douban.queue.VisitedQueue;

/**
 * ClassName:SpliderThread <br/>
 * Date: 2017年1月5日 上午8:43:14 <br/>
 * @author zhao.chuanxi
 * @version 1.0
 * @since JDK 1.6
 */
public class SpliderThread implements Runnable {

	/**
	 * 本方法为主线程 实现方式为： 1. VisitedQueue中取出url进行页面下载 2.
	 * ContentQueue中取出页面内容，解析页面内容，取出下一页的url地址放到VisitedQueue中，
	 * 取出当前页面的书籍信息，放入到BookQueue中。本工程采用htmlparser对元素进行解析，解析规则比较简单 3.
	 * 当8秒内连续两次BookQueue中书籍数量不变，默认为书籍已经取完，对书籍进行排序到后导出到excel
	 * 
	 */

	public static int CURRENT_THREAD_NUM = 0;// 当前线程数
	private static int LAST_BOOK_BUM = 0;// 上次书籍数量
	private static long LAST_TIME = 0;// 上次时间

	@Override
	public void run() {

		while (true) {
			// 设置最大线程数为20
			if (CURRENT_THREAD_NUM <= Contants.MAX_THREAD_NUM) {
				if (VisitedQueue.getQueueSize() != 0) {
					String url = VisitedQueue.getUrl();
					new Thread(new DownloadThread(url)).start();
					CURRENT_THREAD_NUM = CURRENT_THREAD_NUM + 1;
				} else {
					if (LAST_TIME == 0f) {
						LAST_TIME = System.currentTimeMillis();
					}
					// 当前时间与上次时间间隔为8秒
					if ((System.currentTimeMillis() - LAST_TIME) / 1000 > 8) {
						// 如果本次书籍数量和上次统计的书籍数量一致
						if (BookQueue.getQueueSize() == LAST_BOOK_BUM) {
							System.out.println("最终书籍数量："
									+ BookQueue.getQueueSize());
							exportToExcel();
							break;
						} else {
							System.out.println("查询到书籍数量："
									+ BookQueue.getQueueSize());
							LAST_BOOK_BUM = BookQueue.getQueueSize();
							LAST_TIME = System.currentTimeMillis();
						}
					}
				}
			}
			if (ContentQueue.getQueueSize() != 0) {
				// 获取content内容，解析content，把url放到VisitedQueue中
				// 把book相关内容放到bookDetail中然后放到队列里
				String Content = ContentQueue.getContent();
				analysisContent(Content);
			}
		}
	}

	/**
	 * 
	 * analysisContent:(对下载内容进行分析). <br/>
	 * Date:2017年1月5日上午9:29:54
	 * 
	 * @author zhao.chuanxi
	 * @param content
	 *            下载内容
	 * @since JDK 1.6
	 */
	private void analysisContent(String content) {
		// 查询下一页的url
		setVisitedUrl(content);
		// 查询当前页的书本内容
		setBookDetail(content);
	}

	/**
	 * 
	 * setVisitedUrl:(这里用一句话描述这个方法的作用). <br/>
	 * Date:2017年1月5日下午2:07:47
	 * 
	 * @author zhao.chuanxi
	 * @param content
	 * @since JDK 1.6
	 */
	private void setVisitedUrl(String content) {
		try {
			Parser parser = new Parser();
			parser.setInputHTML(content);
			// 下一页的filter
			NodeFilter cssNext = new CssSelectorNodeFilter(".next");
			NodeList nodesNext = parser.parse(cssNext);
			if (nodesNext.size() > 0) {
				String url = nodesNext.elementAt(0).getChildren().elementAt(3)
						.getText().split("\"")[1];
				VisitedQueue.putUrl(Contants.DOUBAN_URL + url);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * setBookDetail:(这里用一句话描述这个方法的作用). <br/>
	 * Date:2017年1月5日下午2:08:46
	 * 
	 * @author zhao.chuanxi
	 * @param content
	 * @since JDK 1.6
	 */
	private void setBookDetail(String content) {
		try {
			Parser parser = new Parser();
			parser.setInputHTML(content);
			// 每一项的filter
			NodeFilter cssItem = new CssSelectorNodeFilter(".subject-item");
			NodeList nodesNext = parser.parse(cssItem);
			String regEx="[^0-9]";   
			Pattern p = Pattern.compile(regEx);
			for (Node node : nodesNext.toNodeArray()) {
				String bookName = getTextByTag(node.toHtml(), "a");// 获取书名
				String scorePerson = p.matcher(getTextByClass(node.toHtml(), ".pl"))
						.replaceAll("").trim();// 获取评价人数
				String score = getTextByClass(node.toHtml(), ".rating_nums");// 获取评分
				String pub = getTextByClass(node.toHtml(), ".pub");// 获取出版信息
				// author/checker/publisher/publisherDate/price
				String[] pubArray = pub.split("/");// 出版信息分国外和国内
				String author = "";// 书籍作者
				String publisher = "";// 出版社
				String publishDate = "";// 出版日期
				String price = "";// 价格
				if (pubArray.length == 5) {// 外国著作
					author = pubArray[0];
					publisher = pubArray[2];
					publishDate = pubArray[3];
					price = pubArray[4];
				} else if (pubArray.length == 4) {// 国内著作
					author = pubArray[0];
					publisher = pubArray[1];
					publishDate = pubArray[2];
					price = pubArray[3];
				}
				// 临时书籍bean，放入队列
				BookDetail temp = new BookDetail();
				temp.setBookAuthor(author);
				temp.setBookName(bookName);
				temp.setPrice(price);
				temp.setPublishedDate(publishDate);
				temp.setPublisher(publisher);
				temp.setScore(score);
				temp.setScorePerson(scorePerson + "");
				BookQueue.putBook(temp);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * getTextByClass:(根据className获取text内容). <br/>
	 * Date:2017年1月5日下午2:23:01
	 * 
	 * @author zhao.chuanxi
	 * @param content
	 *            hrml内容
	 * @param className
	 *            css名称
	 * @return 如果找不到节点返回null，否则返回第一个节点的文字内容
	 * @since JDK 1.6
	 */
	private String getTextByClass(String content, String className) {
		String result = null;
		try {
			// 获取评价信息
			Parser parserScore = new Parser();
			parserScore.setInputHTML(content);
			NodeFilter cssPl = new CssSelectorNodeFilter(className);
			NodeList nodesPl = parserScore.parse(cssPl);
			if (nodesPl.size() > 0) {
				result = nodesPl.elementAt(0).toPlainTextString();
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * getTextByTag:(通过标签获取url内容-只限于本project). <br/>
	 * Date:2017年1月5日下午2:23:01
	 * 
	 * @author zhao.chuanxi
	 * @param content
	 *            html内容
	 * @param tag
	 *            tag名称
	 * @return 如果找不到第二节点返回null，否则返回第二个节点的文字内容
	 * @since JDK 1.6
	 */
	private String getTextByTag(String content, String tag) {
		String result = null;
		try {
			Parser parserScore = new Parser();
			parserScore.setInputHTML(content);
			NodeFilter tagA = new TagNameFilter(tag);
			NodeList nodesTagA = parserScore.parse(tagA);
			if (nodesTagA.size() > 1) {
				result = nodesTagA.elementAt(1).toPlainTextString()
						.replace("\n", "").replace(" ", "");
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * exportToExcel:(导出到excel). <br/>
	 * Date:2017年1月5日下午5:05:41
	 * 
	 * @author zhao.chuanxi
	 * @since JDK 1.6
	 */
	private void exportToExcel() {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(Contants.SHEET_NAME);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居左
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个左对齐格式

		HSSFCell cell = row.createCell(0);
		cell.setCellValue("序号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("书名");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("评分");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("评价人数");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("作者");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("出版社");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("出版日期");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("价格");
		cell.setCellStyle(style);
		// 去除没有评分的和评价数不足1000的
		List<BookDetail> bookQueueTemp = BookQueue.getAllBook();
		List<BookDetail> bookQueue = new ArrayList<BookDetail>();
		for (int i = 0; i < bookQueueTemp.size(); i++) {
			if(bookQueueTemp.get(i).getScore() != null 
					&& bookQueueTemp.get(i).getScorePerson().length() > 3) {
				bookQueue.add(bookQueueTemp.get(i));
			}
		}
		Collections.sort(bookQueue, new BookDetailComparator());
		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		// 坑爹啊，居然评价过1000的不足100个
		int bookNumMax = Contants.FRONT_NUM;
		if(bookQueue.size() <= Contants.FRONT_NUM) {
			bookNumMax = bookQueue.size();
		}
		for (int i = 0; i < bookNumMax; i++) {
			row = sheet.createRow((int) i + 1);
			BookDetail bookDetail = bookQueue.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell(0).setCellValue(i);
			row.createCell(1).setCellValue(bookDetail.getBookName());
			row.createCell(2).setCellValue(bookDetail.getScore());
			row.createCell(3).setCellValue(bookDetail.getScorePerson());
			row.createCell(4).setCellValue(bookDetail.getBookAuthor());
			row.createCell(5).setCellValue(bookDetail.getPublisher());
			row.createCell(6).setCellValue(bookDetail.getPublishedDate());
			row.createCell(7).setCellValue(bookDetail.getPrice());
		}

		FileOutputStream fout = null;
		// 第六步，将文件存到指定位置
		try {
			fout = new FileOutputStream(Contants.FILE_PATH);
			wb.write(fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		long time = System.currentTimeMillis();
		System.out.println("结束时间：" + time);
		System.out.println("时间差：" + (time - Test.timestamp));
	}
}
