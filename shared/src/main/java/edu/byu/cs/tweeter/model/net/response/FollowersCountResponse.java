package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;

/**
 * A paged response for a {@link FollowersCountRequest}.
 */
public class FollowersCountResponse extends CountResponse {

    /**
     * Creates a response indicating that the corresponding request was unsuccessful. Sets the
     * success and more pages indicators to false.
     *
     * @param message a message describing why the request was unsuccessful.
     */
    public FollowersCountResponse(String message) {
        super(false, message, 0);
    }

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param count the count to be included in the result.
     */
    public FollowersCountResponse(int count) {
        super(true, count);
    }
}
