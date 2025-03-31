

## About the project

This project is a working Spring Boot Java application of the tic-tac-toe game running in a two-node setup, designed specifically to demonstrate resilience fault tolerance principles in distributed systems.



Here is a little demo 

![me](https://github.com/patterns-vault/tic-tac-toe/blob/master/gif_demo.gif)

## Installation 

#### 1. Docker compose

You can simply run: 

```shell
docker compose up
```

Two UIs to the two nodes will be available at 

`http://localhost:8090/`
and
http://localhost:8091/

#### 2. IDE Run

If you already have running Kafka and Postgres (make sure that 'tic_tac_toe' Postgres database is created), you can create two Spring Boot Run Configurations and run them at the same time, emulating two nodes. But you need to provide different sets of environment variables. Here they are:

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

The seconc instance 

```env vars
FIRST_TURN_SPRING_KAFKA_CONSUMER_GROUP_ID=first-turn-claim-group-id-2;
GAME_MOVE_SPRING_KAFKA_CONSUMER_GROUP_ID=game-move-group-id-2;
SERVER_PORT=8091;
SPRING_APPLICATION_NAME=instance2;
SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS=localhost:9092;
SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=localhost:9092;
TIC_TAC_TOE_MOVE_DELAY=5000
```
