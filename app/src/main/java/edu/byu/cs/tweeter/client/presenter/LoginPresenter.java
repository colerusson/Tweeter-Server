package edu.byu.cs.tweeter.client.presenter;

public class LoginPresenter extends AuthenticatePresenter {
    public interface View extends AuthenticateView {}
    private final View view;
    public LoginPresenter(View view) {
        super(view);
        this.view = view;
    }

    public void login(String alias, String password) {
        if (validateLogin(alias, password)) {
            view.hideErrorMessage();
            view.showInfoMessage("Logging in...");
            var userService = getUserService();
            userService.login(alias, password, getAuthenticateUserServiceObserver());
        }
    }
}
