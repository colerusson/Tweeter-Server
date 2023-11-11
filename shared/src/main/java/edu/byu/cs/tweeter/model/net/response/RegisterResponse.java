package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

/**
 * A response for a {@link RegisterRequest}.
 */
public class RegisterResponse extends AuthenticateResponse {
    /**
     * Creates a response indicating that the corresponding request was unsuccessful.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public RegisterResponse(String message) {
        super(false, message, null, null);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param user the now logged in user.
     * @param authToken the auth token representing this user's session with the server.
     */
    public RegisterResponse(User user, AuthToken authToken) {
        super(true, user, authToken);
    }
}
