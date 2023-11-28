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
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.util.Pair;

public class FollowService {
    private final FollowDAOInterface followDAO;

    public FollowService(DAOFactoryInterface factory) {
        this.followDAO = factory.getFollowDAO();
    }

    public FollowingResponse getFollowees(FollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = followDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
        return new FollowingResponse(pair.getFirst(), pair.getSecond());
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<User>, Boolean> pair = followDAO.getFollowers(request.getFollowerAlias(), request.getLimit(), request.getLastFollowerAlias());
        return new FollowersResponse(pair.getFirst(), pair.getSecond());
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        int count = followDAO.getFollowingCount(request.getUserAlias());
        return new FollowingCountResponse(count);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        int count = followDAO.getFollowersCount(request.getUserAlias());
        return new FollowersCountResponse(count);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }

        boolean isFollower = followDAO.isFollower(request.getFollowerAlias(), request.getFolloweeAlias());
        return new IsFollowerResponse(isFollower);
    }

    public FollowResponse follow(FollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        boolean success = followDAO.follow(request.getUserAlias(), request.getFollowee());
        if (success) {
            return new FollowResponse(true, "Followed");
        } else {
            return new FollowResponse(false, "Failed to follow");
        }
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }

        boolean success = followDAO.unfollow(request.getUserAlias(), request.getFollowee());
        if (success) {
            return new UnfollowResponse(true, "Unfollowed");
        } else {
            return new UnfollowResponse(false, "Failed to unfollow");
        }
    }
}
