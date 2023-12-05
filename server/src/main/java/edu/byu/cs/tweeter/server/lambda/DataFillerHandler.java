package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.server.bean.FollowBean;
import edu.byu.cs.tweeter.server.bean.UserBean;
import edu.byu.cs.tweeter.server.factory.DAOFactoryInterface;
import edu.byu.cs.tweeter.server.factory.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.service.UserService;

public class DataFillerHandler implements RequestHandler<Void, Void> {
    @Override
    public Void handleRequest(Void input, Context context) {
        int numUsers = 50;
        String followTarget = "@Passoff";

        DAOFactoryInterface factory = new DynamoDAOFactory();

        UserService userService = new UserService(factory);
        FollowService followService = new FollowService(factory);

        List<UserBean> users = new ArrayList<>();
        List<FollowBean> followers = new ArrayList<>();

        for (int i = 1; i <= numUsers; i++) {
            String firstName = "First " + i;
            String lastName = "Last " + i;
            String alias = "@user" + i;

            UserBean user = new UserBean();
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setAlias(alias);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode("Password123");
            user.setPassword(hashedPassword);

            user.setImage_url("https://crussonbucket.s3.us-west-2.amazonaws.com/%40Passoff");
            users.add(user);

            FollowBean follower = new FollowBean();
            follower.setFollower_alias(followTarget);
            follower.setFollowee_alias(alias);
            followers.add(follower);
        }

        userService.fillDatabase(users);
        followService.fillDatabase(followers);

        return null;
    }
}
