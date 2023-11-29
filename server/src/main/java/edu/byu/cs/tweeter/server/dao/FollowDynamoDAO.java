package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class FollowDynamoDAO implements FollowDAOInterface {
    private static final String TableName = "follow";
    public static final String IndexName = "follow_index";
    public static final String FollowerAttr = "follower_alias";
    public static final String FolloweeAttr = "followee_alias";

    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build();

    private static DynamoDbEnhancedClient enhancedClient;

    private DynamoDbEnhancedClient getClient() {
        if (enhancedClient == null) {
            enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }
        return enhancedClient;
    }

    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        return null;
    }

    @Override
    public Pair<List<User>, Boolean> getFollowers(String followerAlias, int limit, String lastFollowerAlias) {
        return null;
    }

    @Override
    public int getFollowingCount(String userAlias) {
        return 0;
    }

    @Override
    public int getFollowersCount(String userAlias) {
        return 0;
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        return false;
    }

    @Override
    public boolean follow(String followerAlias, String followeeAlias) {
        return false;
    }

    @Override
    public boolean unfollow(String followerAlias, String followeeAlias) {
        return false;
    }

    public List<String> getAllFollowers(String userAlias) {
        return null;
    }
}
