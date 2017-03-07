package ie.wit.semester06_project.service;

import android.util.Log;

import ie.wit.semester06_project.activities.BaseActivity;
import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.Income;
import ie.wit.semester06_project.repo.IncomeRepo;

/**
 * Created by joewe on 25/02/2017.
 */

public class DashboardService
{
    private IncomeRepo incomeRepo;

    public DashboardService()
    {
        this.incomeRepo = FinanceApp.serviceFactory.getIncomeRepo();
    }

    /*public void refreshData(){
        incomeRepo.addItem(new Income(999L));
        try {
            incomeRepo.removeItem(999L);
        } catch (Exception e) {
            Log.w(BaseActivity.TAG, e.getMessage());
            e.printStackTrace();
        }
    }*/
}
