package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class HelperDate {
    private static SimpleDateFormat sdf;

    /**
     * Formats a date
     *
     * @param date
     *            Date to format
     * @param format
     *            Format to use
     * @return Date string format
     */
    public static String format(Date date, String format) {
        sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * @return Current date
     */
    public static Date now() {
        return Calendar.getInstance().getTime();
    }

}

