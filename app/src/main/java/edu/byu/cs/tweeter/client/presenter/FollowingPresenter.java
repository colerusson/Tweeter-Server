package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {
    public interface View extends PagedView<User> {}

    public FollowingPresenter(View view) {
        super(view);
    }

    @Override
    protected int loadItems(User user) {
        return 0;
    }
}
