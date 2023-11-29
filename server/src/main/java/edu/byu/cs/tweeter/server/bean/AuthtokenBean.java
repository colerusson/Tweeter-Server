package edu.byu.cs.tweeter.server.bean;

import edu.byu.cs.tweeter.server.dao.AuthtokenDynamoDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
public class AuthtokenBean {
    private String authtoken;
    private String alias;
    private long timestamp;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = AuthtokenDynamoDAO.IndexName)
    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
