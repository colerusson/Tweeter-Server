package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.StatusService;

public class PostStatusHandler extends BaseHandler<StatusService.StatusObserver> {
    private final StatusService.StatusObserver observer;

    public PostStatusHandler(StatusService.StatusObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    protected void doSuccess(Message msg) {
        observer.postStatusSucceeded();
    }
}
