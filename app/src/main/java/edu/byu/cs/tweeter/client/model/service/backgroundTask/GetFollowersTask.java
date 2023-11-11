package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {
    private static final String LOG_TAG = "GetFollowersTask";
    public static final String URL_PATH = "/getfollowers";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollower);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFollowerAlias = getLastItem() == null ? null : getLastItem().getAlias();

            FollowersRequest request = new FollowersRequest(getAuthToken(), targetUserAlias, getLimit(), lastFollowerAlias);
            FollowersResponse response = getServerFacade().getFollowers(request, URL_PATH);

            if (response.isSuccess()) {
                return new Pair<>(response.getFollowers(), response.getHasMorePages());
            } else {
                throw new RuntimeException(response.getMessage());
            }
        } catch (IOException | RuntimeException | TweeterRemoteException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
