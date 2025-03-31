package com.scentbird.tictactoe.tictactoe.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.containers.KafkaContainer;

@SpringBootTest
@TestPropertySource(properties = {
        "FIRST_TURN_SPRING_KAFKA_CONSUMER_GROUP_ID=first-turn-claim-group-id-1",
        "GAME_MOVE_SPRING_KAFKA_CONSUMER_GROUP_ID=game-move-group-id-1",
        "SERVER_PORT=8090",
        "SPRING_APPLICATION_NAME=instance1",
        "SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS=localhost:9092",
        "SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=localhost:9092",
        "TIC_TAC_TOE_MOVE_DELAY=5000"
})
class KafkaPostgresIntegrationTest {

    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    static {
        postgresContainer.start();
        kafkaContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // PostgreSQL properties
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        // Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @Test
    void integrationTest() {
        // Perform integration tests involving both the database and Kafka
    }
}
