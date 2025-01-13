package controller.login;

import model.model.AccountModel;
import model.model.AdminModel;
import model.model.StaffModel;
import view.login.frames.Login;
import view.home.frames.MainView;

public class LogoutController {
    public static void logoutFromMain(MainView mainView){
        new Login();
        mainView.dispose();
        resetModel();
    }

    public static void resetModel(){
        AccountModel.reset();
        AdminModel.reset();
        StaffModel.reset();
    }
}
