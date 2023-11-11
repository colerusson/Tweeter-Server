package edu.byu.cs.tweeter.model.net.response;

public class IsFollowerResponse extends Response {

        private boolean follower;

        public IsFollowerResponse(boolean isFollower) {
            super(true);
            this.follower = isFollower;
        }

        public IsFollowerResponse(String message) {
            super(false, message);
        }

        public boolean isFollower() {
            return follower;
        }

        public void setFollower(boolean follower) {
            this.follower = follower;
        }
}
