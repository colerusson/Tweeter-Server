package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedTask<Status> {
    private static final String LOG_TAG = "GetStoryTask";
    public static final String URL_PATH = "/getstory";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            long lastPostTime;
            if (getLastItem() != null) {
                lastPostTime = getLastItem().getTimestamp();
            } else {
                lastPostTime = 0;
            }

            StoryRequest request = new StoryRequest(getAuthToken(), targetUserAlias, getLimit(), lastPostTime);
            StoryResponse response = getServerFacade().getStory(request, URL_PATH);

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