package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PostStatusRequest {
    private AuthToken authToken;
    private String userAlias;
    private String post;
    private long timestamp;

    public PostStatusRequest() {}

    public PostStatusRequest(AuthToken authToken, String userAlias, String post, long timestamp) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.post = post;
        this.timestamp = timestamp;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public String getPost() {
        return post;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
