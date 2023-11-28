package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.daoInterface.StoryDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class StoryDynamoDAO implements StoryDAOInterface {
    @Override
    public Pair<List<Status>, Boolean> getStory(String userAlias, int limit, Long lastStoryTime) {
        return null;
    }
}
