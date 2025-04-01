
## About the project

This project is a working Spring Boot Java application of the tic-tac-toe game running in a two-node setup, designed specifically to demonstrate resilience and fault tolerance principles in distributed systems.

The application is designed to be run as a two-node system, with each node representing a player in the game. Although both nodes run the same application code, they act as different players based on their configuration.

## How It Works

1. **Dual-Instance Architecture**: The same application runs as two separate instances, differentiated by a different sets of environment variables. Two nodes must be started at the same time.
2. **Initial Handshake**: Nodes exchange initial messages (called `firstTurnClaims` in the context of the application) through a `first-turn-claim-topic` Kafka topic to establish which node takes the first turn.
3. **Game Progression**: Nodes alternate turns by exchanging messages through a `game-move-topic` Kafka topic, maintaining the game state in a `game_move` Postgres database table.
4. After the game is finished (either by the virtue of the fact that one instance wins or that it's a draw - no more move on a board left) the cycle is repeated.

# Technical Stack

    Spring Boot
    Java 
    Postgres
    Kafka
    JavaScript


# Demo

![me](https://github.com/patterns-vault/tic-tac-toe/blob/master/gif_demo.gif)

## Installation

#### 1. Docker compose

You can simply run:

```shell  
docker compose up
```  

Two UIs to the two instances will be available at

`http://localhost:8090/`  
and  
`http://localhost:8091/`

#### 2. IDE Run

If you already have running Kafka and Postgres (make sure that `tic_tac_toe` Postgres database is created), you can create two Spring Boot Run Configurations and run them at the same time, emulating two nodes. But you need to provide different sets of environment variables. Here they are:

The first instance

```env vars  
FIRST_TURN_SPRING_KAFKA_CONSUMER_GROUP_ID=first-turn-claim-group-id-1;  
GAME_MOVE_SPRING_KAFKA_CONSUMER_GROUP_ID=game-move-group-id-1;  
SERVER_PORT=8090;  
SPRING_APPLICATION_NAME=instance1;  
SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS=localhost:9092;  
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=localhost:9092;  
TIC_TAC_TOE_MOVE_DELAY=5000  
```  

The second instance

```env vars  
FIRST_TURN_SPRING_KAFKA_CONSUMER_GROUP_ID=first-turn-claim-group-id-2;  
GAME_MOVE_SPRING_KAFKA_CONSUMER_GROUP_ID=game-move-group-id-2;  
SERVER_PORT=8091;  
SPRING_APPLICATION_NAME=instance2;  
SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS=localhost:9092;  
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=localhost:9092;  
TIC_TAC_TOE_MOVE_DELAY=5000  
```

Two UIs to the two instances will be available at

`http://localhost:8090/`  
and  
`http://localhost:8091/`

# Architectural Decision Records

1. **Message Broker.** Early in the project it was decided that Kafka messaging way is a superior to REST API requests exchange with Circuit Breaker mechanism due to several reasons.
    1. Asynchronous communication is preferrable here. It will allow the application to be free from being blocked while waiting for a response and be decoupled from the failures of consumers.
    2. Using Kafka avoids many of the complexities associated with error handling, retries, fallback and  circuit breaker mechanisms, which would otherwise require explicit coding in a traditional REST API approach.
    3. Kafka enables multiple microservices to consume messages independently from the same topic if they need the same data.
2. **Stateless service.** It was decided to make the application stateless, meaning all state is stored in the database. This approach enhances fault tolerance—if the microservice fails, all state remains preserved in the database, allowing the microservice to resume execution smoothly from the point of failure. Another benefit is that if horizontal scaling becomes necessary, the application can be scaled effortlessly.
3. **Kafka Multi-Consumer Pattern.** Using both a database and Kafka presents certain challenges. One possible issue is that an event may be successfully stored in the database but fail to be sent to Kafka due to an error. A common solution used to avoid such problems is the so-called "transactional outbox pattern". However, to avoid the added complexity and potential overengineering in this context, a **multi-consumer approach** was chosen instead. In our context, a multi-consumer approach ensures that a message is considered committed only upon successful consumption from the broker, rather than at the moment of sending. This eliminates the need for additional recovery logic in case of transmission failures, resulting in a simpler solution. It means that two instances consume the same message and process it according to their specific logic.
4. **Single codebase - different nodes.** To avoid code duplication, it was decided from the beginning to maintain a single codebase, where instances can be instantiated with different environment variables, allowing them to assume different roles. This design is common in distributed systems—for example, in Kubernetes, where master and worker nodes can switch roles based on a leader election mechanism. A similar approach is used in Elasticsearch, which has control nodes, data nodes, and master nodes and if needed they can assume different roles.

# Roadmap

Here is a list of possible future improvements:

1. Replace REST API - JavaScript UI once-a-second requests with WebSocket or RSocket communication.
2. Enforce strict separation of application layers: - persistence layer, Kafka communication layer, service logic layer. Currently, JPA entity objects are sent and received via Kafka without conversion - they are reused. Introducing dedicated Kafka outbound and inbound request models would improve decoupling and maintainability.
3. For performance optimization, introduce an in-memory Caffeine cache to store instance role data, which is currently fetched from the database on every execution of the scheduled job.
4. Add logic to prevent setting an instance name that is already in use, ensuring that each instance has a unique name. Currently, there is no mechanism in place to prevent two instances from having the same name, which could lead to conflicts and issues with the execution of game logic.

# Functional Requirements Fulfillment Summary 
