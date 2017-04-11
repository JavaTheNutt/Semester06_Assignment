package ie.wit.application.service;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    public long getTimestampToMidnight()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public long convertToTimestamp(String date) throws ParseException
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date dateObj = formatter.parse(date);
        return dateObj.getTime();
    }

    public boolean checkTimestampNotBeforeToday(String date) throws ParseException
    {
        return convertToTimestamp(date) >= getTimestampToMidnight();
    }

    public String convertFromTimestamp(long timestamp)
    {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(timestamp);
    }
}
