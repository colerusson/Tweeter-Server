package edu.byu.cs.tweeter.client.model.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Service {
    public interface ServiceObserver {
        void displayError(String message);
        void displayException(Exception ex);
    }

    public interface FollowObserver extends ServiceObserver {
        void isFollowerSucceeded(boolean isFollower);
        void followSucceeded();
        void unfollowSucceeded();
    }

    public interface StatusObserver extends ServiceObserver {
        void postStatusSucceeded();
    }

    public interface UserObserver extends ServiceObserver {
        void logoutSucceeded();
    }

    public interface PagedObserver extends ServiceObserver {
        void loadMoreItems(List<?> items, boolean hasMorePages);
    }

    public interface PagedUserObserver extends ServiceObserver {
        void goToUser(User user);
    }

    public interface CountObserver extends ServiceObserver {
        void followCountSucceeded(int followCount, boolean isAFollower);
    }

    public interface AuthenticateObserver extends ServiceObserver {
        void authenticateSucceeded(AuthToken authToken, User user);
    }

    public static class ServiceHelper {
        public static void executeTask(Runnable task) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(task);
        }
    }
}
