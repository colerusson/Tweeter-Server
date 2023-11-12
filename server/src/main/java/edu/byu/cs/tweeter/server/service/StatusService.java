package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {

    public FeedResponse getFeed(FeedRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<Status>, Boolean> pair = getStatusDAO().getStatusPosts(request.getUserAlias(), request.getLimit(), request.getLastPostTime());
        return new FeedResponse(pair.getFirst(), pair.getSecond());
    }

    public StoryResponse getStory(StoryRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<Status>, Boolean> pair = getStatusDAO().getStoryPosts(request.getUserAlias(), request.getLimit(), request.getLastStoryTime());
        return new StoryResponse(pair.getFirst(), pair.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        } else if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user");
        } else if(request.getPost() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if(request.getTimestamp() == 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a message");
        }

        return new PostStatusResponse(true, "Post successful");
    }

    StatusDAO getStatusDAO() {
        return new StatusDAO();
    }
}
