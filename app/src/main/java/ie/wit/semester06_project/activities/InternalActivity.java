package ie.wit.semester06_project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ie.wit.semester06_project.R;
import ie.wit.semester06_project.main.FinanceApp;

/**
 * The type Internal activity.
 */
public class InternalActivity extends BaseActivity
{
    /**
     * The Details database reference.
     */
    protected DatabaseReference detailsDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        detailsDatabaseReference = FirebaseDatabase.getInstance().getReference("details/" + FinanceApp.getCurrentUser().getKey() + "/transactions");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_finance, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        MenuItem dashboard = menu.findItem(R.id.menuDashboard);
        MenuItem addIncome = menu.findItem(R.id.menuAddIncome);
        if (this instanceof DashboardActivity) {
            dashboard.setEnabled(false);
            addIncome.setEnabled(true);
        } else if (this instanceof AddIncomeActivity) {
            addIncome.setEnabled(false);
            dashboard.setEnabled(true);
        }
        return true;
    }

    /**
     * Sign out.
     *
     * @param item the item
     */
    public void signOut(MenuItem item)
    {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Add income.
     *
     * @param item the item
     */
    public void addIncome(MenuItem item)
    {
        startActivity(new Intent(this, AddIncomeActivity.class));
    }

    /**
     * Dashboard.
     *
     * @param item the item
     */
    public void dashboard(MenuItem item)
    {
        startActivity(new Intent(this, DashboardActivity.class));
    }
}
