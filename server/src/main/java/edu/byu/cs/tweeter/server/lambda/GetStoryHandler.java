package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.StatusService;

/**
 * An AWS lambda function that returns the story posts for a user
 */
public class GetStoryHandler implements RequestHandler<StoryRequest, StoryResponse> {
    /**
     * Returns the story posts for the user specified in the request. Uses information in
     * the request object to limit the number of posts returned and to return the next set of
     * posts after any that were returned in a previous request.
     *
     * @param request contains the data required to fulfill the request.
     * @param context the lambda context.
     * @return the posts.
     */
    @Override
    public StoryResponse handleRequest(StoryRequest request, Context context) {
        StatusService service = new StatusService();
        return service.getStory(request);
    }
}
