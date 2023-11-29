package edu.byu.cs.tweeter.server.daoInterface;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface FeedDAOInterface {
    Pair<List<Status>, Boolean> getFeed(String userAlias, int limit, long lastFeedTime);
    Boolean postStatus(String userAlias, String post, long timestamp);
}
