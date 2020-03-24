package servicethree;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ServiceThree {


    public static void createConnection() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String queueName = "servicethree";
            System.out.println("Waiting for messages. to exit press ctrl+c");

            channel.basicQos(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                message += " ALTERADO";
                System.out.println("Received in service Three'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {

        }

    }
}
