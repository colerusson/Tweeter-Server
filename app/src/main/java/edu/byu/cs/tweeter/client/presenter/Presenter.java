package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter {
    public interface BaseView {
        void displayMessage(String message);
    }

    public BaseView view;
    private final FollowService followService;
    private final UserService userService;
    private final StatusService statusService;
    public Presenter(BaseView view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
        this.statusService = new StatusService();
    }

    public UserService getUserService() {
        return userService;
    }
    public FollowService getFollowService() {
        return followService;
    }
    public StatusService getStatusService() {
        return statusService;
    }
}
