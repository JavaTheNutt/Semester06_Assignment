package ie.wit.application.model.ui;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import ie.wit.application.R;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * Created by joewe on 09/04/2017.
 */
public class UserDisplayNameObserver implements Observer
{
    private TextView userNameLabel;
    private Context context;

    /**
     * Instantiates a new User display name observer.
     *
     * @param context       the context
     * @param userNameLabel the user name label
     */
    public UserDisplayNameObserver(Context context, TextView userNameLabel)
    {
        super();
        this.context = context;
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
        String nameStr = ((UserDisplayName) o).getName();
        if (nameStr == null) {
            userNameLabel.setVisibility(View.GONE);
            return;
        }
        String labelText = context.getResources().getString(R.string.greeting, nameStr);
        Log.d(TAG, "update: updating username label with new value: " + labelText);
        userNameLabel.setText(labelText);
        userNameLabel.setVisibility(View.VISIBLE);
    }

    /**
     * Observe.
     *
     * @param o the o
     */
    public void observe(Observable o)
    {
        o.addObserver(this);
    }
}
