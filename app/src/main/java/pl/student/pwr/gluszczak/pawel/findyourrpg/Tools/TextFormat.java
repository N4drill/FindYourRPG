package pl.student.pwr.gluszczak.pawel.findyourrpg.Tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

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
        return "" + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH);
    }


    /**
     * Concat hour and min ints into proper string format
     *
     * @param hour
     * @param min
     * @return
     */
    public static String hourMinToString(int hour, int min) {
        StringBuilder builder = new StringBuilder();
        builder.append(hour < 10 ? "0" + hour : hour);
        builder.append(":");
        builder.append(min < 10 ? "0" + min : min);
        return builder.toString();
    }

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

}
