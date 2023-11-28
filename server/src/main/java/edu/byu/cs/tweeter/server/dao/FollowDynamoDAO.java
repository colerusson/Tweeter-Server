package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class FollowDynamoDAO implements FollowDAOInterface {
    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        return null;
    }

    @Override
    public Pair<List<User>, Boolean> getFollowers(String followerAlias, int limit, String lastFollowerAlias) {
        return null;
    }

    @Override
    public int getFollowingCount(String userAlias) {
        return 0;
    }

    @Override
    public int getFollowersCount(String userAlias) {
        return 0;
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        return false;
    }

    @Override
    public boolean follow(String followerAlias, String followeeAlias) {
        return false;
    }

    @Override
    public boolean unfollow(String followerAlias, String followeeAlias) {
        return false;
    }
}
