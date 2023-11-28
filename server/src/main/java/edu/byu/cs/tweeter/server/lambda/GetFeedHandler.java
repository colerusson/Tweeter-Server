package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.AuthtokenService;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that returns the feed posts for a user
 */
public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        AuthtokenService authtokenService = new AuthtokenService(factory);

        if (authtokenService.validateToken(request.getAuthToken())) {
            StatusService service = new StatusService(factory);
            return service.getFeed(request);
        } else {
            return new FeedResponse("Invalid auth token");
        }
    }
}
