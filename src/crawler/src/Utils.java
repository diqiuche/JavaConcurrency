package cn.edu.nju.parsems;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**     
 * 类名称：Tools    
 * 类描述：    去除HTML标签
 *     
 */
public class Utils {

	private static String pt = "<.*?>\\s*";

	/**    
	 * 方法作用：  
	 * @return      
	*/
	public static String replace(String doc) {
		if (doc == null || doc.length() <= 0)
			return null;

		return doc.replaceAll(pt, "");
	}

	/**    
	 * 方法作用：  
	 * @return      
	*/
	public static String printQuotationType(int i) {
		if (i < 0 || i >= 3)
			return null;
		if (i == 0) {
			return "典面";
		} else if (i == 1) {
			return "典源";
		} else {
			return "用例";
		}

	}

	/**
	 * 将Json对象转换成Map
	 * @return Map对象
	 * @throws JSONException
	 */
	public static Map<String, String> toMap(String jsonString)
			throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);
		Map<String, String> result = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = jsonObject.keys();

		String key = null;
		String value = null;

		while (iterator.hasNext()) {
			key = iterator.next();
			value = jsonObject.getString(key);
			result.put(key, value);
		}
		return result;
	}

	/**    
	 * 方法作用：  将内容写入txt文件
	 * @return      
	 * @throws IOException 
	*/
	public static void writeFile(String path, String content)
			throws IOException {
		@SuppressWarnings("resource")
		FileOutputStream fos = new FileOutputStream(path, true); //内容追加到文件最后
		FileChannel fc = fos.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(1024 * 1024 * 1024);
		byte[] byteMessage = content.getBytes();
		buf.put(byteMessage);
		buf.flip();
		fc.write(buf);

	}

}
