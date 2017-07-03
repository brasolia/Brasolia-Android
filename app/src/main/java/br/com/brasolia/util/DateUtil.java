package br.com.brasolia.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cayke on 28/06/17.
 */

public class DateUtil {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new Date().getTime();
        if (time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Agora mesmo.";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "Um minuto atrás.";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutos atrás";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Uma hora atrás";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " horas atrás";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Ontem.";
        } else {
            return diff / DAY_MILLIS + " dias atrás.";
        }
    }

    public static String getEventDateString(Date startDate, Date endDate) {
        Date now = new Date();

        if (startDate.getTime() < now.getTime() && now.getTime() < endDate.getTime())
            return "Agora!";
        else if (isSameDay(now, startDate)) {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
            return "Hoje - " + fmt.format(startDate);
        }
        else if (isTomorrow(startDate)) {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
            return "Amanhã - " + fmt.format(startDate);
        }
        else {
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM HH:mm");
            return fmt.format(startDate);
        }
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    private static boolean isTomorrow(Date date1) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        return isSameDay(gc.getTime(), date1);
    }
}
