package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {
    public interface PagedView<U> extends BaseView {
        void setLoadingFooter(boolean value);
        void addMoreItems(List<U> items);
        void goToUser(User user);
    }
    private static final int PAGE_SIZE = 5;
    private T lastItem;
    private boolean hasMorePages;
    private boolean isLoading;
    public PagedView<T> view;

    public PagedPresenter(PagedView<T> view) {
        super(view);
        this.view = view;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            if (loadItems(user) == 1) {
                StatusService statusService = getStatusService();
                statusService.loadMoreFeedItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, (Status) lastItem, new StatusServiceObserver());
            } else if (loadItems(user) == 2) {
                FollowService followService = getFollowService();
                followService.loadMoreFollowerItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, (User) lastItem, new FollowServiceObserver());
            } else if (loadItems(user) == 3){
                StatusService statusService = getStatusService();
                statusService.loadMoreStoryItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, (Status) lastItem, new StatusServiceObserver());
            } else {
                FollowService followService = getFollowService();
                followService.loadMoreFolloweeItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, (User) lastItem, new FollowServiceObserver());
            }
        }
    }

    protected abstract int loadItems(User user);

    public void goToUser(String username) {
        UserService userService = getUserService();
        userService.goToUser(Cache.getInstance().getCurrUserAuthToken(), username, new UserServiceObserver());
    }

    private class UserServiceObserver implements UserService.PagedUserObserver {
        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }
        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get items because of exception: " + ex);
        }
        @Override
        public void goToUser(User user) {
            view.goToUser(user);
        }
    }

    private class StatusServiceObserver implements StatusService.PagedStatusObserver {
        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }
        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get statuses because of exception: " + ex);
        }
        @Override
        public void loadMoreItems(List<?> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            PagedPresenter.this.hasMorePages = hasMorePages;
            if (items != null) {
                lastItem = (items.size() > 0) ? (T) items.get(items.size() - 1) : null;
                view.addMoreItems((List<T>) items);
            }
        }
    }

    private class FollowServiceObserver implements FollowService.PagedFollowObserver {
        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage(message);
        }
        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);
            view.displayMessage("Failed to get following because of exception: " + ex);
        }
        @Override
        public void loadMoreItems(List<?> items, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);
            PagedPresenter.this.hasMorePages = hasMorePages;
            if (items != null) {
                lastItem = (items.size() > 0) ? (T) items.get(items.size() - 1) : null;
                view.addMoreItems((List<T>) items);
            }
        }
    }
}
