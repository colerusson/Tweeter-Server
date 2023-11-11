package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.Service;

public abstract class CountHandler<T extends Service.CountObserver> extends BaseHandler<T> {
    private final T observer;
    private final String COUNT_KEY;
    private final boolean IS_FOLLOWER;

    public CountHandler(T observer, String countKey, boolean isFollower) {
        super(observer);
        this.observer = observer;
        this.COUNT_KEY = countKey;
        this.IS_FOLLOWER = isFollower;
    }

    @Override
    protected void doSuccess(Message msg) {
        int count = msg.getData().getInt(COUNT_KEY);
        observer.followCountSucceeded(count, IS_FOLLOWER);
    }
}
