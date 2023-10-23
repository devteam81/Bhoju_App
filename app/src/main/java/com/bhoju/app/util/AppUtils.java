package com.bhoju.app.util;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private AppUtils() {

    }
    public static ArrayList<Integer> getTimer(Date initDate){
        Calendar currentDate = Calendar.getInstance();
        Calendar eventDate = Calendar.getInstance();
        eventDate.setTime(initDate);
        eventDate.set(Calendar.HOUR_OF_DAY, 21);
        eventDate.set(Calendar.MINUTE, 16);

        //eventDate.set(initDate.getYear(),initDate.getMonth(),initDate.getDate(),24,0,0);
        //eventDate.setTimeZone(TimeZone.getTimeZone("GMT"));

        long diff = eventDate.getTimeInMillis() - currentDate.getTimeInMillis();


        int days = (int) (diff / (24 * 60 * 60 * 1000));
        int hours = (int) (diff / (1000 * 60 * 60) % 24);
        int minutes = (int) (diff / (1000 * 60) % 60);
        int seconds = (int) ((diff / 1000) % 60);

        ArrayList <Integer> timerdata =new ArrayList<>();
        timerdata.add(days);
        timerdata.add(hours);
        timerdata.add(minutes);
        timerdata.add(seconds);

        boolean eventCompleted = endEvent(currentDate, eventDate);
        if(eventCompleted)
            return null;
        else
            return timerdata;
    }

    public static boolean endEvent(Calendar currentDate, Calendar eventDate) {
        return currentDate.getTimeInMillis() >= eventDate.getTimeInMillis();

    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new Date().getTime();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidCard(long number) {
        return (getSize(number) >= 13 &&
                getSize(number) <= 16) &&
                (prefixMatched(number, 4) ||
                        prefixMatched(number, 5) ||
                        prefixMatched(number, 37) ||
                        prefixMatched(number, 6)) &&
                ((sumOfDoubleEvenPlace(number) +
                        sumOfOddPlace(number)) % 10 == 0);
    }

    // Get the result from Step 2
    private static int sumOfDoubleEvenPlace(long number) {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    // Return this number if it is a single digit, otherwise,
    // return the sum of the two digits
    private static int getDigit(int number) {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    // Return sum of odd-place digits in number
    private static int sumOfOddPlace(long number) {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    // Return true if the digit d is a prefix for number
    private static boolean prefixMatched(long number, int d) {
        return getPrefix(number, getSize(d)) == d;
    }

    // Return the number of digits in d
    private static int getSize(long d) {
        String num = d + "";
        return num.length();
    }

    // Return the first k number of digits from
    // number. If the number of digits in number
    // is less than k, return number.
    private static long getPrefix(long number, int k) {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }

    public static String getYouTubeIdFromUrl(String youTubeUrl) {
        if (null == youTubeUrl)
            return "error";
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    public static String dateFormattChange(String inputDate){
        String inputFormat ="yyyy-MM-dd" ;
        String outputFormat = "dd-MM-yyyy";
        String outputDate = null;

        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat);

        try {
            Date date = inputDateFormat.parse(inputDate);
            outputDate = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//      UiUtils.log(TAG, "passed date "+inputDate);
//      UiUtils.log(TAG, "Converted date " +outputDate);
        return outputDate;
    }
}
