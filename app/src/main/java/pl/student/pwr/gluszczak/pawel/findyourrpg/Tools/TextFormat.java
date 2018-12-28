package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import pl.student.pwr.gluszczak.pawel.findyourrpg.R;

public abstract class TextFormat {


    /**
     * Cuts unnecessary numbers after comma
     *
     * @param value
     * @param leftAfterComma
     * @return
     */
    public static String precisionStringFromDouble(double value, int leftAfterComma) {
        Double toCut = BigDecimal.valueOf(value)
                .setScale(leftAfterComma, RoundingMode.HALF_UP)
                .doubleValue();

        return String.valueOf(toCut);

    }

    /**
     * Returns date in proper string format
     *
     * @param date
     * @return
     */
    public static String dateToDateString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        String dayString = String.valueOf(day);
        //Fix date converting
        //String monthString = month == 0 ? "12" : String.valueOf(month);
        String monthString = String.valueOf(month);
        return "" + dayString + "." + monthString;
    }


    /**
     * Concat hour and min ints into proper string format
     *
     * @param hourI
     * @param min
     * @return
     */
    public static String hourMinToString(int hourI, int min) {
        int hour = hourI;
        StringBuilder builder = new StringBuilder();
        builder.append(hour < 10 ? "0" + hour : hour);
        builder.append(":");
        builder.append(min < 10 ? "0" + min : min);
        return builder.toString();
    }

    /**
     * Returns time in proper string format
     *
     * @param date
     * @return
     */
    public static String dateToTimeString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        StringBuilder builder = new StringBuilder();
        builder.append(hour < 10 ? "0" + hour : hour);
        builder.append(":");
        builder.append(min < 10 ? "0" + min : min);
        return builder.toString();
    }

    /**
     * Returns String with players needed and current in proper String format
     *
     * @param needed
     * @param current
     * @return
     */
    public static String playersNeededLeft(int needed, int current) {
        return "" + current + "/" + needed;
    }

    /**
     * Generates a string with proper distance unit
     *
     * @param distance
     * @return
     */
    public static String getDistanceString(Context context, int distance) {
        return "" + String.valueOf(distance) + context.getApplicationContext().getString(R.string.distance_unit);
    }

    /**
     * Generates a string with proper distance unit
     *
     * @param distance
     * @return
     */
    public static String getDistanceString(Context context, double distance) {
        return "" + String.valueOf(distance) + context.getApplicationContext().getString(R.string.distance_unit);
    }


    public static String getEventDateString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        String monthString = month < 10 ? "0" + month : String.valueOf(month);
        String dayString = day < 10 ? "0" + day : String.valueOf(day);
        String hourString = hour < 10 ? "0" + hour : String.valueOf(hour);
        String minString = min < 10 ? "0" + min : String.valueOf(min);

        return ""
                + dayString
                + "."
                + monthString
                + "."
                + calendar.get(Calendar.YEAR)
                + ",  "
                + hourString
                + ":"
                + minString;
    }

}
