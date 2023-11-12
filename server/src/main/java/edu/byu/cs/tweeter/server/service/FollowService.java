package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = getFollowingDAO().getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = getFollowingDAO().getFollowers(request.getFollowerAlias(), request.getLimit(), request.getLastFollowerAlias());
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        int count = 20;
        return new FollowingCountResponse(count);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if(request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        int count = 20;
        return new FollowersCountResponse(count);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        boolean isFollower = new Random().nextInt() > 0;
        return new IsFollowerResponse(isFollower);
    }

    public FollowResponse follow(FollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        return new FollowResponse(true, "Followed");
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        return new UnfollowResponse(true, "Unfollowed");
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}
