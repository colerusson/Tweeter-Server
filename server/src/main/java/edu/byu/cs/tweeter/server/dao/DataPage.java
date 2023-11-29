package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

public class DataPage<T> {
    private List<T> values; // page of values returned by the database
    private boolean hasMorePages; // Indicates whether there are more pages of data available to be retrieved

    public DataPage() {
        setValues(new ArrayList<T>());
        setHasMorePages(false);
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public List<T> getValues() {
        return values;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public T getLastEvaluatedKey() {
        // return the last key in the list of values
        if (values.size() > 0) {
            return (T) values.get(values.size() - 1);
        } else {
            return null;
        }
    }
}