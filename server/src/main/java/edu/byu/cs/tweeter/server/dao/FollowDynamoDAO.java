package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.FollowBean;
import edu.byu.cs.tweeter.server.daoInterface.FollowDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

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
    public int getFollowersCount(String userAlias) {
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
    public int getFollowingCount(String userAlias) {
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
                .partitionValue(followeeAlias)
                .sortValue(followerAlias)
                .build();

        FollowBean follow = table.getItem(key);

        return follow != null;
    }

    @Override
    public boolean follow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        FollowBean followBean = new FollowBean();
        followBean.setFollower_alias(followeeAlias);
        followBean.setFollowee_alias(followerAlias);

        table.putItem(followBean);

        return true;
    }

    @Override
    public boolean unfollow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));

        Key key = Key.builder()
                .partitionValue(followeeAlias)
                .sortValue(followerAlias)
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
    public Pair<List<User>, Boolean> getFollowers(String followerAlias, int limit, String lastFolloweeAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));
        Key key = Key.builder().partitionValue(followerAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (isNonEmptyString(lastFolloweeAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

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
    public List<String> getPagedFollowers(String userAlias, int limit, String lastFollowerAlias) {
        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));
        Key key = Key.builder().partitionValue(userAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        List<String> result = new ArrayList<>();

        PageIterable<FollowBean> pages = table.query(request);
        try {
            pages.stream()
                    .limit(1)
                    .forEach((Page<FollowBean> page) -> {
                        page.items().forEach(followBean -> {
                            String user = followBean.getFollowee_alias();
                            result.add(user);
                        });
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Pair<List<User>, Boolean> getFollowees(String followerAlias, int limit, String lastFollowerAlias) {
        DynamoDbIndex<FollowBean> index = getClient().table(TableName, TableSchema.fromBean(FollowBean.class)).index(IndexName);
        Key key = Key.builder().partitionValue(followerAlias).build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit);

        if (isNonEmptyString(lastFollowerAlias)) {
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<User> result = new DataPage<>();

        SdkIterable<Page<FollowBean>> iterable = index.query(request);
        PageIterable<FollowBean> pages = PageIterable.create(iterable);
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
        if (isFollower) {
            return userDAO.getUser(followBean.getFollower_alias());
        } else {
            return userDAO.getUser(followBean.getFollowee_alias());
        }
    }

    public void addFollowersBatch(List<FollowBean> followers) {
        List<FollowBean> batchToWrite = new ArrayList<>();

        for (FollowBean follower : followers) {
            batchToWrite.add(follower);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfFollowersDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfFollowersDTOs(batchToWrite);
        }
    }

    private void writeChunkOfFollowersDTOs(List<FollowBean> followBeans) {
        if (followBeans.size() > 25)
            throw new RuntimeException("Too many followers to write");

        DynamoDbTable<FollowBean> table = getClient().table(TableName, TableSchema.fromBean(FollowBean.class));
        WriteBatch.Builder<FollowBean> writeBuilder = WriteBatch.builder(FollowBean.class).mappedTableResource(table);

        for (FollowBean item : followBeans) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = getClient().batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfFollowersDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
