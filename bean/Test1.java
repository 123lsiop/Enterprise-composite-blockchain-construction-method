package bean;


import java.util.ArrayList;
import java.util.List;
 
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
 
 
public class Test1 {
 
    public static void main(String[] args) {       
        try{  
               // 连接到 mongodb 服务
               MongoClient mongoClient = new MongoClient( "192.168.130.128" , 27017 );
              
               // 连接到数据库
               MongoDatabase mongoDatabase = mongoClient.getDatabase("LNU"); 
               System.out.println("Connect to database successfully");
                
               // 相当于书记库里面的表，创建
//             mongoDatabase.createCollection("test");
//             System.out.println("集合创建成功");
                
               // 创建之后连接表，然后才能进行数据操作
               MongoCollection<Document> collection = mongoDatabase.getCollection("Test");
               System.out.println("集合 test 选择成功"+collection);
               
               //中间稍微调试一下下
               
//               Bson where = Filters.and(Filters.gt("encode", 20), Filters.eq("title", "标题"));
               //Bson where = Filters.and(Filters.gt("age", "20"),Filters.lt("age", "26"), Filters.eq("name", "xiaoguantou"));
               //Bson where = Filters.and(Filters.gt("time", "2010-10-10"),Filters.lt("time", "2020-02-20"));
               Bson where = Filters.and(Filters.gt("age", "14"),Filters.eq("name", "ok"));
               FindIterable<Document> d = collection.find(where);
               for (Document doc : d) {
//                   System.out.println(doc.get("_id") + "   " + doc.get("title") + "   " + doc.get("encode"));
            	   System.out.println("集合 test 选择成功"+collection);
                   System.out.println(doc.get("_id") + "   " + doc.get("name") + "   " + doc.get("age"));
                 	String filename=doc.get("name").toString();
                    String download_url="http://"+"ip"+":50075/webhdfs/v1/block-chain/"+filename+".json?op=OPEN&namenoderpcaddress="+"ip"+":9000&offset=0";
                    System.out.println(filename+download_url);
               }
               /*
               
               FindIterable<Document> d = collection.find(Filters.lt("age", "29"));
               for (Document doc : d) {
//                 System.out.println(doc.get("_id") + "   " + doc.get("title") + "   " + doc.get("encode"));
                 System.out.println(doc.get("_id") + "   " + doc.get("name") + "   " + doc.get("age"));
             }
             */
             }catch(Exception e)
             {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
             }
    }
}