package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;

public class IsFollowerHandler extends BaseHandler<FollowService.FollowObserver> {
    private final FollowService.FollowObserver observer;

    public IsFollowerHandler(FollowService.FollowObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    protected void doSuccess(Message msg) {
        boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.isFollowerSucceeded(isFollower);
    }
}
