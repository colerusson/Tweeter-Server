package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.server.daoInterface.ImageDAOInterface;


public class ImageS3DAO implements ImageDAOInterface {
    AmazonS3 s3 = AmazonS3ClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();

    @Override
    public String uploadImage(String imageUrl, String alias) {
        byte[] imageBytes = Base64.getDecoder().decode(imageUrl);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        metadata.setContentType("image/jpeg");
        PutObjectRequest request = new PutObjectRequest("crussonbucket", alias, new ByteArrayInputStream(imageBytes), metadata).withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
        return "https://crussonbucket.s3.us-west-2.amazonaws.com/" + alias;
    }
}
