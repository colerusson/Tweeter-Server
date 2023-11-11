package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;

public class GetFollowersCountHandler extends CountHandler<FollowService.CountFollowObserver> {
    public GetFollowersCountHandler(FollowService.CountFollowObserver observer) {
        super(observer, GetFollowersCountTask.COUNT_KEY, true);
    }
}
