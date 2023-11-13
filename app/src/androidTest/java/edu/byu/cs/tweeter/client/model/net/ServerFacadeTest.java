package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeTest {
    private ServerFacade serverFacade;
    private AuthToken authToken;

    @BeforeEach
    void setup() {
        serverFacade = new ServerFacade();
        authToken = new AuthToken();
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws Exception {
         RegisterRequest request = new RegisterRequest("First", "Last", "@username", "password",
                 "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
         String urlPath = "/register";
         RegisterResponse response = serverFacade.register(request, urlPath);
         Assertions.assertNotNull(response);
         Assertions.assertNull(response.getMessage());
         Assertions.assertTrue(response.isSuccess());
         Assertions.assertNotNull(response.getUser());
         Assertions.assertNotNull(response.getAuthToken());
         Assertions.assertEquals("@allen", response.getUser().getAlias());
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws Exception {
        FollowersRequest request = new FollowersRequest(authToken, "@allen", 10, null);
        String urlPath = "/getfollowers";
        FollowersResponse response = serverFacade.getFollowers(request, urlPath);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getMessage());
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNotNull(response.getFollowers());
        Assertions.assertEquals(10, response.getFollowers().size());
        Assertions.assertTrue(response.getHasMorePages());
    }

    @Test
    public void testGetFollowersCount_validRequest_correctResponse() throws Exception {
        FollowersCountRequest request = new FollowersCountRequest(authToken, "@allen");
        String urlPath = "/getfollowerscount";
        FollowersCountResponse response = serverFacade.getFollowersCount(request, urlPath);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getMessage());
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(20, response.getCount());
    }

    @Test
    public void testGetFollowingCount_validRequest_correctResponse() throws Exception {
        FollowingCountRequest request = new FollowingCountRequest(authToken, "@allen");
        String urlPath = "/getfollowingcount";
        FollowingCountResponse response = serverFacade.getFollowingCount(request, urlPath);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getMessage());
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(20, response.getCount());
    }
}
