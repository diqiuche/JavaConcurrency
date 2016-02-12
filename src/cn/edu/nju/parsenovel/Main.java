package cn.edu.nju.parsenovel;

import java.io.IOException;

/**
 * @author wytan 爬取小说
 *
 */
public class Main {
	private static String rootUrl = "http://jiaren.org/2015/02/03/xiaoshuo-223/";
	private static int novelPages = 71;
	private static String filePath = "D:/ResultFiles/";
	private static String fileName = "凤倾天阑.doc";

	public static void main(String[] args) {
		String absolutePath = filePath + fileName;
		try {
			for (int i = 1; i <= novelPages; i++) {
				String url = rootUrl + i + "/";
				String article = Utils.getFromUrl(url).select("article").text();
				int lastIndex = article.lastIndexOf("标签");
				String finalArticle = article.substring(0, lastIndex) + "\n======================================\n";
				Utils.writeFile(absolutePath, finalArticle);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
