package cn.edu.nju.concurrency;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import cn.edu.nju.pg.HandlePostgreSQL;

/**     
 * 类名称：Client    
 * 类描述：    
 *     
 */
public class Client implements Runnable {
	private final int id;
	private HandlePostgreSQL pgHandle;
	private Semaphore semaphore;

	public Client(int i, HandlePostgreSQL pg, Semaphore semaphore) {
		this.id = i;
		this.pgHandle = pg;
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		try {
			semaphore.acquire();
			String queryStr = "select p_version from tb_product where p_id=1 ;";
			pgHandle.selectDatas(queryStr, this.id, "修改前"); //查询出数据
			TimeUnit.MICROSECONDS.sleep(2000);
			String updateStr = "update tb_product set p_version=p_version+1 where p_id=1";
			pgHandle.updateData(updateStr);
			pgHandle.selectDatas(queryStr, this.id, "修改后");
			semaphore.release();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
