package org.ra;

import org.apache.kafka.common.errors.PolicyViolationException;
import org.apache.kafka.server.policy.CreateTopicPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternMatchingCreateTopicPolicy implements CreateTopicPolicy {

  private static final String TOPIC_PATTERN_KEY = "ra.topic.pattern";
  private static final String EXCLUDE_SYS_KEY = "ra.sys.exclude";
  private static final Logger logger = LoggerFactory.getLogger(PatternMatchingCreateTopicPolicy.class);

  private Pattern pattern;
  private Boolean ExcludeSys = true;

  @Override
  public void validate(RequestMetadata requestMetadata) throws PolicyViolationException {
    String topic = requestMetadata.topic();

    if (pattern != null) {
      if (ExcludeSys && topic.startsWith("_")) {
        return;
      }
      logger.debug("Checking '{}' topic name against the pattern configured: {}", topic, pattern);
      if (!pattern.matcher(topic).matches()) throw new PolicyViolationException(String.format(
        "Topic name '%s' does not match the pattern '%s'", topic, pattern));
    } else {
      logger.warn("Not checking '{}' topic name against any pattern, because none was configured", topic);
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
    try {
      pattern = Pattern.compile(topicPattern.toString());
    }
    catch(PatternSyntaxException e) {
      logger.error("pattern compile failed", e);
    }

    Object excludeSys = configs.get(EXCLUDE_SYS_KEY);
    try {
      if (excludeSys!=null) {
        ExcludeSys = isTrue(excludeSys.toString());
      }
    }catch (Exception e) {
      logger.error("cant convert value {} for key {} to bool" ,excludeSys.toString(), EXCLUDE_SYS_KEY);
    }
  }

  @Override
  public void close() {
    logger.info("Closing {} instance", PatternMatchingCreateTopicPolicy.class);
  }

  public static boolean isTrue(String result) {
    String val = result.toLowerCase(Locale.ENGLISH);
    if (val.equals("true") || val.equals("yes") || val.equals("y") || val.equals("1")) {
      return true;
    }
    if (val.equals("false") || val.equals("no") || val.equals("n") || val.equals("0")) {
      return false;
    }
    throw new IllegalArgumentException("Bad boolean value: " + result);
  }
}
