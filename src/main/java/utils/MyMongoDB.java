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
    public static DBCollection mjConsoleSellHistory = mjDb.getCollection(mjConsoleSellHistoryCollection());

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
//        url = "";



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

    private static String mjConsoleSellHistoryCollection() {
        return "mj_console_sell_history";
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
