package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends Presenter {
    public interface AuthenticateView extends BaseView {
        void showErrorMessage(String message);
        void hideErrorMessage();
        void showInfoMessage(String message);
        void hideInfoMessage();
        void openMainView(User user);
    }

    public AuthenticateView view;

    public AuthenticatePresenter(AuthenticateView view) {
        super(view);
        this.view = view;
    }

    public AuthenticateUserServiceObserver getAuthenticateUserServiceObserver() { return new AuthenticateUserServiceObserver(); }

    private class AuthenticateUserServiceObserver implements UserService.AuthenticateUserObserver {
        @Override
        public void displayException(Exception ex) { view.displayMessage(ex.getMessage());}
        @Override
        public void displayError(String message) { view.displayMessage(message); }
        @Override
        public void authenticateSucceeded(AuthToken authToken, User user) {
            view.hideInfoMessage();
            view.hideErrorMessage();
            view.showInfoMessage("Hello, " + user.getName() + "!");
            view.openMainView(user);
        }
    }


    public boolean validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }

        return true;
    }

    public boolean validateRegistration(String firstName, String lastName, String alias, String password, String imageToUpload) {
        if (firstName.length() == 0) {
            view.showErrorMessage("First Name cannot be empty.");
            return false;
        }
        if (lastName.length() == 0) {
            view.showErrorMessage("Last Name cannot be empty.");
            return false;
        }
        if (alias.length() == 0) {
            view.showErrorMessage("Alias cannot be empty.");
            return false;
        }
        if (alias.charAt(0) != '@') {
            view.showErrorMessage("Alias must begin with @.");
            return false;
        }
        if (alias.length() < 2) {
            view.showErrorMessage("Alias must contain 1 or more characters after the @.");
            return false;
        }
        if (password.length() == 0) {
            view.showErrorMessage("Password cannot be empty.");
            return false;
        }
        if (imageToUpload == null) {
            view.showErrorMessage("Profile image must be uploaded.");
            return false;
        }

        return true;
    }

    public String convertImageToBase64(Bitmap image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
