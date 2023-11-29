package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FollowBean;
import edu.byu.cs.tweeter.server.bean.StoryBean;
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
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
    public int getFollowingCount(String userAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userAlias).build());
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder().queryConditional(queryConditional).build();

        int count = 0;

        PageIterable<FollowBean> pages = table.query(queryRequest);
        for (Page<FollowBean> page : pages) {
            count += page.items().size();
        }

        return count;
    }

    @Override
    public int getFollowersCount(String userAlias) {
        DynamoDbIndex<FollowBean> index = getClient().table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);

        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userAlias).build());
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder().queryConditional(queryConditional).build();

        int count = 0;

        SdkIterable<Page<FollowBean>> iterable = index.query(queryRequest);
        PageIterable<FollowBean> pages = PageIterable.create(iterable);
        for (Page<FollowBean> page : pages) {
            count += page.items().size();
        }

        return count;
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();

        FollowBean follow = table.getItem(key);

        return follow != null;
    }

    @Override
    public boolean follow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        FollowBean followBean = new FollowBean();
        followBean.setFollower_alias(followerAlias);
        followBean.setFollowee_alias(followeeAlias);

        table.putItem(followBean);

        return true;
    }

    @Override
    public boolean unfollow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        Key key = Key.builder()
                .partitionValue(followerAlias)
                .sortValue(followeeAlias)
                .build();

        table.deleteItem(key);

        return true;
    }

    public List<String> getAllFollowers(String userAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userAlias).build());
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder().queryConditional(queryConditional).build();

        List<String> followers = new ArrayList<>();

        table.query(queryRequest).items().forEach(item -> {
            String followeeAlias = item.getFollowee_alias();
            followers.add(followeeAlias);
        });

        return followers;
    }

    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));
        Key key = Key.builder().partitionValue(followerAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (isNonEmptyString(lastFolloweeAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().n(String.valueOf(lastFolloweeAlias)).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<>();

        PageIterable<FollowBean> pages = table.query(request);
        try {
            pages.stream()
                    .limit(1)
                    .forEach((Page<FollowBean> page) -> {
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(followBean -> {
                            User user = convertToUser(followBean, false);
                            result.getValues().add(user);
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(result.getValues(), result.isHasMorePages());
    }

    @Override
    public Pair<List<User>, Boolean> getFollowers(String followerAlias, int limit, String lastFollowerAlias) {
        // TODO: Change this to use index

        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));
        Key key = Key.builder().partitionValue(followerAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().n(String.valueOf(lastFollowerAlias)).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<>();

        PageIterable<FollowBean> pages = table.query(request);
        try {
            pages.stream()
                    .limit(1)
                    .forEach((Page<FollowBean> page) -> {
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(followBean -> {
                            User user = convertToUser(followBean, true);
                            result.getValues().add(user);
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(result.getValues(), result.isHasMorePages());
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }

    private User convertToUser(FollowBean followBean, boolean isFollower) {
        UserDynamoDAO userDAO = new UserDynamoDAO();
        return userDAO.getUser(followBean.getFollowee_alias());
//        if (isFollower) {
//            return userDAO.getUser(followBean.getFollower_alias());
//        } else {
//            return userDAO.getUser(followBean.getFollowee_alias());
//        }
    }
}
