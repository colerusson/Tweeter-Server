package edu.byu.cs.tweeter.server.daoInterface;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthtokenDAOInterface {
    Boolean validateToken(AuthToken token);
    String generateToken(String alias);
}
