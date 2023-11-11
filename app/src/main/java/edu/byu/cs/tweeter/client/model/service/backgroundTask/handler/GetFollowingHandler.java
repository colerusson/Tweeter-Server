package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;

public class GetFollowingHandler extends PagedHandler<FollowService.PagedFollowObserver> {
    public GetFollowingHandler(FollowService.PagedFollowObserver observer) {
        super(observer, GetFollowingTask.ITEMS_KEY, GetFollowingTask.MORE_PAGES_KEY);
    }
}
