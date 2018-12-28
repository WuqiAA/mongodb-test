package utils;


/**
 * 使用指南：
 * 1，修改MyMongoDb中数据库
 * 2，通过MongoDbUtils进行相关查询
 */
public class Main {
    public static void main(String[] args) {
//        MongoDbUtils.statisticsLastNDayAllClubHaoKaAndChangCi(7);
//        MongoDbUtils.findLastNDaysChangCi(2);
//        MongoDbUtils.staticAllClubHaoKaAndChangCi(1545235200,1545321600);
//        System.out.println("============================================");
//        MongoDbUtils.statisticsChangCiAndActivePlayer(1545235200,1545321600);

        System.out.println("总耗卡："+MongoDbUtils.staticAllHaoKaAndChangCi(1545580800, 1545667200));
        System.out.println("新增用户："+MongoDbUtils.staticNewPlayerCount(1545580800, 1545667200));
        System.out.println("活跃用户："+MongoDbUtils.staticActivePlayerCnt(1545580800, 1545667200));
        System.out.println("最高在线："+MongoDbUtils.staticMaxOnlineCount(1545580800, 1545667200));
        System.out.println("交易卡数："+MongoDbUtils.getAgentSellCardsCount(1543593600));
    }
}
