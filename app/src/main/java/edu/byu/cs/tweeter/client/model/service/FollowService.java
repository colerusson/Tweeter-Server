package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service {
    public interface FollowObserver extends Service.FollowObserver {}
    public interface PagedFollowObserver extends PagedObserver {}
    public interface CountFollowObserver extends CountObserver {}

    public void loadMoreFolloweeItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee, PagedFollowObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ServiceHelper.executeTask(getFollowingTask);
    }

    public void loadMoreFollowerItems(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, PagedFollowObserver observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        ServiceHelper.executeTask(getFollowersTask);
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, FollowObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken, currUser, selectedUser, new IsFollowerHandler(observer));
        ServiceHelper.executeTask(isFollowerTask);
    }

    public void follow(AuthToken currUserAuthToken, User user, FollowObserver observer) {
        FollowTask followTask = new FollowTask(currUserAuthToken, user, new FollowHandler(observer));
        ServiceHelper.executeTask(followTask);
    }

    public void unfollow(AuthToken currUserAuthToken, User user, FollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken, user, new UnfollowHandler(observer));
        ServiceHelper.executeTask(unfollowTask);
    }

    public void updateSelectedUserFollowingAndFollowers(AuthToken authToken, User selectedUser, CountFollowObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(authToken, selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(authToken, selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }
}
