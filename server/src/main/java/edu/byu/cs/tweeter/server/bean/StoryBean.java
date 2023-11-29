package edu.byu.cs.tweeter.server.bean;

import edu.byu.cs.tweeter.server.dao.StoryDynamoDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StoryBean {
    private String poster_alias;
    private long timestamp;
    private String message;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = StoryDynamoDAO.IndexName)
    public String getPoster_alias() {
        return poster_alias;
    }

    public void setPoster_alias(String poster_alias) {
        this.poster_alias = poster_alias;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = StoryDynamoDAO.IndexName)
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
