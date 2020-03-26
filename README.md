## Rabbitmq Poc

This poc is for study the rabbitmq concept in modules with mongodb


## Description

Strings going to "exchange" with two queues and the logic have a randons exceptions simulations when this exception was trigged the value of queue
 will go to "retry_exchange" and after 10000 milliseconds it will go to queue again to read and insert in mongodb.

