package edu.byu.cs.tweeter.server.factory;

import edu.byu.cs.tweeter.server.daoInterface.AuthtokenDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.FeedDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.StoryDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.UserDAOInterface;

public interface DAOFactoryInterface {
    AuthtokenDAOInterface getAuthtokenDAO();
    FeedDAOInterface getFeedDAO();
    FollowDAOInterface getFollowDAO();
    StoryDAOInterface getStoryDAO();
    UserDAOInterface getUserDAO();
}
