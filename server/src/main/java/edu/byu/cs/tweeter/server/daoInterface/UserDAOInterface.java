package edu.byu.cs.tweeter.server.daoInterface;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.util.Pair;

public interface UserDAOInterface {
    Pair<User, AuthToken> login(String username, String password);
    Pair<User, AuthToken> register(String username, String password, String firstName, String lastName, String image);
    User getUser(String username);
    Pair<Boolean, String> logout(String username);
    void addUserBatch(List<UserBean> users);
}
