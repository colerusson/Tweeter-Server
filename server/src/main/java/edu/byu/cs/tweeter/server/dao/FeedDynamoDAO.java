package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.daoInterface.FeedDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class FeedDynamoDAO implements FeedDAOInterface {
    @Override
    public Pair<List<Status>, Boolean> getFeed(String userAlias, int limit, Long lastFeedTime) {
        return null;
    }
}
