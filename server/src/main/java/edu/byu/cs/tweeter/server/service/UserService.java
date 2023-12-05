package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.server.daoInterface.ImageDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.UserDAOInterface;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.util.Pair;

public class UserService {
    private final UserDAOInterface userDAO;
    private final ImageDAOInterface imageDAO;

    public UserService(DAOFactoryInterface factory) {
        this.userDAO = factory.getUserDAO();
        this.imageDAO = factory.getImageDAO();
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null) {
            return new LoginResponse("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            return new LoginResponse("[Bad Request] Missing a password");
        }

        Pair<User, AuthToken> result = userDAO.login(request.getUsername(), request.getPassword());
        if (result == null) {
            return new LoginResponse("[Bad Request] Invalid username or password");
        }
        return new LoginResponse(result.getFirst(), result.getSecond());
    }

    public LogoutResponse logout(LogoutRequest request) {
        if (request.getUsername() == null) {
            return new LogoutResponse(false,"[Bad Request] Missing a username");
        }

        Pair<Boolean, String> result = userDAO.logout(request.getUsername());
        return new LogoutResponse(result.getFirst(), result.getSecond());
    }

    public RegisterResponse register(RegisterRequest request) {
        if (request.getUsername() == null) {
            return new RegisterResponse("[Bad Request] Missing a username");
        } else if (request.getPassword() == null) {
            return new RegisterResponse("[Bad Request] Missing a password");
        } else if (request.getFirstName() == null) {
            return new RegisterResponse("[Bad Request] Missing a first name");
        } else if (request.getLastName() == null) {
            return new RegisterResponse("[Bad Request] Missing a last name");
        } else if (request.getImageUrl() == null) {
            return new RegisterResponse("[Bad Request] Missing an image");
        }

        Pair<User, AuthToken> result = userDAO.register(request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName(), request.getImageUrl());
        if (result == null) {
            return new RegisterResponse("[Bad Request] Username already taken");
        }
        return new RegisterResponse(result.getFirst(), result.getSecond());
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAlias() == null) {
            return new GetUserResponse("[Bad Request] Missing a username");
        }

        User user = userDAO.getUser(request.getAlias());
        return new GetUserResponse(user);
    }

    public String uploadImage(String imageUrl, String userAlias) {
        return imageDAO.uploadImage(imageUrl, userAlias);
    }

    public void fillDatabase(List<UserBean> users) {
        if (users.size() > 0) {
            userDAO.addUserBatch(users);
        }
    }
}
