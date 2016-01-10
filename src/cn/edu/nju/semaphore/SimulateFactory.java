package cn.edu.nju.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

import cn.edu.nju.pg.HandlePostgreSQL;

/**     
 * 类名称：SimulateFactory    
 * 类描述：    
 *     
 */
public class SimulateFactory {

	/**    
	 * 方法作用：  
	 * @return      
	*/
	public static void main(String[] args) {
		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(25);
		HandlePostgreSQL pgSql = new HandlePostgreSQL();
		Semaphore shareSem = new Semaphore(1);
		for (int i = 1; i <= 20; i++) {
			threadPool.execute(new Client(i, pgSql, shareSem)); //放入线程池执行
		}

		threadPool.shutdown();
	}

}
