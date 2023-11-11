package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class MainActionPresenter extends Presenter {
    public interface MainActionView extends BaseView {
        void postStatus();
        void isFollower(boolean isFollower);
        void follow();
        void unfollow();
        void followCountSucceeded(int followCount, boolean isAFollower);
        void logout();
    }

    public MainActionView view;

    public MainActionPresenter(MainActionView view) {
        super(view);
        this.view = view;
    }

    protected StatusServiceObserver getStatusServiceObserver() { return new StatusServiceObserver(); }
    protected FollowServiceObserver getFollowServiceObserver() { return new FollowServiceObserver(); }
    protected CountFollowObserver getCountFollowServiceObserver() { return new CountFollowObserver(); }
    protected UserServiceObserver getUserServiceObserver() { return new UserServiceObserver(); }

    private class UserServiceObserver implements UserService.UserObserver {
        @Override
        public void displayException(Exception ex) { view.displayMessage(ex.getMessage());}
        @Override
        public void displayError(String message) { view.displayMessage(message); }
        @Override
        public void logoutSucceeded() { view.logout(); }
    }

    private class StatusServiceObserver implements StatusService.StatusObserver {
        @Override
        public void displayError(String message) { view.displayMessage(message); }
        @Override
        public void displayException(Exception ex) { view.displayMessage(ex.getMessage()); }
        @Override
        public void postStatusSucceeded() {
            view.displayMessage("Posting status...");
            view.postStatus();
        }
    }

    private class FollowServiceObserver implements FollowService.FollowObserver {
        @Override
        public void displayError(String message) { view.displayMessage(message); }
        @Override
        public void displayException(Exception ex) { view.displayMessage(ex.getMessage()); }
        @Override
        public void isFollowerSucceeded(boolean isFollower) { view.isFollower(isFollower); }
        @Override
        public void followSucceeded() { view.follow(); }
        @Override
        public void unfollowSucceeded() { view.unfollow(); }
    }

    private class CountFollowObserver implements FollowService.CountFollowObserver {
        @Override
        public void displayError(String message) { view.displayMessage(message); }
        @Override
        public void displayException(Exception ex) { view.displayMessage(ex.getMessage()); }
        @Override
        public void followCountSucceeded(int followCount, boolean isAFollower) { view.followCountSucceeded(followCount, isAFollower); }
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }
}
