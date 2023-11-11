package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.util.Pair;

public abstract class PagedTask<T> extends AuthenticatedTask {
    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";
    private final User targetUser;
    private final int limit;
    private final T lastItem;
    private List<T> items;
    private boolean hasMorePages;

    public User getTargetUser() {
        return targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public T getLastItem() {
        return lastItem;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public PagedTask(Handler messageHandler, AuthToken authToken, User targetUser, int limit, T LastItem) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = LastItem;
    }

    @Override
    protected void doTask() throws IOException, TweeterRemoteException {
        Pair<List<T>, Boolean> pageOfItems = getItems();
        setItems(pageOfItems.getFirst());
        setHasMorePages(pageOfItems.getSecond());
    }

    protected abstract Pair<List<T>, Boolean> getItems() throws IOException, TweeterRemoteException;

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages());
    }
}
