package edu.byu.cs.tweeter.server.daoInterface;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface StoryDAOInterface {
    Pair<List<Status>, Boolean> getStory(String userAlias, int limit, Long lastStoryTime);
}
