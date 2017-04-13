package ie.wit.application.model.ui;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import ie.wit.application.R;

/**
 * http://stackoverflow.com/a/15434375/4108556
 */
public class BalanceObserver implements Observer
{
    private final TextView balanceView;
    private final TextView totalIncomeView;
    private final TextView totalExpenditureView;
    private final Context context;
    private final String VALUE;

    /**
     * Instantiates a new Balance observer.
     *
     * @param context the context
     * @param views   the views
     */
    public BalanceObserver(Context context, Map<String, TextView> views, String value)
    {
        super();
        this.context = context;
        this.balanceView = views.get("balance");
        this.totalIncomeView = views.get("income");
        this.totalExpenditureView = views.get("expenditure");
        this.VALUE = value;
    }


    /**
     * Observe.
     *
     * @param observable the observable
     */
    public void observe(Observable observable)
    {
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
        Map<String, Float> data = ((Balance) o).getAll(VALUE);
        String strBalance = "€" + roundFloat(data.get("balance").toString());
        String strIncome = "€" + roundFloat(data.get("income").toString());
        String strExpenditure = "€" + roundFloat(data.get("expenditure").toString());
        int balanceColorId = data.get("balance") <= 0 ? R.color.negativeBalance : R.color.positiveBalance;
        balanceView.setText(strBalance);
        balanceView.setTextColor(ContextCompat.getColor(context, balanceColorId));
        totalIncomeView.setText(strIncome);
        totalExpenditureView.setText(strExpenditure);

    }

    //http://stackoverflow.com/a/2808648/4108556
    private float roundFloat(String value)
    {
        BigDecimal bd = new BigDecimal(value);
        bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
