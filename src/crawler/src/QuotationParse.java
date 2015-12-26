package cn.edu.nju.parsems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**     
 * 类名称：QuotationParse    
 * 类描述：    典故抓取
 *     
 */
public class QuotationParse {

	/**    
	 * 方法作用：  main函数
	 * @return      
	 * @throws IOException 
	*/
	public static void main(String[] args) throws IOException {
		/***先连接数据库***/
		MongoDBProcess dbHandle = new MongoDBProcess();
		dbHandle.setCollectionStr("qt_parse"); //quotation parse
		dbHandle.connectMongoDB();

		insertQuotations(dbHandle);

		System.out.println("程序运行结束！");
	}

	/**    
	 * 方法作用：  将1025个典故插入数据库
	 * @return      
	*/
	public static void insertQuotations(MongoDBProcess dbHandle)
			throws IOException {
		int count = 1;
		for (int inc = 0; inc < 103; inc++) {

			String urlString = "http://sou-yun.com/AllusionsIndex.aspx?page="
					+ inc;
			String doc_html = getWebContent(urlString); //获取原始的HTML文档页面
			Document doc_text = Jsoup.connect(urlString).get(); //获取HTML的document对象

			String[] ids = getIdItems(doc_html); //获取本页的div的所有ID，即典故的数目
			List<String> titleList = getTitle(doc_html);//获取左边栏目的典故标题
			int titleIndex = 0; //左边栏目标题数组索引
			String cycleId;

			for (String id : ids) {
				if (id != null && id.length() > 0) {
					/*********每次要插入一个典故，包括典面，典源和用例********/
					org.bson.Document oneQtDocument = new org.bson.Document(
							"id", count);
					oneQtDocument.append("title", titleList.get(titleIndex++));

					cycleId = "#" + id + " .allusionBlock"; //拼接字符串，遍历ID
					for (int p = 0; p < 3; p++) {
						if (p == 0) {
							Element resultElements = doc_text.select(cycleId)
									.get(p);
							String res = resultElements.text().replaceFirst(
									"典故", "");
							oneQtDocument.append(Utils.printQuotationType(p),
									res);
							System.out.println(Utils.printQuotationType(p)
									+ "\n" + res); //输出内容

						}
						if (p == 1) {
							Element resultElements = doc_text.select(cycleId)
									.get(p);
							oneQtDocument.append(Utils.printQuotationType(p),
									resultElements.text());
							System.out.println(Utils.printQuotationType(p)
									+ "\n" + resultElements.text()); //输出内容
						} else {
							Element resultElements = doc_text.select(cycleId)
									.last();
							oneQtDocument.append(Utils.printQuotationType(p),
									resultElements.text());
							System.out.println(Utils.printQuotationType(p)
									+ "\n" + resultElements.text()); //输出内容
						}

					}
					dbHandle.insertMongoData(oneQtDocument); //插入一条典故，包括典面典源和用例，还有id，id对应的值为count
					System.out.println("---------第" + (count++)
							+ "个典故插入数据库成功--------------");
					//					break;
				}
			}
			//			break;
		}
	}

	/**    
	 * 方法作用：  抓取左边栏目的标题,返回数组
	 * @return      
	*/
	public static List<String> getTitle(String doc_text) {
		if (doc_text == null || doc_text.length() <= 0)
			return null;

		Pattern pattern = Pattern
				.compile("<div id=\"IndexPanel\" class=\"list\".*?>(.*?)</div>");
		Matcher matcher = pattern.matcher(doc_text);
		String titleArray[];
		String resultStr = "";
		if (matcher.find())
			resultStr = matcher.group(0);
		titleArray = resultStr.split("<a href='javascript.*?>");
		for (int k = 0; k < titleArray.length && titleArray[k] != null
				&& titleArray[k].length() > 0; k++) {
			titleArray[k] = Utils.replace(titleArray[k]);
		}
		List<String> titlelList = new ArrayList<String>();
		for (int i = 0; i < titleArray.length; i++) {
			if (titleArray[i] != null && titleArray[i].length() > 0
					&& !titleArray[i].equals("")) {
				titlelList.add(titleArray[i]);
			}
		}
		return titlelList;
	}

	/**    
	 * 方法作用：  抓取div里的id列表，然后逐个遍历
	 * @return      
	*/
	public static String[] getIdItems(String doc_text) {
		if (doc_text == null || doc_text.length() <= 0)
			return null;

		String[] ids = new String[20];
		Pattern pt = Pattern.compile("<div id='item_(.*?)'.*?>");
		Matcher matcher = pt.matcher(doc_text);
		String s = null;
		int i = 0;
		while (matcher.find()) {
			s = matcher.group(0);
			s = s.split("\'")[1].trim();
			ids[i] = s;
			i++;
		}
		return ids;
	}

	/**    
	 * 方法作用：  jsoup解析URL里的内容，返回document
	 * @return      
	*/
	public static Document getFromUrl(String url) throws IOException {
		if (url == null || url.length() <= 0)
			return null;

		Document doc = null;
		// blacklist、blacklistdetail页面等
		Connection con = Jsoup.connect(url).timeout(50000000)
				.maxBodySize(1000000000).ignoreContentType(true)
				.followRedirects(false);// 获取请求连接
		// 浏览器可接受的MIME类型
		con.header("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		con.header("Accept-Encoding", "gzip, deflate");
		con.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		con.header("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		try {
			doc = con.get();

		} catch (Exception e) {
			System.out.println(e.getMessage() + " has problem.");
		}
		return doc;
	}

	/**    
	 * 方法作用：  获取原始的HTML文档，包含标签
	 * @return      
	*/
	public static String getWebContent(String domain) {
		if (domain == null || domain.length() <= 0)
			return null;

		StringBuffer sb = new StringBuffer();
		try {
			java.net.URL url = new java.net.URL(domain);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
