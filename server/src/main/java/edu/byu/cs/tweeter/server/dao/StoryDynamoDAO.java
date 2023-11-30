package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.StoryBean;
import edu.byu.cs.tweeter.server.daoInterface.StoryDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

public class StoryDynamoDAO implements StoryDAOInterface {
    private static final String TableName = "story";
    public static final String IndexName = "story_index";
    public static final String PosterAttr = "poster_alias";
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
        DynamoDbTable<StoryBean> table = getClient().table(TableName, TableSchema.fromBean(StoryBean.class));
        StoryBean storyBean = new StoryBean();
        storyBean.setPoster_alias(userAlias);
        storyBean.setTimestamp(timestamp);
        storyBean.setMessage(post);
        table.putItem(storyBean);
        return true;
    }

    @Override
    public Pair<List<Status>, Boolean> getStory(String userAlias, int limit, long lastStoryTime) {
        // TODO: Sort this by timestamp from newest to oldest
        DynamoDbTable<StoryBean> table = getClient().table(TableName, TableSchema.fromBean(StoryBean.class));
        Key key = Key.builder().partitionValue(userAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (lastStoryTime != 0) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(PosterAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().n(Long.toString(lastStoryTime)).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<Status> result = new DataPage<>();

        PageIterable<StoryBean> pages = table.query(request);
        try {
            pages.stream()
                    .limit(1)
                    .forEach((Page<StoryBean> page) -> {
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(storyBean -> {
                            Status status = convertToStatus(storyBean);
                            result.getValues().add(status);
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(result.getValues(), result.isHasMorePages());
    }

    private Status convertToStatus(StoryBean storyBean) {
        User user = getUser(storyBean.getPoster_alias());
        List<String> urls = getUrls(storyBean.getMessage());
        List<String> mentions = getMentions(storyBean.getMessage());

        return new Status(storyBean.getMessage(), user, storyBean.getTimestamp(), urls, mentions);
    }

    private User getUser(String userAlias) {
        // TODO: Fix this to avoid talking to another DAO
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
