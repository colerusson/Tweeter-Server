package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.FollowService;

public class FollowHandler extends BaseHandler<FollowService.FollowObserver> {
    private final FollowService.FollowObserver observer;

    public FollowHandler(FollowService.FollowObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    protected void doSuccess(Message msg) {
        observer.followSucceeded();
    }
}
