package cn.edu.nju.poem;

import java.io.IOException;

/**     
 * 类名称：Consumer    
 * 类描述：    
 *     
 */
public class Consumer implements Runnable {

	private BoundedBuffer buf;

	public Consumer(BoundedBuffer buf) {
		this.buf = buf;
	}

	@Override
	public void run() {
		try {
			while (buf.getIsExit()==false) { //不断判断文件有没有结束
				buf.writeBufToFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
