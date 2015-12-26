package cn.edu.nju.parsems;

import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**     
 * 类名称：App    
 * 类描述：    写文件
 *     
 */
public class QuotationWriteToFiles {
	public static void main(String[] args) throws Exception {
		MongoDBProcess dbHandle = new MongoDBProcess();
		dbHandle.setCollectionStr("qt_parse"); //quotation parse
		dbHandle.connectMongoDB();

		//dbHandle.deleteMongoDatas(new Document());  //删除数据库中的所有数据

		MongoDatabase db = dbHandle.getDB("DB_MY_MONGO");

		StringBuilder jsonStr = new StringBuilder();
		FindIterable<Document> iterable3 = db.getCollection("qt_parse").find(
				new Document("id", new Document("$gt", 0)));
		MongoCursor<Document> cursor = iterable3.iterator();

		int count = 0;
		while (cursor.hasNext()) {
			jsonStr.append(cursor.next().toJson());

			Map<String, String> oneMessage = Utils.toMap(jsonStr.toString());
			for (Map.Entry<String, String> entry : oneMessage.entrySet()) {

				if (entry.getKey().equals("典面")) {
					Utils.writeFile("F:\\dianmian-9.txt", entry.getValue());
					Utils.writeFile("F:\\dianmian-9.txt",
							"\n==========================\n");
				} else if (entry.getKey().equals("典源")) {
					Utils.writeFile("F:\\dianyuan-9.txt", entry.getValue());
					Utils.writeFile("F:\\dianyuan-9.txt",
							"\n==========================\n");
				} else if (entry.getKey().equals("用例")) {
					Utils.writeFile("F:\\yongli-9.txt", entry.getValue());
					Utils.writeFile("F:\\yongli-9.txt",
							"\n==========================\n");
				} else {

				}
			}
			jsonStr = new StringBuilder("");
			System.out.println(count++);
		}
		System.out.println(count);

		System.out.println("-----------------end-------------------");
	}

}

// 更新数据
//		dbHandle.updateMongoDatas(new Document("address.zipcode", "10075"),
//				new Document("$set", new Document("address.zipcode", "88888")));
//		System.out.println();

//		dbHandle.selectMongoDatas(new Document("cuisine", "Italian"));

/** 删除数据 **/
//				dbHandle.deleteMongoDatas(new Document("id", 1));
//		mongoDBTest.deleteMongoDatas(new Document("address.zipcode","88888"));
//		System.out.println();
//		mongoDBTest.selectMongoDatas(new Document());
