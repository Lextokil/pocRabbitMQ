package serviceone;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import serviceone.util.ConnectionFailException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Slf4j
public class ServiceOne {

    private static final String EXCHANGE_NAME = "xml";
    private static final String EXCHANGE_RETRY_TWO = "retry_exchange_two";
    private static final String EXCHANGE_RETRY_THREE = "retry_exchange_three";
    private static final String DIRECT = "direct";
    private static final String SERVICETWO = "servicetwo";
    private static final String SERVICETHREE = "servicethree";

    public static void postXmlinARow(Connection connection) {
        try {
            String key = "rabbitmq";
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, DIRECT);

            Map<String, Object> argsTwo = new HashMap<>();
            argsTwo.put("x-delayed-type", DIRECT);
            channel.exchangeDeclare(EXCHANGE_RETRY_TWO, "x-delayed-message", true, false, argsTwo);

            Map<String, Object> argsThree = new HashMap<>();
            argsThree.put("x-delayed-type", DIRECT);
            channel.exchangeDeclare(EXCHANGE_RETRY_THREE, "x-delayed-message", true, false, argsThree);

            channel.queueDeclare(SERVICETWO, true, false, false, null);
            channel.queueDeclare(SERVICETHREE, true, false, false, null);

            channel.queueBind(SERVICETWO, EXCHANGE_NAME, key);
            channel.queueBind(SERVICETHREE, EXCHANGE_NAME, key);
            channel.queueBind(SERVICETWO, EXCHANGE_RETRY_TWO, key);
            channel.queueBind(SERVICETHREE, EXCHANGE_RETRY_THREE, key);

            for (int i = 0; i < 1000; i++) {
                String message = "XML " + i;
                channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes(StandardCharsets.UTF_8));
                log.info("Enviando Xml " + i);
                Thread.sleep(6000);
            }


        } catch (Exception e) {
            log.error("Erro: " + e.getStackTrace());
            throw new ConnectionFailException(e);
        }
    }

    public static Connection createConnection() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("admin");

            return connectionFactory.newConnection();
        } catch (RuntimeException | IOException | TimeoutException e) {
            log.error("Erro: " + e.getStackTrace());
            throw new ConnectionFailException(e);
        }

    }
}
