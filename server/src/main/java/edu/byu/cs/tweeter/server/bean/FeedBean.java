package edu.byu.cs.tweeter.server.bean;

import edu.byu.cs.tweeter.server.dao.FeedDynamoDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedBean {
    private String receiver_alias;
    private long timestamp;
    private String message;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FeedDynamoDAO.IndexName)
    public String getReceiver_alias() {
        return receiver_alias;
    }

    public void setReceiver_alias(String receiver_alias) {
        this.receiver_alias = receiver_alias;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FeedDynamoDAO.IndexName)
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
