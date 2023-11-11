package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.Service;

public abstract class PagedHandler<T extends Service.PagedObserver> extends BaseHandler<T> {
    private final T observer;
    private final String ITEMS_KEY;
    private final String MORE_PAGES_KEY;

    public PagedHandler(T observer, String itemsKey, String morePagesKey) {
        super(observer);
        this.observer = observer;
        this.ITEMS_KEY = itemsKey;
        this.MORE_PAGES_KEY = morePagesKey;
    }

    @Override
    protected void doSuccess(Message msg) {
        loadMoreItems(msg, ITEMS_KEY, MORE_PAGES_KEY);
    }

    protected <E> void loadMoreItems(Message msg, String itemsKey, String morePagesKey) {
        List<E> items = (List<E>) msg.getData().getSerializable(itemsKey);
        boolean hasMorePages = msg.getData().getBoolean(morePagesKey);
        observer.loadMoreItems(items, hasMorePages);
    }
}
