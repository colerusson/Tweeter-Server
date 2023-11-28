package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.daoInterface.UserDAOInterface;
import edu.byu.cs.tweeter.util.Pair;

public class UserDynamoDAO implements UserDAOInterface {
    @Override
    public Pair<User, AuthToken> login(String username, String password) {
        return null;
    }

    @Override
    public Pair<User, AuthToken> register(String username, String password, String firstName, String lastName, String image) {
        return null;
    }

    @Override
    public User getUser(String username) {
        return null;
    }

    @Override
    public Pair<Boolean, String> logout(String username) {
        return null;
    }
}
