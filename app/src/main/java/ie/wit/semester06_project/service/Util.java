package ie.wit.semester06_project.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import ie.wit.semester06_project.activities.BaseActivity;

/**
 * Created by joewe on 24/02/2017.
 */

public class Util
{
    public void makeAToast(Context src, String msg)
    {
        Toast.makeText(src, msg, Toast.LENGTH_SHORT).show();
    }

}
