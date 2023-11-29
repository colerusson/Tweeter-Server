package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that registers a user and returns the user object and an auth code for
 * a successful register.
 */
public class RegisterHandler implements RequestHandler<RegisterRequest, RegisterResponse> {
    @Override
    public RegisterResponse handleRequest(RegisterRequest request, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        UserService userService = new UserService(factory);

        String imageUrl = userService.uploadImage(request.getImageUrl(), request.getUsername());

        request.setImageUrl(imageUrl);
        RegisterResponse registerResponse = userService.register(request);

        if (registerResponse.isSuccess()) {
            AuthtokenService authtokenService = new AuthtokenService(factory);
            authtokenService.addAuthToken(registerResponse.getUser().getAlias(), registerResponse.getAuthToken());
        }
        return registerResponse;
    }
}
