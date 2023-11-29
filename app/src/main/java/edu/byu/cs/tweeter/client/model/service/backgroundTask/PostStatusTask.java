package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {
    private static final String LOG_TAG = "PostStatusTask";
    public static final String URL_PATH = "/poststatus";
    private final Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(authToken, messageHandler);
        this.status = status;
    }

    @Override
    protected void doTask() {
        try {
            User currUser = status.getUser();
            String post = status.getPost();
            long timestamp = status.getTimestamp();

            PostStatusRequest request = new PostStatusRequest(getAuthToken(), currUser.getAlias(), post, timestamp);
            PostStatusResponse response = getServerFacade().postStatus(request, URL_PATH);

            if (response.isSuccess()) {
                loadSuccessBundle(new Bundle());
            } else {
                throw new RuntimeException(response.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(AuthenticatedTask.SUCCESS_KEY, true);
        msgBundle.putString(AuthenticatedTask.MESSAGE_KEY, "Status posted successfully!");
    }
}
