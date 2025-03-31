

Here is a little demo 

![me](https://github.com/patterns-vault/tic-tac-toe/blob/master/gif_demo.gif)


To run the first instance, use: 

```shell
java -jar build/libs/tic-tac-toe-0.0.1-SNAPSHOT.jar \
 --server.port=8090 \
 --spring.application.name=instance1 \
 --spring.kafka.consumer.first-turn-claim.group-id=first-turn-claim-group-id-1 \
 --spring.kafka.consumer.game-move.group-id=game-move-group-id-1

```
To run the second instance, use:

```shell
 java -jar build/libs/tic-tac-toe-0.0.1-SNAPSHOT.jar \
 --server.port=8091 \
 --spring.application.name=instance2 \
 --spring.kafka.consumer.first-turn-claim.group-id=first-turn-claim-group-id-2 \
 --spring.kafka.consumer.game-move.group-id=game-move-group-id-2

```
