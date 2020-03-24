package serviceone;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class ServiceOne {

    public static final String EXCHANGE_NAME = "xml";

    public static void postXmlinARow(Connection connection, int num) {
        try {
            log.info("Enviando Xml "+num);
            String key = "rabbitmq";
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String message = "XML "+num ;
            channel.queueDeclare("servicetwo", true, false, false, null);
            channel.queueDeclare("servicethree", true, false, false, null);
            channel.queueBind("servicetwo",EXCHANGE_NAME, key);
            channel.queueBind("servicethree",EXCHANGE_NAME, key);
            channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes("UTF-8"));
            
        } catch (Exception e) {

        }
    }

    public static Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Connection connection = connectionFactory.newConnection();
        return connection;

    }
}
