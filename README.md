Библиотека позволяет проверять создаваемые топики в Kafka на соотвествие регулярному выражению. 

Использование
=============

Server.properties
```
create.topic.policy.class.name=org.ra.PatternMatchingCreateTopicPolicy
ra.topic.pattern=^000-1(-\\d{3}-\\d)?\\.[a-z0-9-]+\\.(db|cdc|bin|cmd|sys|log|tmp|ipc)\\.[a-z0-9-.]+\\.\\d+$
```

Docker:
```
KAFKA_CREATE_TOPIC_POLICY_CLASS_NAME: org.ra.PatternMatchingCreateTopicPolicy
KAFKA_RA_TOPIC_PATTERN: ^999-9(-\\d{3}-\\d)?\\.[a-z0-9-]+\\.(db|cdc|bin|cmd|sys|log|tmp|ipc)\\.[a-z0-9-.]+\\.\\d+$$
```

Полезные ссылки
===============

* https://dzone.com/articles/kafka-topics-naming
* https://cwiki.apache.org/confluence/display/KAFKA/KIP-108%3A+Create+Topic+Policy