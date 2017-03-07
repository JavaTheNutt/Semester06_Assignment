package ie.wit.semester06_project.service;

import ie.wit.semester06_project.main.FinanceApp;
import ie.wit.semester06_project.model.Income;
import ie.wit.semester06_project.model.Transaction;
import ie.wit.semester06_project.repo.IncomeRepo;

/**
 * Created by joewe on 25/02/2017.
 */

public class CalculationService
{
    private IncomeRepo incomeRepo;
    public CalculationService(){
        incomeRepo = FinanceApp.serviceFactory.getIncomeRepo();
    }
    public float getTotal(){
        float total = getIncomeTotal();
        total -= getExpenditureTotal();
        return total;
    }

    public Float getIncomeTotal(){
        float total = 0;
        for (Transaction income : incomeRepo.getAllItems()) {
            total += income.getAmount();
        }
        return total;
    }
    public float getExpenditureTotal(){
        return 0;
    }

}
