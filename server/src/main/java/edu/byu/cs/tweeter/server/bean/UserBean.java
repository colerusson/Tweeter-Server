package edu.byu.cs.tweeter.server.bean;

import edu.byu.cs.tweeter.server.dao.UserDynamoDAO;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

@DynamoDbBean
public class UserBean {
    private String first_name;
    private String last_name;
    private String alias;
    private String password;
    private String image_url;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = UserDynamoDAO.IndexName)
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getImage_url() {
        return image_url;
    }
}
