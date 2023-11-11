package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetFeedTask";
    public static final String URL_PATH = "/getfeed";


    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            Long lastPostTime = getLastItem() == null ? null : getLastItem().getTimestamp();

            FeedRequest request = new FeedRequest(getAuthToken(), targetUserAlias, getLimit(), lastPostTime);
            FeedResponse response = getServerFacade().getFeed(request, URL_PATH);

            if (response.isSuccess()) {
                return new Pair<>(response.getPosts(), response.getHasMorePages());
            } else {
                throw new RuntimeException(response.getMessage());
            }
        } catch (IOException | RuntimeException | TweeterRemoteException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
