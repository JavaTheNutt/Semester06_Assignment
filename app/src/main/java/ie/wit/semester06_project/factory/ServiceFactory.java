package ie.wit.semester06_project.factory;

import ie.wit.semester06_project.repo.IncomeRepo;
import ie.wit.semester06_project.service.CalculationService;
import ie.wit.semester06_project.service.DashboardService;
import ie.wit.semester06_project.service.LoginValidationServiceImpl;
import ie.wit.semester06_project.service.Util;

/**
 * Created by joewe on 24/02/2017.
 */

public class ServiceFactory
{
    private LoginValidationServiceImpl loginValidationService;
    private Util util;
    private IncomeRepo incomeRepo;
    private CalculationService calculationService;
    private DashboardService dashboardService;

    public LoginValidationServiceImpl getLoginValidationService()
    {
        if(loginValidationService == null){
            loginValidationService = new LoginValidationServiceImpl();
        }
        return loginValidationService;
    }
    public Util getUtil(){
        if(util == null){
            util = new Util();
        }
        return util;
    }
    public IncomeRepo getIncomeRepo(){
        if(incomeRepo == null){
            incomeRepo = new IncomeRepo();
        }
        return incomeRepo;
    }
    public CalculationService getCalculationService(){
        if(calculationService == null){
            calculationService = new CalculationService();
        }
        return calculationService;
    }
    public DashboardService getDashboardService(){
        if (dashboardService == null){
            dashboardService = new DashboardService();
        }
        return dashboardService;
    }
}
