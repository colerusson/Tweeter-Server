package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.StatusService;


public class UpdateFeedHandler implements RequestHandler<SQSEvent, Void> {
    // TODO: Use SQS messages from PostUpdateFeedMessagesHandler to update the feed
    // TODO: Call the StatusService to update the feed

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        DAOFactoryInterface factory = new DynamoDAOFactory();
        StatusService statusService = new StatusService(factory);

        // TODO: Update feeds page by page from the SQS messages
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            System.out.println(msg.getBody());
        }

        return null;
    }
}
