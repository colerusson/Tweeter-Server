package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";
    public static final String URL_PATH = "/unfollow";
    private final User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void doTask() {
        // TODO: Implement this method when the database is ready.
        try {
            UnfollowRequest request = new UnfollowRequest(getAuthToken(), followee.getAlias(), getCache().getCurrUser().getAlias());
            UnfollowResponse response = getServerFacade().unfollow(request, URL_PATH);

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
