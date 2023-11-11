package edu.byu.cs.tweeter.client.presenter;

public class RegisterPresenter extends AuthenticatePresenter {
    public interface View extends AuthenticateView {}
    private final View view;
    public RegisterPresenter(View view) {
        super(view);
        this.view = view;
    }

    public void register(String firstName, String lastName, String alias, String password, String imageToUpload) {
        if (validateRegistration(firstName, lastName, alias, password, imageToUpload)) {
            view.hideErrorMessage();
            view.showInfoMessage("Register Successful");
            var userService = getUserService();
            userService.register(firstName, lastName, alias, password, imageToUpload, getAuthenticateUserServiceObserver());
        }
    }
}
