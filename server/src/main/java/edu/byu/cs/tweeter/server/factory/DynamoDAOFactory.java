package edu.byu.cs.tweeter.server.factory;

import edu.byu.cs.tweeter.server.dao.AuthtokenDynamoDAO;
import edu.byu.cs.tweeter.server.dao.FeedDynamoDAO;
import edu.byu.cs.tweeter.server.dao.FollowDynamoDAO;
import edu.byu.cs.tweeter.server.dao.StoryDynamoDAO;
import edu.byu.cs.tweeter.server.dao.UserDynamoDAO;
import edu.byu.cs.tweeter.server.daoInterface.AuthtokenDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.FeedDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.StoryDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.UserDAOInterface;

public class DynamoDAOFactory implements DAOFactoryInterface {
    @Override
    public AuthtokenDAOInterface getAuthtokenDAO() {
        return new AuthtokenDynamoDAO();
    }

    @Override
    public FeedDAOInterface getFeedDAO() {
        return new FeedDynamoDAO();
    }

    @Override
    public FollowDAOInterface getFollowDAO() {
        return new FollowDynamoDAO();
    }

    @Override
    public StoryDAOInterface getStoryDAO() {
        return new StoryDynamoDAO();
    }

    @Override
    public UserDAOInterface getUserDAO() {
        return new UserDynamoDAO();
    }
}
