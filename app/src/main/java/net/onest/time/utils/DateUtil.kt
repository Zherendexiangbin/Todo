package net.onest.time.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

class DateUtil {
    private val currentDateTime: LocalDateTime = LocalDateTime.now()

    // 从今天起 往上推一天
    private val yesterdays: LocalDateTime = currentDateTime.minusDays(1)

    // 从今天起 往上推一周
    private val weeks: LocalDateTime = currentDateTime.minusWeeks(1).plusDays(1)

    // 从今天起 往上推一个月
    private val months: LocalDateTime = currentDateTime.minusMonths(1).plusDays(1)

    var todayEpochMill: Long =
        currentDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000
    var yesterdayEpochMilli: Long =
        yesterdays.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000
    var weekEpochMilli: Long =
        weeks.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000
    var monthEpochMilli: Long =
        months.toInstant(ZoneOffset.of("+8")).toEpochMilli() + 8 * 60 * 60 * 1000


    companion object {
        val curDay: String
            //获取当前日期
            get() {
                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = dateFormat.format(calendar.time)
                return currentDate
            }

        val curWeek: String
            //获取当前一周的日期
            get() {
                val calendarWeek = Calendar.getInstance()
                val dateFormatWeek = SimpleDateFormat("yyyy-MM-dd")

                // 设置一周的第一天为周一
                calendarWeek[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
                val startDate = dateFormatWeek.format(calendarWeek.time)

                // 获取本周的最后一天（星期日）
                calendarWeek.add(Calendar.DATE, 6)

                // 判断是否已经是月末，如果是，则将结束日期设置为当月最后一天
                if (calendarWeek[Calendar.DAY_OF_MONTH] == calendarWeek.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    calendarWeek[Calendar.DAY_OF_MONTH] =
                        calendarWeek.getActualMaximum(Calendar.DAY_OF_MONTH)
                } else {
                    // 如果不是月末，则继续获取本周的结束日期
                    calendarWeek[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
                }

                val endDate = dateFormatWeek.format(calendarWeek.time)
                return "$startDate ~ $endDate"
            }

        val curMonth: String
            //获取当前一月的日期
            get() {
                val calendarMonth = Calendar.getInstance()
                val dateFormatMonth = SimpleDateFormat("yyyy-MM-dd")
                // 获取当月的第一天
                calendarMonth[Calendar.DAY_OF_MONTH] = 1
                val startDateMonth = dateFormatMonth.format(calendarMonth.time)
                // 获取当月的最后一天
                calendarMonth[Calendar.DAY_OF_MONTH] =
                    calendarMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
                val endDateMonth = dateFormatMonth.format(calendarMonth.time)

                return "$startDateMonth ~ $endDateMonth"
            }

        /**
         * 获得指定日期的 EpochMillisecond
         */
        @JvmStatic
        fun epochMillisecond(date: Date = Date()): Long {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar.toInstant().epochSecond * 1000
            //        LocalDate localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        return 1000 * (localDate.toEpochDay() * 60 * 60 * 24 - 8 * 60 * 60);
        } //    public enum Unit {
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
}
