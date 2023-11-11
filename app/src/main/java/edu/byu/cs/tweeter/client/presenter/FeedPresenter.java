package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {
    public interface View extends PagedView<Status> {}

    public FeedPresenter(View view) {
        super(view);
    }

    @Override
    protected int loadItems(User user) {
        return 1;
    }
}
