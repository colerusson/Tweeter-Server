package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetStoryHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {
    public interface StatusObserver extends Service.StatusObserver {}
    public interface PagedStatusObserver extends PagedObserver {}

    public void loadMoreFeedItems(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedStatusObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken, user, pageSize, lastStatus, new GetFeedHandler(observer));
        ServiceHelper.executeTask(getFeedTask);
    }

    public void loadMoreStoryItems(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, PagedStatusObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken, user, pageSize, lastStatus, new GetStoryHandler(observer));
        ServiceHelper.executeTask(getStoryTask);
    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, StatusObserver observer) {
        PostStatusTask postStatusTask = new PostStatusTask(currUserAuthToken, newStatus, new PostStatusHandler(observer));
        ServiceHelper.executeTask(postStatusTask);
    }
}
