package com.scentbird.tictactoe.tictactoe.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    @Value("${tic-tac-toe.kafka.first-turn-claim.topic.name}")
    private String firstTurnClaimTopicName;

    @Value("${tic-tac-toe.kafka.game-move.topic.name}")
    private String gameMoveTopicName;

    @Bean
    public NewTopic firstTurnClaimNewTopic() {
        return TopicBuilder.name(firstTurnClaimTopicName)
                .partitions(3)
                .replicas(1)
                .config("min.insync.replicas", "1")
                .build();
    }

    @Bean
    public NewTopic gameMoveNewTopic() {
        return TopicBuilder.name(gameMoveTopicName)
                .partitions(3)
                .replicas(1)
                .config("min.insync.replicas", "1")
                .build();
    }
}
