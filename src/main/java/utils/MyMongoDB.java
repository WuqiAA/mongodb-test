package utils;

import com.mongodb.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 获得指定数据库的集合
 */
public class MyMongoDB {
    private static final Map<String, MongoClient> clients = new HashMap<String, MongoClient>();

    private static final DB mjDb = getDB();
    public static DBCollection mjRoomCollection = mjDb.getCollection(getMjRoomCollection());
    public static DBCollection mjConsoleUsersCollection = mjDb.getCollection(getMjConsoleUsersCollection());
    public static DBCollection mjClubsCollection = mjDb.getCollection(getMjClubsCollection());
    public static DBCollection mjClubWanFaCollection = mjDb.getCollection(getMjClubWanFaCollection());
    public static DBCollection mjPlayersCollection = mjDb.getCollection(getMjPlayersCollection());

    /**
     * 获得数据库
     * 本地、三湘、安庆、内蒙、鄂东、江苏
     *
     * @return
     */
    private static DB getDB() {
        String url = "";
        String dbName = "";

        //本地
//        url = "mongodb://127.0.0.1:27017";
//        dbName = "majiang";

        //三湘
//        url = "mongodb://sanxiang_mj_user:sanxiangmima@dds-bp1c1c1a0a65ae641462-pub.mongodb.rds.aliyuncs.com:3717/mj_sanxiang";
//        dbName = "mj_sanxiang";

        //安庆
//        url = "mongodb://niumowang_mj_user:niumowangmima@dds-bp1c1c1a0a65ae641462-pub.mongodb.rds.aliyuncs.com:3717/mj_niumowang";
//        dbName = "mj_niumowang";

        //内蒙
//        url = "mongodb://nmg_mj_user:nmgpwd@dds-bp1c1c1a0a65ae641462-pub.mongodb.rds.aliyuncs.com:3717/mj_nmg";
//        dbName = "mj_nmg";

//        鄂东
/*        url = "mongodb://qile_mj_user:4Uah2XLC4u7nG6oYOaSK@dds-bp1c1c1a0a65ae641462-pub.mongodb.rds.aliyuncs.com:3717/mj_qile";
//        4Uah2XLC4u7nG6oYOaSK@dds-bp1b87d8c3c39fb42.mongodb.rds.aliyuncs.com:3717
        dbName = "mj_qile";*/

        //江苏
//        url = "mongodb://changzhou_mj_user:4Uah2XLC4u7nG6oYOaSK@dds-bp1c1c1a0a65ae641462dds-bp1c1c1a0a65ae642.mongodb.rds.aliyuncs.com:3717.mongodb.rds.aliyuncs.com:3717/mj_changzhou";
//        dbName = "mj_changzhou";

        //福建游:
//        url = "mongodb://fujianyou_mj_user:aa9a9cc953th5shd1ml2tgzgmq@dds-bp1c1c1a0a65ae641462-pub.mongodb.rds.aliyuncs.com:3717/mj_fujianyou";
////                                                                     @dds-bp1c1c1a0a65ae642.mongodb.rds.aliyuncs.com:3717/mj_fujianyou
//        dbName = "mj_fujianyou";

        //贝贝福建:
        url = "mongodb://fujian_mj_user:fujianmima@dds-bp1c1c1a0a65ae641462-pub.mongodb.rds.aliyuncs.com:3717/mj_fujian";
        dbName = "mj_fujian";

        return getDB(url, dbName);
    }

    private static String getMjRoomCollection() {
        return "mj_rooms";
    }

    private static String getMjPlayersCollection() {
        return "mj_players";
    }

    private static String getMjConsoleUsersCollection() {
        return "mj_console_users";
    }

    private static String getMjClubsCollection() {
        return "mj_clubs";
    }

    private static String getMjClubWanFaCollection() {
        return "mj_club_wanfa";
    }

    public static DB getDB(String url, String dbName) {
        MongoClient client = initClient(url);
        return client.getDB(dbName);
    }

    private static synchronized MongoClient initClient(String url) {
        MongoClient mongoClient = clients.get(url);
        if (mongoClient != null) {
            return mongoClient;
        }
        try {
            mongoClient = new MongoClient(new MongoClientURI(url));
            mongoClient.setReadPreference(ReadPreference.secondaryPreferred());
            clients.put(url, mongoClient);
        } catch (Exception e) {
            System.out.println("unknown host for monogo db:" + e);
        }
        return mongoClient;
    }

}
