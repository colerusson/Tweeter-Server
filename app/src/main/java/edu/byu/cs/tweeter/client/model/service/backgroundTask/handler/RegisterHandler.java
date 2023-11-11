package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;

public class RegisterHandler extends AuthenticateHandler<UserService.AuthenticateUserObserver> {
    public RegisterHandler(UserService.AuthenticateUserObserver observer) {
        super(observer, RegisterTask.USER_KEY, RegisterTask.AUTH_TOKEN_KEY);
    }
}
