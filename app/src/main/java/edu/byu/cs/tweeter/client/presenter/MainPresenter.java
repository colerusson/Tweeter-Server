package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends MainActionPresenter {
    public interface View extends MainActionView {}
    private final FollowService followService;
    private final UserService userService;
    public MainPresenter(View view) {
        super(view);
        this.followService = getFollowService();
        this.userService = getUserService();
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(), selectedUser, getFollowServiceObserver());
    }

    public void unfollow(User user) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), user, getFollowServiceObserver());
    }

    public void follow(User user) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(), user, getFollowServiceObserver());
    }

    public void logout() {
        userService.logout(Cache.getInstance().getCurrUserAuthToken(), getUserServiceObserver());
    }

    public void postStatus(Status status) {
        getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), status, getStatusServiceObserver());
    }

    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.updateSelectedUserFollowingAndFollowers(Cache.getInstance().getCurrUserAuthToken(), selectedUser, getCountFollowServiceObserver());
    }
}
