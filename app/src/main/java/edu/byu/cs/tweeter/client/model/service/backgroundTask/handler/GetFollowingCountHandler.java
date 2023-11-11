package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;

public class GetFollowingCountHandler extends CountHandler<FollowService.CountFollowObserver> {
    public GetFollowingCountHandler(FollowService.CountFollowObserver observer) {
        super(observer, GetFollowingCountTask.COUNT_KEY, false);
    }
}
