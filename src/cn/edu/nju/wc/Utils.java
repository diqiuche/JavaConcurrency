package cn.edu.nju.wc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 工具类
 * 
 * @author Administrator
 *
 */
public class Utils {
	
	private Lock lock=new ReentrantLock();

	/**
	 * 方法作用： 将内容写入txt文件,第一个参数是路径，第二个参数是文件内容
	 * 
	 * @return
	 * @throws IOException
	 */
	public static void writeFile(String path, String content) throws IOException {
		if (path == null || path.length() <= 0 || content == null || content.length() <= 0)
			return;
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

	/**
	 * 方法作用： 获取文件的行数
	 * 
	 * @return
	 */
	public static int getFileLineNum(String path) {
		if (path == null || path.length() <= 0)
			return -1;

		File test = new File(path);
		long fileLength = test.length();
		LineNumberReader rf = null;
		int lines = 0;
		try {
			rf = new LineNumberReader(new FileReader(test));

			if (rf != null) {
				rf.skip(fileLength);
				lines = rf.getLineNumber();
				rf.close();
			}
			//System.out.println(lines);

		} catch (IOException e) {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ee) {
				}
			}
		}
		return lines;
	}

	/**
	 * 将map里的键值对转化为完整的字符串 string ：value
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String convertMapToString(Map<String, Integer> map) {
		if (map == null || map.size() <= 0)
			return null;
		StringBuilder sb = new StringBuilder();

		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> entry = (Entry<String, Integer>) iterator.next();
			String key = entry.getKey();
			Integer val = entry.getValue();
			String tmp = key + ":" + val + "\n";
			sb.append(tmp); // 拼接字符串，以后一并写入文件中
		}

		return sb.toString();
	}

}
