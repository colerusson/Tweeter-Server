package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends CountTask {
    private static final String LOG_TAG = "GetFollowingCountTask";
    public static final String URL_PATH = "/getfollowingcount";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();

            FollowingCountRequest request = new FollowingCountRequest(getAuthToken(), targetUserAlias);
            FollowingCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);

            if (response.isSuccess()) {
                return response.getCount();
            } else {
                throw new RuntimeException(response.getMessage());
            }
        } catch (IOException | RuntimeException | TweeterRemoteException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
