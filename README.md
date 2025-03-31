
## About the project

This project is a working Spring Boot Java application of the tic-tac-toe game running in a two-node setup, designed specifically to demonstrate resilience and fault tolerance principles in distributed systems.

The application is designed to be run as a two-node system, with each node representing a player in the game. Although both nodes run the same application code, they act as different players based on their configuration.

## How It Works

1. **Dual-Instance Architecture**: The same application runs as two separate instances, differentiated by environment variables. Two nodes must be started at the same time.
2. **Initial Handshake**: Nodes exchange initial messages (called `firstTurnClaims` in the context of the application) through a `first-turn-claim-topic` Kafka topic to establish which node takes the first turn.
3. **Game Progression**: Nodes alternate turns by exchanging messages through a `game-move-topic` Kafka topic, maintaining the game state in a `game_move` Postgres database table.
4. After the game is finished (either by the virtue of the fact that one instance wins or that it's a draw - no more move on a board left) the cycle is repeated.

# Technical Stack

    Spring Boot
    Java 
    Postgres
    Kafka
    JavaScript


Here is a little demo

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

# Architectural Decision Records

1. Kafka messaging, all the message exchanges consider finalized only when they are consumed from Kafka topics
2. Kafka Multi-Consumer Pattern:
3. Stateless - store all the state in a database making it resilient to instance failures. Every instance writes data in its own schema which is the same as an instance's name. 
