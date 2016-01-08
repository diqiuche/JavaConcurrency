package cn.edu.nju.poem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**     
 * 类名称：Producer    
 * 类描述：    
 *     
 */
public class Producer implements Runnable {
	private BoundedBuffer buf;
	public static String filePath = "f:\\项目1-全唐诗.txt";
	public static int lineNum = 353016 + 100000; //文件行数
	private static String h1 = null; //卷，编号
	private static String h2 = null; // 篇名
	private static String h3 = null; //作者

	public Producer(BoundedBuffer buf) {
		this.buf = buf;
	}

	/**    
	 * 方法作用：  生产者处理文件，读取加入缓冲区
	 * @return      
	*/
	@SuppressWarnings("resource")
	public void dealFile() throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		int lineIndex = 0;

		while (lineIndex <= lineNum) {
			while (lineIndex < 200) { //略过前面200行
				br.readLine();
				lineIndex++;
			}
			String str = br.readLine(); //逐行读
			if (str != null) {
				if (str.contains("卷") && str.contains("【")) {
					str = str.trim();
					int centPre = str.indexOf("【");
					int centSuf = str.lastIndexOf("】");
					h1 = str.substring(0, centPre).trim();
					if (centPre <= centSuf && centSuf + 1 <= str.length()) {
						h2 = str.substring(centPre, centSuf + 1);
						h3 = str.substring(centSuf + 1).trim();
					} else {
						h2 = str.substring(centPre, str.length());
						h3 = "";
					}

				}
				if (str.contains("，")
						|| (str.contains("。") || str.contains("！") || str
								.contains("？"))) {
					StringBuilder content = new StringBuilder(str.trim()); //诗的正文

					String tmp = br.readLine();
					while (tmp != null && tmp.trim().length() > 0) {
						content.append(tmp.trim());
						tmp = br.readLine();
						lineIndex++;
					}
					/*****判断是放入list还是开始写文件，1000条写一次*****/

					OnePoem onePoem = new OnePoem(h1, h2, h3,
							content.toString());
					buf.addOnePoemToBuf(onePoem); //加入到缓冲区

					System.out.println(lineIndex);

				}

			}

			lineIndex++;
		}
		buf.setExit(true);
		System.out.println("=========================");
	}

	@Override
	public void run() {
		try {
			this.dealFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
