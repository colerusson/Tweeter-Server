package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.bean.AuthtokenBean;
import edu.byu.cs.tweeter.server.daoInterface.AuthtokenDAOInterface;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class AuthtokenDynamoDAO implements AuthtokenDAOInterface {
    private static final String TableName = "authtoken";
    public static final String IndexName = "authtoken_index";

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
    public Boolean validateToken(AuthToken token) {
        DynamoDbTable<AuthtokenBean> table = getClient().table(TableName, TableSchema.fromBean(AuthtokenBean.class));
        Key key = Key.builder().partitionValue(token.getToken()).build();

        AuthtokenBean authtokenBean = table.getItem(key);
        return authtokenBean != null;
    }

    @Override
    public void addAuthToken(String alias, AuthToken authToken) {
        DynamoDbTable<AuthtokenBean> table = getClient().table(TableName, TableSchema.fromBean(AuthtokenBean.class));
        AuthtokenBean authtokenBean = new AuthtokenBean();

        authtokenBean.setAlias(alias);
        authtokenBean.setAuthtoken(authToken.getToken());
        authtokenBean.setTimestamp(authToken.getTimestamp());
        table.putItem(authtokenBean);
    }

    @Override
    public void deleteToken(AuthToken authToken) {
        DynamoDbTable<AuthtokenBean> table = getClient().table(TableName, TableSchema.fromBean(AuthtokenBean.class));
        Key key = Key.builder().partitionValue(authToken.getToken()).build();

        table.deleteItem(key);
    }
}
