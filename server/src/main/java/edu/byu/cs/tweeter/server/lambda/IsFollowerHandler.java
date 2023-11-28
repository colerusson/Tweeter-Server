package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.FollowService;

/**
 * An AWS lambda function that returns if a user follows another user
 */
public class IsFollowerHandler implements RequestHandler<IsFollowerRequest, IsFollowerResponse> {
    @Override
    public IsFollowerResponse handleRequest(IsFollowerRequest request, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        AuthtokenService authtokenService = new AuthtokenService(factory);

        if (authtokenService.validateToken(request.getAuthToken())) {
            FollowService service = new FollowService(factory);
            return service.isFollower(request);
        } else {
            return new IsFollowerResponse("Invalid auth token");
        }
    }
}
