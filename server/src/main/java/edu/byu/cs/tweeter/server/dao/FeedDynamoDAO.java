package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FeedBean;
import edu.byu.cs.tweeter.server.bean.StoryBean;
import edu.byu.cs.tweeter.server.daoInterface.FeedDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class FeedDynamoDAO implements FeedDAOInterface {
    private static final String TableName = "feed";
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
    public Boolean postStatus(String userAlias, String post, long timestamp) {
        FollowDynamoDAO followDAO = new FollowDynamoDAO();
        List<String> followerAliases = followDAO.getAllFollowers(userAlias);

        if (followerAliases != null) {
            DynamoDbTable<FeedBean> feedTable = getClient().table(TableName, TableSchema.fromBean(FeedBean.class));
            for (String followerAlias : followerAliases) {
                FeedBean feedBean = new FeedBean();
                feedBean.setReceiver_alias(followerAlias);
                feedBean.setTimestamp(timestamp);
                feedBean.setMessage(post);
                feedBean.setPoster_alias(userAlias);
                feedTable.putItem(feedBean);
            }
        }

        return true;
    }

    @Override
    public Pair<List<Status>, Boolean> getFeed(String userAlias, int limit, long lastFeedTime) {
        // TODO: Sort this by timestamp from newest to oldest
        DynamoDbTable<FeedBean> table = getClient().table(TableName, TableSchema.fromBean(FeedBean.class));
        Key key = Key.builder().partitionValue(userAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastFeedTime != 0) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(ReceiverAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().n(String.valueOf(lastFeedTime)).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Status> result = new DataPage<>();

        PageIterable<FeedBean> pages = table.query(request);
        try {
            pages.stream()
                    .limit(1)
                    .forEach((Page<FeedBean> page) -> {
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(feedBean -> {
                            Status status = convertToStatus(feedBean);
                            result.getValues().add(status);
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(result.getValues(), result.isHasMorePages());
    }

    private Status convertToStatus(FeedBean feedBean) {
        User user = getUser(feedBean.getPoster_alias());
        List<String> urls = getUrls(feedBean.getMessage());
        List<String> mentions = getMentions(feedBean.getMessage());

        return new Status(feedBean.getMessage(), user, feedBean.getTimestamp(), urls, mentions);
    }

    private User getUser(String userAlias) {
        UserDynamoDAO userDAO = new UserDynamoDAO();
        return userDAO.getUser(userAlias);
    }

    private List<String> getUrls(String message) {
        List<String> urls = new ArrayList<>();
        String[] words = message.split(" ");
        for (String word : words) {
            if (word.startsWith("http://") || word.startsWith("https://")) {
                urls.add(word);
            }
        }
        return urls;
    }

    private List<String> getMentions(String message) {
        List<String> mentions = new ArrayList<>();
        String[] words = message.split(" ");
        for (String word : words) {
            if (word.startsWith("@")) {
                mentions.add(word);
            }
        }
        return mentions;
    }
}
