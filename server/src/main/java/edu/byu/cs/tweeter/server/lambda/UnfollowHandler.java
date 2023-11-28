package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.FollowService;

public class UnfollowHandler implements RequestHandler<UnfollowRequest, UnfollowResponse> {
    @Override
    public UnfollowResponse handleRequest(UnfollowRequest request, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        AuthtokenService authtokenService = new AuthtokenService(factory);

        if (authtokenService.validateToken(request.getAuthToken())) {
            FollowService service = new FollowService(factory);
            return service.unfollow(request);
        } else {
            return new UnfollowResponse(false, "Invalid auth token.");
        }
    }
}
