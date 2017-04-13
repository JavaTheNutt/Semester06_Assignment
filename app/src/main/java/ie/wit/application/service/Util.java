package ie.wit.application.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * Created by joewe on 24/02/2017.
 */
public class Util
{
    /**
     * Make a toast.
     *
     * @param src the src
     * @param msg the msg
     */
    public void makeAToast(Context src, String msg)
    {
        Toast.makeText(src, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets timestamp to midnight.
     *
     * @return the timestamp to midnight
     */
    public long getTimestampToMidnight()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     * Convert to timestamp long.
     *
     * @param date the date
     * @return the long
     * @throws ParseException the parse exception
     */
    public long convertToTimestamp(String date) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date dateObj = formatter.parse(date);
        return dateObj.getTime();
    }

    /**
     * Check timestamp not before today boolean.
     *
     * @param date the date
     * @return the boolean
     * @throws ParseException the parse exception
     */
    public boolean checkTimestampNotBeforeToday(String date) throws ParseException
    {
        return convertToTimestamp(date) >= getTimestampToMidnight();
    }

    /**
     * Convert from timestamp string.
     *
     * @param timestamp the timestamp
     * @return the string
     */
    public String convertFromTimestamp(long timestamp)
    {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(timestamp);
    }
    public boolean checkTimestampPending(Long timestamp){
        return timestamp >= getTimestampToMidnight();
    }
    public String titleCase(String str)
    {
        Log.d(TAG, "titleCase: fixing string: " + str);
        String[] splitStr = str.split("\\s+");
        String[] fixedStr = new String[splitStr.length];
        Log.d(TAG, "titleCase: numbers of words: " + splitStr.length);
        int i = 0;
        for (String s : splitStr) {
            Log.d(TAG, "titleCase: current string: " + s);
            String tempStr = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            fixedStr[i] = tempStr;
            i++;
        }

        return TextUtils.join(" ", fixedStr);
    }
}
