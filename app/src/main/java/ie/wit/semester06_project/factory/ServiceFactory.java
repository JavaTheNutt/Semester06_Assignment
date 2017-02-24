package ie.wit.semester06_project.factory;

import ie.wit.semester06_project.service.LoginValidationServiceImpl;
import ie.wit.semester06_project.service.Util;

/**
 * Created by joewe on 24/02/2017.
 */

public class ServiceFactory
{
    private LoginValidationServiceImpl loginValidationService;
    private Util util;
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
}