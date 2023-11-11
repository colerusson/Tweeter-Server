package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;

public class GetFeedHandler extends PagedHandler<StatusService.PagedStatusObserver> {
    public GetFeedHandler(StatusService.PagedStatusObserver observer) {
        super(observer, GetFeedTask.ITEMS_KEY, GetFeedTask.MORE_PAGES_KEY);
    }
}
