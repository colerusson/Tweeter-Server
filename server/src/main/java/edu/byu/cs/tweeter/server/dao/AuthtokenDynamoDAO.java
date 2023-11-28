package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.daoInterface.AuthtokenDAOInterface;

public class AuthtokenDynamoDAO implements AuthtokenDAOInterface {
    @Override
    public Boolean validateToken(AuthToken token) {
        return null;
    }

    @Override
    public String generateToken(String alias) {
        return null;
    }
}
