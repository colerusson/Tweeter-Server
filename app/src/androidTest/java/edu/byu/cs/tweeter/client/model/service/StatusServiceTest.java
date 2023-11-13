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

public class StatusServiceTest {
    private User currentUser;
    private AuthToken authToken;
    private StatusService statusServiceSpy;
    private PagedStatusObserver observer;
    private CountDownLatch countDownLatch;

    @BeforeEach
    public void setup() {
        currentUser = new User("FirstName", "LastName", null);
        authToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());
        observer = new PagedStatusObserver();

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

    @Test
    public void testGetStory_validRequest_correctResponse() throws InterruptedException {
        statusServiceSpy.loadMoreStoryItems(authToken, currentUser, 10, null, observer);
        awaitCountDownLatch();

        List<Status> expectedPosts = FakeData.getInstance().getFakeStatuses().subList(0, 10);
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertTrue(observer.isHasMorePages());
        Assertions.assertEquals(expectedPosts, observer.getPosts());
        Assertions.assertNull(observer.getException());
        Assertions.assertNull(observer.getMessage());
    }
}
