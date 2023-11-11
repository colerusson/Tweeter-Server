package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticateResponse extends Response {
    private final User user;
    private final AuthToken authToken;


    AuthenticateResponse(boolean success, User user, AuthToken authToken) {
        super(success);
        this.user = user;
        this.authToken = authToken;
    }
    public AuthenticateResponse(boolean success, String message, User user, AuthToken authToken) {
        super(success, message);
        this.user = user;
        this.authToken = authToken;
    }


    /**
     * Returns the logged in user.
     *
     * @return the user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the auth token.
     *
     * @return the auth token.
     */
    public AuthToken getAuthToken() {
        return authToken;
    }
}
