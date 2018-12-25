package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间的工具类
 */
public class TimeUtils {
    /**
     * 获得今天的时间戳
     * 昨天的是-86400
     *
     * @return
     */
    public static long todayBeginTimeStamp() {
        Calendar todayBegin = Calendar.getInstance();
        todayBegin.set(Calendar.HOUR_OF_DAY, 0);
        todayBegin.set(Calendar.MINUTE, 0);
        todayBegin.set(Calendar.SECOND, 0);
        todayBegin.set(Calendar.MILLISECOND, 0);
        return todayBegin.getTimeInMillis() / 1000;
    }

    /**
     * 获得之前n天开始的时间戳
     *
     * @param n
     * @return
     */
    public static long lastNDayBeginTimeStamp(int n) {
        return todayBeginTimeStamp() - 86400 * n;
    }

    /**
     * 通过时间戳获得年月日形式
     *
     * @param timeStamp
     * @return
     */
    public static String formatTimeStampToNianYueRi(long timeStamp) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy.MM.dd");
        return s.format(timeStamp * 1000);
    }

}
