package ru.tsystems.javaschool.kuzmenkov.logiweb.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Set of utils to help with dates.
 * 
 * @author Nikolay Kuzmenkov
 */
public class DateUtil {

    private DateUtil() {

    }
    
    /**
     * Calculate difference in hours between two Date objects.
     * Will return negative if dates not in chronological order.
     * 
     * @param earlierDate
     * @param laterDate
     * @return time in hours
     */
    public static float diffInHours(Date earlierDate, Date laterDate) {
        long resultMills = laterDate.getTime() - earlierDate.getTime();
        float resultHours = (int) resultMills / 1000 / 60 / 60;

        return resultHours;
    }

    /**
     * Get first day of current month with 00:00:00 on the clock.
     * @return 
     */
    public static Date getFirstDateOfCurrentMonth() {
        return getFirstDayOfCurrentOrNextMonth(false);
    }
    
    /**
     * Get first day of next month with 00:00:00 on the clock.
     *
     * @param
     * @return
     */
    public static Date getFirstDayOfNextMonth() {
        return getFirstDayOfCurrentOrNextMonth(true);
    }

    /**
     * Calculate difference between current time and first second of next month.
     *
     * @return hours in float
     */
    public static float getHoursUntilEndOfMonth() {
        return diffInHours(new Date(), getFirstDayOfNextMonth());
    }

    private static Date getFirstDayOfCurrentOrNextMonth(boolean nextMonth) {
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(new Date());  //today
        
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        if(nextMonth) {
            calendar.add(Calendar.MONTH, 1); //add one month
        }
        
        Date result = calendar.getTime();
        calendar.setTime(new Date());
        
        return result;
    }

    /**
     * Converts interval of dates to specific format of cal-heatmap javascript
     * plugin.
     *
     * @see https://kamisama.github.io/cal-heatmap/
     *
     * @param start
     * @param end
     * @return Map<String, Integer> where string is unix timestamp of hour
     *         (anywhere from begging of it up until the end) and Integer is
     *         always 1 (represents intensity at this hour). Timestamps
     *         are made with 1 hour intervals.
     */
    public static Map<String, Integer> convertIntervalToCalHeatmapFormat(Date start, Date end) {
        if (end == null) {
            end = new Date(); //now
        }

        Map<String, Integer> counter = new HashMap<String, Integer>();
        Calendar cal = Calendar.getInstance();

        Date intervalPivot = new Date(start.getTime());

        while (intervalPivot.before(end)) {
            long unixTimestamp = intervalPivot.getTime() / 1000L;
            counter.put(String.valueOf(unixTimestamp), 1);
            cal.setTime(intervalPivot);
            cal.add(Calendar.HOUR_OF_DAY, 1);
            intervalPivot = cal.getTime();
        }
        cal.setTime(new Date());

        return counter;
    }
}
