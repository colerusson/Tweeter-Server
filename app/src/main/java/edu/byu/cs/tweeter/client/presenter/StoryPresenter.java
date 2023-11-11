package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {
    public interface View extends PagedView<Status> {}

    public StoryPresenter(View view) {
        super(view);
    }

    @Override
    protected int loadItems(User user) {
        return 1;
    }
}
