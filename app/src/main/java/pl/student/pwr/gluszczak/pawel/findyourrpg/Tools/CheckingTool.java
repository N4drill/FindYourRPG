package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.student.pwr.gluszczak.pawel.findyourrpg.Enums.ExpierienceLevel;

abstract public class CheckingTool {

    /**
     * Retruns true if string is empty
     *
     * @param string String to check
     * @return
     */

    public static boolean isEmpty(String string) {
        return string.equals("");
    }


    /**
     * Return true if string are both same
     *
     * @param string1
     * @param string2
     * @return
     */
    public static boolean doesStringMatch(String string1, String string2) {
        return string1.equals(string2);
    }

    /**
     * Return true if input String contains number
     *
     * @param input
     * @return
     */
    public static boolean isStringNumber(String input) {
        try {
            int age = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Primitive check if input String is email
     *
     * @param string
     * @return
     */
    public static boolean isStringEmail(String string) {
        return string.contains("@");
    }

    /**
     * Return true if value is in bounds with > low && < high
     *
     * @param low
     * @param high
     * @param value
     * @return
     */
    public static boolean isIntInBounds(int low, int high, int value) {
        return value > low && value < high;
    }

    /**
     * Return true if passed date is same day as current Date
     *
     * @param toCheckDate
     * @return
     */
    public static boolean isToday(Date toCheckDate) {
        Date currentDate = new Date();
        Calendar currentCalendar = Calendar.getInstance();
        Calendar toCheckCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);
        toCheckCalendar.setTime(toCheckDate);

        return currentCalendar.get(Calendar.DAY_OF_MONTH) == toCheckCalendar.get(Calendar.DAY_OF_MONTH)
                && currentCalendar.get(Calendar.MONTH) == toCheckCalendar.get(Calendar.MONTH)
                && currentCalendar.get(Calendar.YEAR) == toCheckCalendar.get(Calendar.YEAR);
    }


    /**
     * Return true if passed hour and min is before current Date time
     *
     * @param hour
     * @param min
     * @return
     */
    public static boolean isHourAndMinBeforeDate(int hour, int min) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        if (hour < calendar.get(Calendar.HOUR)) {
            return true;
        } else {
            return hour == calendar.get(Calendar.HOUR) && min < calendar.get(Calendar.MINUTE);
        }
    }

    public static boolean areFirstExpLowerOrSameAsSecondExp(Context context, String first, String second) {
        ExpierienceLevel singleton = ExpierienceLevel.getInstance(context);
        List<String> expLevels = singleton.getExpLevels();

        if (!(expLevels.contains(first) && expLevels.contains(second))) {
            return false;
        }

        return expLevels.indexOf(first) <= expLevels.indexOf(second);
    }
}
