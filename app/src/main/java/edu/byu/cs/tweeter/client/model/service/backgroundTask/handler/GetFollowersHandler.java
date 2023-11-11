package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;

public class GetFollowersHandler extends PagedHandler<FollowService.PagedFollowObserver> {
    public GetFollowersHandler(FollowService.PagedFollowObserver observer) {
        super(observer, GetFollowersTask.ITEMS_KEY, GetFollowersTask.MORE_PAGES_KEY);
    }
}
