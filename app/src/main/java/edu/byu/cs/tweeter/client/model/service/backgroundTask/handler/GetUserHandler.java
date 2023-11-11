package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;

public class GetUserHandler extends BaseHandler<UserService.PagedUserObserver> {
    private final UserService.PagedUserObserver observer;

    public GetUserHandler(UserService.PagedUserObserver observer) {
        super(observer);
        this.observer = observer;
    }

    @Override
    protected void doSuccess(Message msg) {
        User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
        observer.goToUser(user);
    }
}
