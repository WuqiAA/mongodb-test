package utils;

import com.mongodb.*;

import java.util.*;

public class MongoDbUtils {

    /**
     * 通过clubId，找指定时间的场次
     *
     * @param clubId
     * @param begin
     * @param end
     * @return
     */
    public static int findChangCiByClubId(int clubId, long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        query.put("club_id", clubId);
        return MyMongoDB.mjRoomCollection.find(query).size();
    }

    /**
     * 获得指定MapId的场次数量
     *
     * @param mapId
     * @param begin
     * @param end
     * @return
     */
    public static int findChangCiByMapId(int mapId, long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        query.put("map_id", mapId);
        return MyMongoDB.mjRoomCollection.find(query).size();
    }

    /**
     * 获得指定club_wanfa_id的场次
     *
     * @param clubWanFaId
     * @param begin
     * @param end
     * @return
     */
    public static int findChangCiByClubWanFaId(int clubWanFaId, long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        query.put("club_wanfa_id", clubWanFaId);
        return MyMongoDB.mjRoomCollection.find(query).size();
    }

    /**
     * 获得场次
     *
     * @param begin
     * @param end
     * @return
     */
    public static int findChangCi(long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        return MyMongoDB.mjRoomCollection.find(query).size();
    }

    /**
     * 获得之前n天的场次
     *
     * @param n
     */
    public static void findLastNDaysChangCi(int n) {
        long lastDayEnd = TimeUtils.todayBeginTimeStamp();
        long lastDayBegin = lastDayEnd - 86400;
        for (int i = 0; i < n; i++) {
            int changci = findChangCi(lastDayBegin, lastDayEnd);
            System.out.println(TimeUtils.formatTimeStampToNianYueRi(lastDayBegin) + ":" + changci);
            lastDayBegin -= 86400;
            lastDayEnd -= 86400;
        }
    }

    /**
     * 根据club_id，获得之前n天的场次
     *
     * @param clubId
     * @param n
     */
    public static void findLastNDaysChangCiByClubId(int clubId, int n) {
        long lastDayEnd = TimeUtils.todayBeginTimeStamp();
        long lastDayBegin = lastDayEnd - 86400;
        for (int i = 0; i < n; i++) {
            int changci = findChangCiByClubId(clubId, lastDayBegin, lastDayEnd);
            System.out.println(TimeUtils.formatTimeStampToNianYueRi(lastDayBegin) + ":" + changci);
            lastDayBegin -= 86400;
            lastDayEnd -= 86400;
        }
    }


    /**
     * 获得之前n天的场次
     *
     * @param mapId
     * @param n
     */
    public static void findLastNDaysChangCiByMapId(int mapId, int n) {
        long lastDayEnd = TimeUtils.todayBeginTimeStamp();
        long lastDayBegin = lastDayEnd - 86400;
        for (int i = 0; i < n; i++) {
            int changci = findChangCiByMapId(mapId, lastDayBegin, lastDayEnd);
            System.out.println(TimeUtils.formatTimeStampToNianYueRi(lastDayBegin) + ":" + changci);
            lastDayBegin -= 86400;
            lastDayEnd -= 86400;
        }
    }

    /**
     * 分mapId统计指定时间的场次和活跃用户
     *
     * @param begin
     * @param end
     */
    public static void statisticsChangCiAndActivePlayer(long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        DBCursor mjRooms = MyMongoDB.mjRoomCollection.find(query);

        Map<Integer, Integer> changCiMap = new HashMap<Integer, Integer>();
        Map<Integer, Set<Integer>> activePlayers = new HashMap<Integer, Set<Integer>>();
        Set<Integer> allActivePlayers = new HashSet<Integer>();

        List<DBObject> mjRoomsList = mjRooms.toArray();
        for (DBObject mjRoom : mjRoomsList) {
            if (mjRoom.containsField("map_id")) {
                int mapId = (Integer) mjRoom.get("map_id");
                if (changCiMap.containsKey(mapId)) {
                    changCiMap.put(mapId, changCiMap.get(mapId) + 1);
                } else {
                    changCiMap.put(mapId, 1);
                }
                Set<Integer> players;
                if (activePlayers.containsKey(mapId)) {
                    players = activePlayers.get(mapId);
                } else {
                    players = new HashSet<Integer>();
                    activePlayers.put(mapId, players);
                }

                if (mjRoom.containsField("total_result")) { //结果
                    BasicDBList totalResults = (BasicDBList) mjRoom.get("total_result");
                    for (int i = 0; i < totalResults.size(); i++) {
                        DBObject oneScore = (DBObject) totalResults.get(i);
                        if (oneScore.containsField("uid")) {
                            int uid = (Integer) oneScore.get("uid");
                            players.add(uid);
                            allActivePlayers.add(uid);
                        }
                    }
                }
            }
        }
        System.out.println("*******  map_id  :  changci  *******");
        if (changCiMap.size() != 0) {
            Set<Map.Entry<Integer, Integer>> set = changCiMap.entrySet();
            for (Map.Entry<Integer, Integer> entry : set) {
                System.out.println(entry.getKey() + "," + entry.getValue());
            }
        }
        System.out.println("*******  map_id  :  active players  *******");
        if (activePlayers.size() != 0) {
            Set<Map.Entry<Integer, Set<Integer>>> set = activePlayers.entrySet();
            for (Map.Entry<Integer, Set<Integer>> entry : set) {
                System.out.println(entry.getKey() + "," + entry.getValue().size());
            }
        }
        System.out.println("all active player : " + allActivePlayers.size());
        System.out.println("******************************************************");
    }

    public static void statisticsLastNDayActivePlayerAndChangci(int n) {
        long lastDayEnd = TimeUtils.todayBeginTimeStamp();
        long lastDayBegin = lastDayEnd - 86400;
        for (int i = 0; i < n; i++) {
            System.out.println(TimeUtils.formatTimeStampToNianYueRi(lastDayBegin));
            statisticsChangCiAndActivePlayer(lastDayBegin, lastDayEnd);
            lastDayBegin -= 86400;
            lastDayEnd -= 86400;
            System.out.println();
            System.out.println();
        }
    }

    public static void statisticsLastNDayAllClubHaoKaAndChangCi(int n) {
        long lastDayEnd = TimeUtils.todayBeginTimeStamp();
        long lastDayBegin = lastDayEnd - 86400;
        for (int i = 0; i < n; i++) {
            System.out.println(TimeUtils.formatTimeStampToNianYueRi(lastDayBegin));
            staticAllClubHaoKaAndChangCi(lastDayBegin, lastDayEnd);
            lastDayBegin -= 86400;
            lastDayEnd -= 86400;
            System.out.println();
            System.out.println();
        }
    }

    /***
     * 所有俱乐部耗卡和场次
     * @param begin
     * @param end
     */
    public static void staticAllClubHaoKaAndChangCi(long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        DBCursor mjRooms = MyMongoDB.mjRoomCollection.find(query);

        Map<Integer, Integer> clubId2CardUsed = new HashMap<Integer, Integer>();
        Map<Integer, Integer> clubId2ChangCi = new HashMap<Integer, Integer>();
        Map<Integer, Integer> clubId2AgentId = new HashMap<Integer, Integer>();
        ArrayList<Integer> clubIds = new ArrayList<Integer>();

        for (DBObject mjRoom : mjRooms) {
            if (mjRoom.containsField("club_id")) {
                int clubId = (Integer) mjRoom.get("club_id");
                int cardUsed = (Integer) mjRoom.get("cardused");
                if (clubId2CardUsed.containsKey(clubId)) {
                    clubId2CardUsed.put(clubId, clubId2CardUsed.get(clubId) + cardUsed);
                    clubId2ChangCi.put(clubId, clubId2ChangCi.get(clubId) + 1);
                } else {
                    clubIds.add(clubId);
                    clubId2CardUsed.put(clubId, cardUsed);
                    clubId2ChangCi.put(clubId, 1);
                    int agentId = (Integer) mjRoom.get("owner_uid");
                    clubId2AgentId.put(clubId, agentId / 10000);
                }
            }
        }

        for (int clubId : clubIds) {
            System.out.println("club_id:" + clubId + ",agent_id:" + clubId2AgentId.get(clubId) + ",cardused/changci:" +
                    clubId2CardUsed.get(clubId) + "/" + clubId2ChangCi.get(clubId));
        }
        System.out.println("**************************************");

    }

    /***
     * 所有耗卡和场次
     * @param begin
     * @param end
     * @return
     */
    public static int staticAllHaoKaAndChangCi(long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        DBCursor mjRooms = MyMongoDB.mjRoomCollection.find(query);
        int cards = 0;
        int changci = 0;
        for (DBObject mjRoom : mjRooms) {
            if (mjRoom.containsField("cardused")) {
                if ((Integer) mjRoom.get("cardused") > 0) {
                    changci += 1;
                    cards +=(Integer) mjRoom.get("cardused");
                }
            }
        }
        System.out.println("场次："+changci);
        return cards;
    }

    /***
     * 统计同时在线人数
     * @param begin
     * @param end
     */
    public static int static5OnlineCount(long begin, long end) {
        int count = 0;
        int card = 0;
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        /*DBObject dbObjectCard = new BasicDBObject();
        dbObject.put("$gt", 0);*/

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
//        query.put("cardused",dbObjectCard);
        DBCursor mjRooms = MyMongoDB.mjRoomCollection.find(query);
        for (DBObject mjRoom : mjRooms) {
            if (mjRoom.containsField("cardused")) {
                if ((Integer) mjRoom.get("cardused") > 0) {
                    card = (Integer) mjRoom.get("cardused");
                    if (mjRoom.containsField("uid1")) {
                        int uid = (Integer) mjRoom.get("uid1");
                        if (uid > 0) {
                            count++;
                        }
                    }
                    if (mjRoom.containsField("uid2")) {
                        int uid = (Integer) mjRoom.get("uid2");
                        if (uid > 0) {
                            count++;
                        }
                    }
                    if (mjRoom.containsField("uid3")) {
                        int uid = (Integer) mjRoom.get("uid3");
                        if (uid > 0) {
                            count++;
                        }
                    }
                    if (mjRoom.containsField("uid4")) {
                        int uid = (Integer) mjRoom.get("uid4");
                        if (uid > 0) {
                            count++;
                        }
                    }
                }
            }
        }
//        System.out.println("crete_time:"+begin+"  (5分钟内)    人数:"+count+"     耗卡："+card);
        return count;

    }

    /***
     * 统计同时最大在线人数  统计粒度5分钟
     * @param begin
     * @param end
     */
    public static int staticMaxOnlineCount(long begin, long end) {
        int maxOnlineCount = 0;
        long beginFind = begin;
        while (beginFind < end) {
            int count = static5OnlineCount(beginFind, beginFind+300);
            if (count > maxOnlineCount) {
                maxOnlineCount = count;
            }
            beginFind +=300;
        }
        return maxOnlineCount;
    }

    /**
     * 新增用户量
     * @param begin
     * @param end
     * @return
     */
    public static int staticNewPlayerCount(long begin, long end) {
        int newPlayerCount = 0;
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);
        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        DBCursor mjRooms = MyMongoDB.mjPlayersCollection.find(query);

        return mjRooms.size();
    }

    /**
     * 固定时间活跃用户量
     * @param begin
     * @param end
     * @return
     */
    public static int staticActivePlayerCnt(long begin, long end) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("$gt", begin);
        dbObject.put("$lt", end);

        DBObject query = new BasicDBObject();
        query.put("create_time", dbObject);
        DBCursor mjRooms = MyMongoDB.mjRoomCollection.find(query);
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (DBObject mjRoom : mjRooms) {
            int uid1 = (Integer)mjRoom.get("uid1");
            int uid2 = (Integer)mjRoom.get("uid2");
            int uid3 = (Integer)mjRoom.get("uid3");
            int uid4 = (Integer)mjRoom.get("uid4");
            if (uid1 > 0 && !list.contains(uid1)) {
                list.add(uid1);
            }
            if (uid2 > 0 && !list.contains(uid2)) {
                list.add(uid2);
            }
            if (uid3 > 0 && !list.contains(uid3)) {
                list.add(uid3);
            }
            if (uid4 > 0 && !list.contains(uid4)) {
                list.add(uid4);
            }
        }
        return list.size();
    }
}
