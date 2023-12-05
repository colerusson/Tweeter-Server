package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class UserStatusTest {
    private StatusService statusServiceSpy;
    private UserService userServiceSpy;
    private PagedStatusObserver pagedObserver;
    private UserObserver userObserver;
    private StatusObserver statusObserver;
    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        statusServiceSpy = Mockito.spy(new StatusService());
        userServiceSpy = Mockito.spy(new UserService());
        pagedObserver = new PagedStatusObserver();
        userObserver = new UserObserver();
        statusObserver = new StatusObserver();

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class PagedStatusObserver implements StatusService.PagedStatusObserver {
        private boolean success;
        private String message;
        private List<Status> posts;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void displayError(String message) {
            this.success = false;
            this.message = message;
            this.posts = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void displayException(Exception ex) {
            this.success = false;
            this.message = null;
            this.posts = null;
            this.hasMorePages = false;
            this.exception = ex;

            countDownLatch.countDown();
        }

        @Override
        public void loadMoreItems(List<?> items, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.posts = (List<Status>) items;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getPosts() {
            return posts;
        }

        public boolean isHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }

    private class UserObserver implements UserService.AuthenticateUserObserver {
        private boolean success;
        private String message;
        private Exception exception;
        private User user;
        private AuthToken authToken;

        @Override
        public void displayError(String message) {
            this.success = false;
            this.message = message;
            this.user = null;
            this.exception = null;
            this.authToken = null;

            countDownLatch.countDown();
        }

        @Override
        public void displayException(Exception ex) {
            this.success = false;
            this.message = null;
            this.user = null;
            this.exception = ex;
            this.authToken = null;

            countDownLatch.countDown();
        }

        @Override
        public void authenticateSucceeded(AuthToken authToken, User user) {
            this.success = true;
            this.message = null;
            this.user = user;
            this.exception = null;
            this.authToken = authToken;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public User getUser() {
            return user;
        }

        public Exception getException() {
            return exception;
        }

        public AuthToken getAuthToken() {
            return authToken;
        }
    }

    private class StatusObserver implements StatusService.StatusObserver {
        private boolean success;
        private String message;
        private Exception exception;

        @Override
        public void displayError(String message) {
            this.success = false;
            this.message = message;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void displayException(Exception ex) {
            this.success = false;
            this.message = null;
            this.exception = ex;

            countDownLatch.countDown();
        }

        @Override
        public void postStatusSucceeded() {
            this.success = true;
            this.message = null;
            this.exception = null;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Exception getException() {
            return exception;
        }
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws InterruptedException {
        userServiceSpy.login("@Kolyan", "Password123", userObserver);
        awaitCountDownLatch();

        AuthToken currentAuthToken = userObserver.getAuthToken();
        User currentUser = userObserver.getUser();

        Assertions.assertTrue(userObserver.isSuccess());
        Assertions.assertNull(userObserver.getMessage());
        Assertions.assertNotNull(userObserver.getUser());
        Assertions.assertNotNull(userObserver.getUser().getAlias());
        Assertions.assertNotNull(userObserver.getUser().getFirstName());
        Assertions.assertNotNull(userObserver.getUser().getLastName());
        Assertions.assertNotNull(userObserver.getUser().getImageUrl());
        Assertions.assertNull(userObserver.getException());
        Assertions.assertNotNull(currentAuthToken);
        Assertions.assertEquals(userObserver.getUser().getAlias(), "@Kolyan");
        Assertions.assertEquals(userObserver.getUser().getFirstName(), "Kolyan");
        Assertions.assertEquals(userObserver.getUser().getLastName(), "Russki");

        long currentTime = System.currentTimeMillis();
        Status status = new Status("Hello World!", currentUser, currentTime, null, null);

        statusServiceSpy.postStatus(currentAuthToken, status, statusObserver);
        awaitCountDownLatch();

        Assertions.assertTrue(statusObserver.isSuccess());
        Assertions.assertNull(statusObserver.getMessage());
        Assertions.assertNull(statusObserver.getException());

        statusServiceSpy.loadMoreStoryItems(currentAuthToken, currentUser, 5, null, pagedObserver);
        awaitCountDownLatch();

        Assertions.assertTrue(pagedObserver.isSuccess());
        Assertions.assertFalse(pagedObserver.isHasMorePages());
        Assertions.assertNull(pagedObserver.getException());
        Assertions.assertNull(pagedObserver.getMessage());
        Assertions.assertNotNull(pagedObserver.getPosts());
        Assertions.assertEquals(1, pagedObserver.getPosts().size());
        Status postedStatus = pagedObserver.getPosts().get(0);
        Assertions.assertEquals("Hello World!", postedStatus.getPost());
        Assertions.assertEquals(currentTime, status.getTimestamp());
        Assertions.assertEquals(currentUser, status.getUser());
    }
}
