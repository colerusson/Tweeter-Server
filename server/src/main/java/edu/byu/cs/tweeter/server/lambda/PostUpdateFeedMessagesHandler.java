package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {
    // TODO: Use Follow Service to get all the followers of the user and page them
    // TODO: Use SQS to send messages to the UpdateFeedHandler for each page

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        FollowService followService = new FollowService(factory);

        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println(msg.getBody());
        }

        // TODO: Populate a list of followers using the FollowService

        // TODO: Send SQS messages to UpdateFeedHandler for each page
        String messageBody = "Change This Message Body";
        String queueUrl = "https://sqs.us-west-2.amazonaws.com/379683941185/updateFeedQueue";

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

        String msgId = send_msg_result.getMessageId();
        System.out.println("Message ID: " + msgId);

        return null;
    }
}
