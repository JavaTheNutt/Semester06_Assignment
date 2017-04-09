package ie.wit.application.model.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import static ie.wit.application.activities.BaseActivity.TAG;
/**
 * Created by joewe on 09/04/2017.
 */

public class UserDisplayNameObserver implements Observer
{
    private TextView userNameLabel;

    public UserDisplayNameObserver(TextView userNameLabel)
    {
        super();
        this.userNameLabel = userNameLabel;
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg)
    {
        Log.d(TAG, "update: updating username label with new value: " + ((UserDisplayName)o).getName());
        userNameLabel.setText("Welcome, " + ((UserDisplayName)o).getName());
        userNameLabel.setVisibility(View.VISIBLE);
    }
    public void observe(Observable o){
        o.addObserver(this);
    }
}
