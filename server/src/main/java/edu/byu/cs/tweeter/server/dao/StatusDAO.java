package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusDAO {
    public Pair<List<Status>, Boolean> getStatusPosts(String userAlias, int limit, Long lastPostTime) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert userAlias != null;

        List<Status> allPosts = getDummyPosts();
        List<Status> responsePosts = new ArrayList<>(limit);

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allPosts != null) {
                int statusIndex = getStartingIndex(lastPostTime, allPosts);

                for(int limitCounter = 0; statusIndex < allPosts.size() && limitCounter < limit; statusIndex++, limitCounter++) {
                    responsePosts.add(allPosts.get(statusIndex));
                }

                hasMorePages = statusIndex < allPosts.size();
            }
        }

        return new Pair<>(responsePosts, hasMorePages);
    }

    public Pair<List<Status>, Boolean> getStoryPosts(String userAlias, int limit, Long lastPostTime) {
        // TODO: Generates dummy data. Replace with a real implementation.
        assert limit > 0;
        assert userAlias != null;

        List<Status> allPosts = getDummyPosts();
        List<Status> responsePosts = new ArrayList<>(limit);

        boolean hasMorePages = false;

        if(limit > 0) {
            if (allPosts != null) {
                int statusIndex = getStartingIndex(lastPostTime, allPosts);

                for(int limitCounter = 0; statusIndex < allPosts.size() && limitCounter < limit; statusIndex++, limitCounter++) {
                    responsePosts.add(allPosts.get(statusIndex));
                }

                hasMorePages = statusIndex < allPosts.size();
            }
        }

        return new Pair<>(responsePosts, hasMorePages);
    }

    private int getStartingIndex(Long lastPostTime, List<Status> allPosts) {

        int statusIndex = 0;

        if(lastPostTime != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allPosts.size(); i++) {
                if(lastPostTime.equals(allPosts.get(i).getTimestamp())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    statusIndex = i + 1;
                    break;
                }
            }
        }

        return statusIndex;
    }

    /**
     * Returns the list of dummy status data. This is written as a separate method to allow
     * mocking of the statuses.
     *
     * @return the statuses.
     */
    List<Status> getDummyPosts() {
        return getFakeData().getFakeStatuses();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
