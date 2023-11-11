package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;
import android.util.Pair;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.Service;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticateHandler<T extends Service.AuthenticateObserver> extends BaseHandler<T> {
    private final T observer;
    private final String USER_KEY;
    private final String  AUTH_TOKEN_KEY;

    public AuthenticateHandler(T observer, String userKey, String authTokenKey) {
        super(observer);
        this.observer = observer;
        this.USER_KEY = userKey;
        this.AUTH_TOKEN_KEY = authTokenKey;
    }

    @Override
    protected void doSuccess(Message msg) {
        Pair<AuthToken, User> pair = handleLoginRegister(msg, USER_KEY, AUTH_TOKEN_KEY);
        observer.authenticateSucceeded(pair.first, pair.second);
    }

    protected Pair<AuthToken, User> handleLoginRegister(Message msg, String userKey, String authTokenKey) {
        User user = (User) msg.getData().getSerializable(userKey);
        AuthToken authToken = (AuthToken) msg.getData().getSerializable(authTokenKey);
        Cache.getInstance().setCurrUser(user);
        Cache.getInstance().setCurrUserAuthToken(authToken);
        return new Pair<>(authToken, user);
    }
}
