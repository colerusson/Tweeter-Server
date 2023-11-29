package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(LoginRequest loginRequest, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        UserService userService = new UserService(factory);
        LoginResponse loginResponse = userService.login(loginRequest, context);
        if (loginResponse.isSuccess()) {
            AuthtokenService authtokenService = new AuthtokenService(factory);
            authtokenService.addAuthToken(loginResponse.getUser().getAlias(), loginResponse.getAuthToken());
        }
        return loginResponse;
    }
}
