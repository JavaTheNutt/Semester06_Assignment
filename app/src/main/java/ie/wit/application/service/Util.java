package ie.wit.application.service;

import android.content.Context;
import android.widget.Toast;

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

}
