package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;

public class LoginHandler extends AuthenticateHandler<UserService.AuthenticateUserObserver> {
    public LoginHandler(UserService.AuthenticateUserObserver observer) {
        super(observer, LoginTask.USER_KEY, LoginTask.AUTH_TOKEN_KEY);
    }
}
