package org.ra;

import org.apache.kafka.common.errors.PolicyViolationException;
import org.apache.kafka.server.policy.CreateTopicPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

public class PatternMatchingCreateTopicPolicy implements CreateTopicPolicy {

  private static final String TOPIC_PATTERN_KEY = "ra.topic.pattern";
  private static final Logger logger = LoggerFactory.getLogger(PatternMatchingCreateTopicPolicy.class);

  private Pattern pattern;

  @Override
  public void validate(RequestMetadata requestMetadata) throws PolicyViolationException {
    String topic = requestMetadata.topic();
    if (pattern != null) {
      logger.info("Checking '{}' topic name against the pattern configured: {}", topic, pattern);
      if (!pattern.matcher(topic).matches()) throw new PolicyViolationException(String.format(
        "Topic name '%s' does not match the pattern '%s'", topic, pattern));
    } else {
      logger.info("Not checking '{}' topic name against any pattern, because none was configured", topic);
    }
  }

  @Override
  public void configure(Map<String, ?> configs) {
    if (configs == null) {
      logger.warn("Config map is null");
      return;
    }
    Object topicPattern = configs.get(TOPIC_PATTERN_KEY);
    if (topicPattern == null) {
      logger.warn("No entry with '{}' key was found in config provided: {}", TOPIC_PATTERN_KEY, configs);
      return;
    }
    logger.info("Compiling pattern from value provided: {}", topicPattern);
    pattern = Pattern.compile(topicPattern.toString());
  }

  @Override
  public void close() {
    logger.info("Closing {} instance", PatternMatchingCreateTopicPolicy.class);
  }
}
