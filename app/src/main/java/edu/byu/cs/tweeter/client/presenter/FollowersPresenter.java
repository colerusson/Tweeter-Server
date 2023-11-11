package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {
    public interface View extends PagedView<User> {}

    public FollowersPresenter(View view) {
        super(view);
    }

    @Override
    protected int loadItems(User user) {
        return 2;
    }
}
