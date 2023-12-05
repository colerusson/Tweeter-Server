package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.daoInterface.FeedDAOInterface;
import edu.byu.cs.tweeter.server.daoInterface.StoryDAOInterface;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.util.Pair;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class StatusService {
    private final StoryDAOInterface storyDAO;
    private final FeedDAOInterface feedDAO;

    public StatusService(DAOFactoryInterface factory) {
        this.storyDAO = factory.getStoryDAO();
        this.feedDAO = factory.getFeedDAO();
    }

    public FeedResponse getFeed(FeedRequest request) {
        if (request.getUserAlias() == null) {
            return new FeedResponse("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            return new FeedResponse("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<Status>, Boolean> pair = feedDAO.getFeed(request.getUserAlias(), request.getLimit(), request.getLastPostTime());
        return new FeedResponse(pair.getFirst(), pair.getSecond());
    }

    public StoryResponse getStory(StoryRequest request) {
        if (request.getUserAlias() == null) {
            return new StoryResponse("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            return new StoryResponse("[Bad Request] Request needs to have a positive limit");
        }

        Pair<List<Status>, Boolean> pair = storyDAO.getStory(request.getUserAlias(), request.getLimit(), request.getLastPostTime());
        return new StoryResponse(pair.getFirst(), pair.getSecond());
    }

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if (request == null) {
            return new PostStatusResponse(false, "[Bad Request] Request needs to have a status");
        } else if (request.getUserAlias() == null) {
            return new PostStatusResponse(false, "[Bad Request] Request needs to have a user");
        } else if (request.getPost() == null) {
            return new PostStatusResponse(false, "[Bad Request] Request needs to have a user alias");
        } else if (request.getTimestamp() == 0) {
            return new PostStatusResponse(false, "[Bad Request] Request needs to have a message");
        }

        Boolean result = storyDAO.postStatus(request.getUserAlias(), request.getPost(), request.getTimestamp());
        if (result) {
            // TODO: Send SQS message to post to feed
            String messageBody = "Change this message body as needed";
            String queueUrl = "https://sqs.us-west-2.amazonaws.com/379683941185/postStatusQueue";

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(messageBody);

            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
            SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);

            String msgId = send_msg_result.getMessageId();
            System.out.println("Message ID: " + msgId);

            return new PostStatusResponse(true);
        }
        return new PostStatusResponse(false, "Could not post status");
    }

    public void postFeed() {
        // TODO: Use SQS messages from PostUpdateFeedMessagesHandler to update the feed
        // TODO: Call the feedDAO to update the feed
    }
}
