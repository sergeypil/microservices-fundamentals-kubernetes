package net.serg.resourceservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AwsConfig {

    @Bean
    //@Profile("local")
    AWSCredentialsProvider localAwsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials("foo", "bar"));
    }

    @Bean
    @Profile("local")
    public AmazonS3 amazonS3local(AWSCredentialsProvider credentialsProvider) {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", Regions.US_EAST_1.getName()))
            .withPathStyleAccessEnabled(true)
            .withCredentials(credentialsProvider)
            .build();
    }

    @Bean
    @Profile("compose")
    public AmazonS3 amazonS3(AWSCredentialsProvider credentialsProvider) {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localstack:4566", Regions.US_EAST_1.getName()))
            .withPathStyleAccessEnabled(true)
            .withCredentials(credentialsProvider)
            .build();
    }
    
}
