package cn.edu.nju.poem;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**     
 * 类名称：Poem_Main    
 * 类描述：    
 *     
 */
public class Poem_Main {

	/**    
	 * 方法作用：  
	 * @return      
	*/
	public static void main(String[] args) {
		BoundedBuffer buf = new BoundedBuffer();
		Producer readFile = new Producer(buf);
		Consumer writeFile = new Consumer(buf);

		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(4);
		threadPool.execute(readFile);
		threadPool.execute(writeFile);

		//threadPool.shutdown();

	}

}
