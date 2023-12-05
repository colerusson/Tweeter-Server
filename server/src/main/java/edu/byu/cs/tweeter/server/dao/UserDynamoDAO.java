package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.server.daoInterface.UserDAOInterface;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserDynamoDAO implements UserDAOInterface {
    private static final String TableName = "user";

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
    public Pair<User, AuthToken> login(String alias, String password) {
        DynamoDbTable<UserBean> table = getClient().table(TableName, TableSchema.fromBean(UserBean.class));
        Key key = Key.builder().partitionValue(alias).build();

        UserBean userBean = table.getItem(key);
        if (userBean == null) {
            return null;
        }

        String storedHashedPassword = userBean.getPassword();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, storedHashedPassword)) {
            return null;
        }

        User user = new User(userBean.getFirst_name(), userBean.getLast_name(), userBean.getAlias(), userBean.getImage_url());

        String authTokenString = java.util.UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        AuthToken authToken = new AuthToken(authTokenString, timestamp);

        return new Pair<>(user, authToken);
    }

    @Override
    public Pair<User, AuthToken> register(String username, String password, String firstName, String lastName, String image) {
        DynamoDbTable<UserBean> table = getClient().table(TableName, TableSchema.fromBean(UserBean.class));
        UserBean userBean = new UserBean();

        userBean.setAlias(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        userBean.setPassword(hashedPassword);

        userBean.setFirst_name(firstName);
        userBean.setLast_name(lastName);
        userBean.setImage_url(image);
        table.putItem(userBean);

        String authTokenString = java.util.UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();
        AuthToken authToken = new AuthToken(authTokenString, timestamp);

        User user = new User(firstName, lastName, username, image);
        return new Pair<>(user, authToken);
    }

    @Override
    public User getUser(String username) {
        DynamoDbTable<UserBean> table = getClient().table(TableName, TableSchema.fromBean(UserBean.class));
        Key key = Key.builder().partitionValue(username).build();

        UserBean userBean = table.getItem(key);
        if (userBean == null) {
            return null;
        }

        return new User(userBean.getFirst_name(), userBean.getLast_name(), userBean.getAlias(), userBean.getImage_url());
    }

    @Override
    public Pair<Boolean, String> logout(String username) {
        DynamoDbTable<UserBean> table = getClient().table(TableName, TableSchema.fromBean(UserBean.class));
        Key key = Key.builder().partitionValue(username).build();

        UserBean userBean = table.getItem(key);
        if (userBean == null) {
            return new Pair<>(false, "User not found");
        }
        else {
            return new Pair<>(true, "User logged out");
        }
    }

    public void addUserBatch(List<UserBean> users) {
        List<UserBean> batchToWrite = new ArrayList<>();

        for (UserBean user : users) {
            batchToWrite.add(user);

            if (batchToWrite.size() == 25) {
                // package this batch up and send to DynamoDB.
                writeChunkOfUserDTOs(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        // write any remaining
        if (batchToWrite.size() > 0) {
            // package this batch up and send to DynamoDB.
            writeChunkOfUserDTOs(batchToWrite);
        }
    }

    private void writeChunkOfUserDTOs(List<UserBean> userBeans) {
        if (userBeans.size() > 25)
            throw new RuntimeException("Too many users to write");

        DynamoDbTable<UserBean> table = getClient().table(TableName, TableSchema.fromBean(UserBean.class));
        WriteBatch.Builder<UserBean> writeBuilder = WriteBatch.builder(UserBean.class).mappedTableResource(table);

        for (UserBean item : userBeans) {
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try {
            BatchWriteResult result = getClient().batchWriteItem(batchWriteItemEnhancedRequest);

            // just hammer dynamodb again with anything that didn't get written this time
            if (result.unprocessedPutItemsForTable(table).size() > 0) {
                writeChunkOfUserDTOs(result.unprocessedPutItemsForTable(table));
            }

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
