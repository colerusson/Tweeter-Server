package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that posts a status for a user
 */
public class PostStatusHandler implements RequestHandler<PostStatusRequest, PostStatusResponse> {
    @Override
    public PostStatusResponse handleRequest(PostStatusRequest request, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        AuthtokenService authtokenService = new AuthtokenService(factory);

        if (authtokenService.validateToken(request.getAuthToken())) {
            StatusService statusService = new StatusService(factory);
            return statusService.postStatus(request);
        } else {
            return new PostStatusResponse(false, "Invalid auth token");
        }
    }
}
