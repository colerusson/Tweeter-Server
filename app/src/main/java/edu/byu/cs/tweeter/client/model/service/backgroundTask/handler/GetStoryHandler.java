package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;

public class GetStoryHandler extends PagedHandler<StatusService.PagedStatusObserver> {
    public GetStoryHandler(StatusService.PagedStatusObserver observer) {
        super(observer, GetStoryTask.ITEMS_KEY, GetStoryTask.MORE_PAGES_KEY);
    }
}
