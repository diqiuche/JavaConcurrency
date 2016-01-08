package cn.edu.nju.poem;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.edu.nju.parsems.Utils;

/**     
 * 类名称：BoundedBuffer    
 * 类描述：    
 */
public class BoundedBuffer {

	private final Lock lock = new ReentrantLock();
	private final Condition producerd = lock.newCondition();
	private final Condition consumerd = lock.newCondition();

	private List<OnePoem> poems = new CopyOnWriteArrayList<OnePoem>();

	private String newLine = System.getProperty("line.separator");//换行

	private volatile boolean isExit = false;

	/**    
	 * 方法作用：  
	 * @return      
	 * @throws InterruptedException 
	*/
	public void addOnePoemToBuf(OnePoem poem) {
		if (poem == null)
			return;
		lock.lock();
		try {
			int size = poems.size();
			if (size == 1000) { //如果已经达到1000，则生产者停止等待
				producerd.await(); //挂起自己，生产者
				consumerd.signal(); //唤醒消费者
			} else {
				poems.add(poem);

			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			lock.unlock();
		}
	}

	/**    
	 * 方法作用：  把list写到文件中
	 * @return      
	 * @throws InterruptedException 
	*/
	public void writeBufToFile() throws IOException, InterruptedException {
		lock.lock();
		try {
			int size = poems.size();
			while (size >= 0 && size < 1000) {
				//写文件阻塞，等到1000再写
				size = poems.size();
				consumerd.await();
			}

			if (size == 1000) {
				String content1000 = this.concatPoem();
				Utils.writeFile("f:/tangshi-9.txt", content1000);
				this.clear(); //清空
				producerd.signal(); //唤醒放入诗歌的线程

			}
		} finally {
			lock.unlock();
		}
	}

	/**    
	 * 方法作用：  拼接1000首诗歌
	 * @return      
	*/
	public String concatPoem() {
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < poems.size(); i++) {
			OnePoem poem = poems.get(i);
			sb.append(poem.getH1() + newLine);
			sb.append(poem.getH2() + newLine);
			sb.append(poem.getH3() + newLine);
			sb.append(poem.getContent() + newLine + newLine);
		}
		return sb.toString();
	}

	public void clear() {
		this.poems.clear();
	}

	public synchronized boolean getIsExit() {
		return isExit;
	}

	public synchronized void setExit(boolean isExit) {
		this.isExit = isExit;
	}

}
