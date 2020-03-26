package serviceone;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
public class ServiceOne {

    public static final String EXCHANGE_NAME = "xml";
    public static final String EXCHANGE_RETRY_TWO = "retry_exchange_two";
    public static final String EXCHANGE_RETRY_THREE = "retry_exchange_three";

    public static void postXmlinARow(Connection connection) {
        try {
            String key = "rabbitmq";
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            Map<String, Object> argsTwo = new HashMap<>();
            argsTwo.put("x-delayed-type", "direct");
            channel.exchangeDeclare(EXCHANGE_RETRY_TWO, "x-delayed-message", true, false, argsTwo);

            Map<String, Object> argsThree = new HashMap<>();
            argsThree.put("x-delayed-type", "direct");
            channel.exchangeDeclare(EXCHANGE_RETRY_THREE, "x-delayed-message", true, false, argsThree);

            channel.queueDeclare("servicetwo", true, false, false, null);
            channel.queueDeclare("servicethree", true, false, false, null);

            channel.queueBind("servicetwo",EXCHANGE_NAME, key);
            channel.queueBind("servicethree",EXCHANGE_NAME, key);
            channel.queueBind("servicetwo",EXCHANGE_RETRY_TWO, key);
            channel.queueBind("servicethree",EXCHANGE_RETRY_THREE, key);

            for (int i = 0; i <1000  ; i++) {
                String message = "XML "+i ;
                channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes("UTF-8"));
                log.info("Enviando Xml "+i);
                Thread.sleep(6000);
            }


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
