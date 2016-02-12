package cn.edu.nju.parsenovel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {
	/**
	 * 方法作用： jsoup解析URL里的内容，返回document
	 * 
	 * @return
	 */
	public static Document getFromUrl(String url) throws IOException {
		Document doc = null;
		// blacklist、blacklistdetail页面等
		Connection con = Jsoup.connect(url).timeout(50000000).maxBodySize(1000000000).ignoreContentType(true)
				.followRedirects(false);// 获取请求连接
		// 浏览器可接受的MIME类型
		con.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		con.header("Accept-Encoding", "gzip, deflate");
		con.header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		con.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0");
		try {
			doc = con.get();

		} catch (Exception e) {
			System.out.println(e.getMessage() + " has problem.");
		}
		return doc;
	}

	/**
	 * 方法作用： 将内容写入txt文件,第一个参数是路径，第二个参数是文件内容
	 * 
	 * @return
	 * @throws IOException
	 */
	public static void writeFile(String path, String content) throws IOException {
		FileOutputStream fos = new FileOutputStream(path, true); // 内容追加到文件最后
		FileChannel fc = fos.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(1024 * 1024 * 1024);
		byte[] byteMessage = content.getBytes();
		buf.put(byteMessage);
		buf.flip();
		fc.write(buf);
		fc.close();
		fos.close();

	}
}
