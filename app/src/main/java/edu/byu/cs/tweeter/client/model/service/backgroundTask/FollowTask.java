package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "FollowTask";
    public static final String URL_PATH = "/follow";
    private final User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void doTask() {
        // TODO: Implement this method when the database is ready.
        try {
            FollowRequest request = new FollowRequest(getAuthToken(), followee.getAlias(), getCache().getCurrUser().getAlias());
            FollowResponse response = getServerFacade().follow(request, URL_PATH);

            if (response.isSuccess()) {
                // TODO: Implement this method when the database is ready.
            } else {
                throw new RuntimeException(response.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        // TODO: Implement this method when the database is ready.
    }
}
