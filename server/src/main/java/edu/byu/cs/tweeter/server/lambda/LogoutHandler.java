package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user out and clears the cache for the user for
 * a successful logout.
 */
public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest request, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        AuthtokenService authtokenService = new AuthtokenService(factory);

        if (authtokenService.validateToken(request.getAuthToken())) {
            UserService userService = new UserService(factory);
            return userService.logout(request);
        } else {
            return new LogoutResponse(false, "Invalid auth token");
        }
    }
}
