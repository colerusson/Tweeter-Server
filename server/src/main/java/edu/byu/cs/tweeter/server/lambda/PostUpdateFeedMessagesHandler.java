package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        FollowService followService = new FollowService(factory);

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String messageBody = msg.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(messageBody);

                String userAlias = jsonNode.get("userAlias").asText();
                String post = jsonNode.get("post").asText();
                long timestamp = jsonNode.get("timestamp").asLong();

                String lastFollowerAlias = null;
                int batchSize = 100;

                List<String> followers = followService.getPagedFollowers(userAlias, batchSize, null);
                if (!followers.isEmpty()) {
                    lastFollowerAlias = followers.get(followers.size() - 1);
                }


                while (!followers.isEmpty()) {
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("userAlias", userAlias);
                    messageMap.put("post", post);
                    messageMap.put("timestamp", timestamp);
                    messageMap.put("followers", followers);

                    ObjectMapper newObjectMapper = new ObjectMapper();
                    String newMessageBody = newObjectMapper.writeValueAsString(messageMap);

                    String queueUrl = "https://sqs.us-west-2.amazonaws.com/379683941185/updateFeedQueue";
                    SendMessageRequest send_msg_request = new SendMessageRequest()
                            .withQueueUrl(queueUrl)
                            .withMessageBody(newMessageBody);

                    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
                    sqs.sendMessage(send_msg_request);

                    followers = followService.getPagedFollowers(userAlias, batchSize, lastFollowerAlias);
                    if (!followers.isEmpty()) {
                        lastFollowerAlias = followers.get(followers.size() - 1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
