package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Service {
    public interface UserObserver extends Service.UserObserver {}
    public interface PagedUserObserver extends Service.PagedUserObserver {}
    public interface AuthenticateUserObserver extends AuthenticateObserver {}

    public void goToUser(AuthToken currUserAuthToken, String username, PagedUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, username, new GetUserHandler(observer));
        ServiceHelper.executeTask(getUserTask);
    }

    public void login(String username, String password, AuthenticateUserObserver observer) {
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        ServiceHelper.executeTask(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticateUserObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password, imageBytesBase64, new RegisterHandler(observer));
        ServiceHelper.executeTask(registerTask);
    }

    public void logout(AuthToken authToken, UserObserver observer) {
        LogoutTask logoutTask = new LogoutTask(authToken, new LogoutHandler(observer));
        ServiceHelper.executeTask(logoutTask);
    }
}
