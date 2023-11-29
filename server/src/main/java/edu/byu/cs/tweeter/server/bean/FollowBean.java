package edu.byu.cs.tweeter.server.bean;

import edu.byu.cs.tweeter.server.dao.FollowDynamoDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowBean {
    private String follower_alias;
    private String followee_alias;
    private String follower_name;
    private String followee_name;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowDynamoDAO.IndexName)
    public String getFollower_alias() {
        return follower_alias;
    }

    public void setFollower_alias(String followerHandle) {
        this.follower_alias = followerHandle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FollowDynamoDAO.IndexName)
    public String getFollowee_alias() {
        return followee_alias;
    }

    public void setFollowee_alias(String followeeHandle) {
        this.followee_alias = followeeHandle;
    }

    public void setFollower_name(String followerName) {
        this.follower_name = followerName;
    }

    public void setFollowee_name(String followeeName) {
        this.followee_name = followeeName;
    }

    public String getFollower_name() {
        return follower_name;
    }

    public String getFollowee_name() {
        return followee_name;
    }
}
