package edu.byu.cs.tweeter.server.daoInterface;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FollowBean;
import edu.byu.cs.tweeter.util.Pair;

public interface FollowDAOInterface {
    Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias);
    Pair<List<User>, Boolean> getFollowers(String followerAlias, int limit, String lastFollowerAlias);
    int getFollowingCount(String userAlias);
    int getFollowersCount(String userAlias);
    boolean isFollower(String followerAlias, String followeeAlias);
    boolean follow(String followerAlias, String followeeAlias);
    boolean unfollow(String followerAlias, String followeeAlias);
    void addFollowersBatch(List<FollowBean> followers);
    List<String> getPagedFollowers(String userAlias, int limit, String lastFollowerAlias);
}
