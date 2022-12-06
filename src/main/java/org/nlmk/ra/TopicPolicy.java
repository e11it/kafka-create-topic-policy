package org.nlmk.ra;

import org.apache.kafka.common.errors.PolicyViolationException;
import org.apache.kafka.server.policy.CreateTopicPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;


public class TopicPolicy implements CreateTopicPolicy {
    private final Properties defaults;
    private final Logger logger = LoggerFactory.getLogger(TopicPolicy.class.toString());

    //private final static String TopicPattern = "\\w+\\.{1}\\w+";
    private final static String TopicPattern = Config;

    @Override
    public void validate(RequestMetadata requestMetadata) throws PolicyViolationException {
        StringBuilder bd = new StringBuilder().append(" Topic Name=").append(requestMetadata.topic());
        logger.info(bd.toString());
        if (requestMetadata.topic().isEmpty() || !Pattern.matches(TopicPattern, requestMetadata.topic())) {
            throw new PolicyViolationException("Topic name " + requestMetadata.topic() + " should match the pattern " + TopicPattern);
        }
    }

    @Override
    public void close() throws Exception {
        logger.info(" Close & release.");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        if (configs != null) {
            for (String k : configs.keySet()) {
                logger.info(configs.get(k).toString());
            }
        }
    }
}