package net.onest.time.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    //获取当前日期
    public static String getCurDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(calendar.getTime());
        return currentDate;
    }

    //获取当前一周的日期
    public static String getCurWeek() {
        Calendar calendarWeek = Calendar.getInstance();
        SimpleDateFormat dateFormatWeek = new SimpleDateFormat("yyyy-MM-dd");

        // 设置一周的第一天为周一
        calendarWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = dateFormatWeek.format(calendarWeek.getTime());

        // 获取本周的最后一天（星期日）
        calendarWeek.add(Calendar.DATE, 6);

        // 判断是否已经是月末，如果是，则将结束日期设置为当月最后一天
        if (calendarWeek.get(Calendar.DAY_OF_MONTH) == calendarWeek.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendarWeek.set(Calendar.DAY_OF_MONTH, calendarWeek.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else {
            // 如果不是月末，则继续获取本周的结束日期
            calendarWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        }

        String endDate = dateFormatWeek.format(calendarWeek.getTime());
        return startDate + " ~ " + endDate;
    }

    //获取当前一月的日期
    public static String getCurMonth() {
        Calendar calendarMonth = Calendar.getInstance();
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当月的第一天
        calendarMonth.set(Calendar.DAY_OF_MONTH, 1);
        String startDateMonth = dateFormatMonth.format(calendarMonth.getTime());
        // 获取当月的最后一天
        calendarMonth.set(Calendar.DAY_OF_MONTH, calendarMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDateMonth = dateFormatMonth.format(calendarMonth.getTime());

        return startDateMonth + " ~ " + endDateMonth;
    }

    /**
     * 获得指定日期的 EpochSecond
     */
    public static Long epochMillisecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.toInstant().getEpochSecond() * 1000;
//        LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        return 1000 * (localDate.toEpochDay() * 60 * 60 * 24 - 8 * 60 * 60);
    }

//    public enum Unit {
//        DAY(1), WEEK(7), MONTH(30), YEAR(365);
//
//        private final Integer days;
//
//        Unit(Integer days) {
//            this.days = days;
//        }
//
//        public long getStartEpochSecond() {
//            Calendar calendar = Calendar.getInstance();
//            switch (this) {
//                case DAY:
//                    calendar.set(Calendar.HOUR_OF_DAY, 0);
//                    calendar.set(Calendar.MINUTE, 0);
//                    calendar.set(Calendar.SECOND, 0);
//                    calendar.set(Calendar.MILLISECOND, 0);
//                    return calendar.toInstant().getEpochSecond();
//                case WEEK:
//                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//                    calendar.set(Calendar.HOUR_OF_DAY, 0);
//                    calendar.set(Calendar.MINUTE, 0);
//                    calendar.set(Calendar.SECOND, 0);
//                    return calendar.getTime().toInstant().getEpochSecond();
//                case MONTH:
//                    calendar.set(calendar.get(Calendar.YEAR),
//                            calendar.get(Calendar.MONTH),
//                            calendar.get(Calendar.DAY_OF_MONTH),
//                            0, 0, 0);
//                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//                    return calendar.getTime().toInstant().getEpochSecond();
//                case YEAR:
//            }
//            return 0;
//        }
//
//        public long getEndEpochSecond() {
//            return LocalDate.now(ZoneId.of("UTC+8")).(LocalTime.MIN, ZoneOffset.of("+8"));
//        }
//
//        public Integer getDays() {
//            return days;
//        }
//    }

//点击增加时:
    //获取下一日的日期:

    //获取下一周的日期:
    //获取下一月的日期:
//点击减少时:
    //获取上一日的日期:
    //获取上一周的日期:
    //获取上一月的日期:

}
