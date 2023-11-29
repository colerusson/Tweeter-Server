package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.daoInterface.AuthtokenDAOInterface;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;

public class AuthtokenService {
    private final AuthtokenDAOInterface authtokenDAO;

    public AuthtokenService(DAOFactoryInterface factory) {
        this.authtokenDAO = factory.getAuthtokenDAO();
    }

    public Boolean validateToken(AuthToken token) {
        return authtokenDAO.validateToken(token);
    }

    public void addAuthToken(String alias, AuthToken authToken) {
        authtokenDAO.addAuthToken(alias, authToken);
    }

    public void deleteToken(AuthToken authToken) {
        authtokenDAO.deleteToken(authToken);
    }
}
