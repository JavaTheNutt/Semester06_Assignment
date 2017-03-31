package ie.wit.application.model.ui_model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import ie.wit.application.R;
import ie.wit.application.activities.BaseActivity;

/**
 * http://stackoverflow.com/a/15434375/4108556
 */
public class BalanceObserver implements Observer
{
    private TextView balanceView;
    private Context context;

    /**
     * Instantiates a new Balance observer.
     *
     * @param context     the context
     * @param balanceView the balance view
     */
    public BalanceObserver(Context context, TextView balanceView){
        super();
        this.context = context;
        this.balanceView = balanceView;
    }

    /**
     * Observe.
     *
     * @param observable the observable
     */
    public void observe(Observable observable){
        observable.addObserver(this);
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
        float balance = ((Balance)o).getCurrentBalance();
        String strTotal = "€" + Float.toString(balance);
        int colorId = balance <= 0 ? R.color.negativeBalance : R.color.positiveBalance;
        Log.d(BaseActivity.TAG, "update: " + ((Balance) o).getCurrentBalance());
        balanceView.setTextColor(ContextCompat.getColor(context, colorId));
        balanceView.setText(strTotal);
    }

}
