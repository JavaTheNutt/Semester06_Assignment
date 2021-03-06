package ie.wit.application.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.annimon.stream.function.Consumer;

import java.text.ParseException;
import java.util.Calendar;

import ie.wit.application.main.FinanceApp;

import static ie.wit.application.activities.BaseActivity.TAG;

/**
 * Found: https://developer.android.com/guide/topics/ui/controls/pickers.html
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private Consumer<String> dateSelectedCallback;

    /**
     * Override to build your own custom Dialog container.  This is typically
     * used to show an AlertDialog instead of a generic Dialog; when doing so,
     * #onCreateView(LayoutInflater, ViewGroup, Bundle)does not need
     * to be implemented since the AlertDialog takes care of its own content.
     * <p>
     * <p>This method will be called after {@link #onCreate(Bundle)} and
     * before #onCreateView(LayoutInflater, ViewGroup, Bundle).  The
     * default implementation simply instantiates and returns a {@link Dialog}
     * class.
     * <p>
     * <p><em>Note: DialogFragment own the {@link Dialog#setOnCancelListener
     * Dialog.setOnCancelListener} and {@link Dialog#setOnDismissListener
     * Dialog.setOnDismissListener} callbacks.  You must not set them yourself.</em>
     * To find out about these events, override onCancel(DialogInterface)
     * and #onDismiss(DialogInterface).</p>
     *
     * @param savedInstanceState The last saved instance state of the Fragment,
     *                           or null if this is a freshly created Fragment.
     * @return Return a new Dialog instance to be displayed by the Fragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog, this, year, month, day);
    }

    /**
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (0-11 for compatibility with
     *                   {@link Calendar#MONTH})
     * @param dayOfMonth th selected day of the month (1-31, depending on
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        if (dateSelectedCallback != null) {
            String dateStr = dayOfMonth + "/" + (month + 1) + "/" + year;
            try {
                if (FinanceApp.serviceFactory.getUtil().checkTimestampNotBeforeToday(dateStr)) {
                    dateSelectedCallback.accept(dateStr);
                    return;
                }
                Toast.makeText(getActivity(), "Please select a date in the future", Toast.LENGTH_SHORT).show();
            } catch (ParseException e) {
                Log.e(TAG, "onDateSet: date is not in correct format", e);
            }

        }
    }

    /**
     * Register date selected callback.
     *
     * @param cb the cb
     */
    public void registerDateSelectedCallback(Consumer<String> cb)
    {
        this.dateSelectedCallback = cb;
    }
}
