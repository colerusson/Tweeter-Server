package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class LogoutHandler extends BaseHandler<UserService.UserObserver> {
    private final UserService.UserObserver observer;

    public LogoutHandler(UserService.UserObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    protected void doSuccess(Message msg) {
        Cache.getInstance().setCurrUser(null);
        Cache.getInstance().setCurrUserAuthToken(null);
        observer.logoutSucceeded();
    }
}
