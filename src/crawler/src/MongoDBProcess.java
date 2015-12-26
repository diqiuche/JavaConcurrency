package cn.edu.nju.parsems;

import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 类名称：MongoDB
 *  类描述：

 */
public class MongoDBProcess {

	private String DBString = "DB_MY_MONGO";// 数据库名
	private String hostName = "localhost";// 主机名
	private int port = 27017;// 端口号

	private MongoClient mongoClient; // mongo客户端
	private MongoDatabase mongoDatabase; // mongo数据库
	private MongoCollection<Document> collection; // 集合
	private String collectionStr; // 集合字符串

	public MongoDBProcess() {
		mongoClient = new MongoClient(hostName, port);
	}

	/**    
	 * 方法作用：  通过传入字符串指定database来获取对应的  mongodatabase
	 * @return      
	*/
	public MongoDatabase getDB(String s) {
		mongoDatabase = mongoClient.getDatabase(s);
		return mongoDatabase;
	}

	/**
	 * 方法作用：连接mongodb数据库
	 */
	public void connectMongoDB() {
		mongoDatabase = mongoClient.getDatabase(DBString);
		collection = mongoDatabase.getCollection(collectionStr); //获取collection

	}

	/**
	 * 方法作用：插入数据
	 * 
	 * @return
	 */
	public void insertMongoData(Document document) {
		if (document != null) {
			collection.insertOne(document);
		}
	}

	/**
	 * 方法作用：插入数据 list 集合
	 * @return
	 */
	public void insertMongoData(List<Document> documents) {
		if (documents != null) {
			collection.insertMany(documents);
		}
	}

	/**
	 * 方法作用：删除集合里的数据，如果document为   new Document() 则会清空里面的所有数据
	 * @return
	 */
	public void deleteMongoDatas(Document document) {
		collection.deleteMany(document);
	}

	/**
	 * 方法作用：
	 * 
	 * @return
	 */
	public void updateMongoDatas(Document document1, Document document2) {
		collection.updateOne(document1, document2);
	}

	/**
	 * 方法作用：查询数据，遍历
	 * @return
	 */
	public void selectMongoDatas(Document document) {
		FindIterable<Document> iterable = collection.find(document);
		iterable.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});

	}

	/***************************************/

	public MongoCollection<Document> getCollection(String dbName,
			String collectionName) {
		return collection;
	}

	public void setCollection(MongoCollection<Document> collection) {
		this.collection = collection;
	}

	public String getCollectionStr() {
		return collectionStr;
	}

	public void setCollectionStr(String collectionStr) {
		this.collectionStr = collectionStr;
	}

	public String getDBString() {
		return DBString;
	}

	public void setDBString(String dBString) {
		DBString = dBString;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public MongoDatabase getMongoDatabase() {
		return mongoDatabase;
	}

	public void setMongoDatabase(MongoDatabase mongoDatabase) {
		this.mongoDatabase = mongoDatabase;
	}

	public MongoCollection<Document> getCollection() {
		return collection;
	}

}
