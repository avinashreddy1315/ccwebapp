package com.csye6225.businessLogics;

import com.amazonaws.services.sns.AmazonSNSAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;

public class EmailAndPasswordLogics {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AmazonSNSAsync amazonSNSClient;


    /**
     * Method to Validate is the email id is a Valid email Id
     *
     * @param email
     * @return True/False
     */
    public boolean ValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";


        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Method to check if the entered Password is acceptable Password or not
     *
     * @param password
     * @return True/False
     */
    public boolean ValidPassword(String password) {
        String regex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    public void sendMessage(String emailId) throws ExecutionException, InterruptedException {

        logger.info("Sending Message - {} ", emailId);

        String topicArn = getTopicArn("reset_password");

        PublishRequest publishRequest = new PublishRequest(topicArn, emailId);
        logger.info("publish");
        Future<PublishResult> publishResultFuture = amazonSNSClient.publishAsync(publishRequest);
        String messageId = publishResultFuture.get().getMessageId();
        logger.info(messageId);

        logger.info("Send Message {} with message Id {} ", emailId, messageId);

    }

    public String getTopicArn(String topicName) {
        //logger.info(topicArn);
        String topicArn = null;

        try {
            Topic topic = amazonSNSClient.listTopicsAsync().get().getTopics().stream()
                    .filter(t -> t.getTopicArn().contains(topicName)).findAny().orElse(null);
            logger.info(topic.toString());

            if (null != topic) {
                topicArn = topic.getTopicArn();
                logger.info(topicArn);
            } else {
                logger.info("No Topic found by the name : ", topicName);
            }

        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }

        logger.info("Arn corresponding to topic name {} is {} ", topicName, topicArn);

        return topicArn;



    }
}