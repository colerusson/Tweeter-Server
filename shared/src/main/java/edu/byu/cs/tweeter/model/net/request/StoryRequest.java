package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * stories for a specified user.
 */
public class StoryRequest {

    private AuthToken authToken;
    private String userAlias;
    private int limit;
    private long lastPostTime;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private StoryRequest() {}

    public StoryRequest(AuthToken authToken, String userAlias, int limit, Long lastPostTime) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastPostTime = lastPostTime;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Long getLastStoryTime() {
        return lastPostTime;
    }

    public void setLastStoryTime(Long lastPostTime) {
        this.lastPostTime = lastPostTime;
    }
}
