package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public abstract class AuthenticatedTask extends BackgroundTask {
    private final AuthToken authToken;

    public AuthenticatedTask(AuthToken authToken, Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
    }

    protected AuthToken getAuthToken() {
        return authToken;
    }
}
