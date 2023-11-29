package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FeedBean;
import edu.byu.cs.tweeter.server.bean.StoryBean;
import edu.byu.cs.tweeter.server.daoInterface.FeedDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class FeedDynamoDAO implements FeedDAOInterface {
    private static final String FeedTableName = "feed";
    public static final String IndexName = "feed_index";
    public static final String ReceiverAttr = "receiver_alias";
    public static final String TimestampAttr = "timestamp";

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
    public Pair<List<Status>, Boolean> getFeed(String userAlias, int limit, long lastFeedTime) {
        return null;
    }

    @Override
    public Boolean postStatus(String userAlias, String post, long timestamp) {
        FollowDynamoDAO followDAO = new FollowDynamoDAO();
        List<String> followerAliases = followDAO.getAllFollowers(userAlias);

        if (followerAliases != null) {
            // Add the post to each follower's feed
            DynamoDbTable<FeedBean> feedTable = getClient().table(FeedTableName, TableSchema.fromBean(FeedBean.class));
            for (String followerAlias : followerAliases) {
                FeedBean feedBean = new FeedBean();
                feedBean.setReceiver_alias(followerAlias); // The user who made the post
                feedBean.setTimestamp(timestamp);
                feedBean.setMessage(post);
                feedTable.putItem(feedBean);
            }
        }

        return true;
    }
}
