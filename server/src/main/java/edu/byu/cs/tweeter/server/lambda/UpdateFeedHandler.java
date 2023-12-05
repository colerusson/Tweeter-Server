package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;


public class UpdateFeedHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        StatusService statusService = new StatusService(factory);

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            String messageBody = msg.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(messageBody);

                String userAlias = jsonNode.get("userAlias").asText();
                String post = jsonNode.get("post").asText();
                long timestamp = jsonNode.get("timestamp").asLong();
                List<String> followers = objectMapper.convertValue(jsonNode.get("followers"), new TypeReference<List<String>>() {});

                statusService.updateFeed(followers, userAlias, post, timestamp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
