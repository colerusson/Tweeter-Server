package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class CountResponse extends Response {

    private final int count;

    CountResponse(boolean success, int count) {
        super(success);
        this.count = count;
    }

    CountResponse(boolean success, String message, int count) {
        super(success, message);
        this.count = count;
    }

    /**
     * An indicator of whether more data is available from the server. A value of true indicates
     * that the result was limited by a maximum value in the request and an additional request
     * would return additional data.
     *
     * @return true if more data is available; otherwise, false.
     */
    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FollowersCountResponse that = (FollowersCountResponse) param;

        return (Objects.equals(count, that.getCount()) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
