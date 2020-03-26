package core.servicethree;

import com.rabbitmq.client.*;
import core.servicethree.mdfe.Mdfe;
import core.servicethree.mdfe.MdfeServiceThree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ServiceThree {

    private final MdfeServiceThree mdfeServiceThree;

    @Autowired
    public ServiceThree(MdfeServiceThree mdfeServiceThree) {
        this.mdfeServiceThree = mdfeServiceThree;
    }


    public void createConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "servicethree";
        log.info("Waiting for messages. to exit press ctrl+c");

        channel.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            try {
                if (ThreadLocalRandom.current().nextInt(0, 10 + 1) == 7) {
                    log.info("Xml: " + message + " Foi para fila de retry");
                    throw new RuntimeException("Cabum");
                }
                message += " ALTERADO";
                Mdfe mdfe = new Mdfe();
                mdfe.setChaveAcesso(message);
                mdfe.setDataProcessamento(LocalDate.now());
                mdfe.setCod(UUID.randomUUID().toString());
                this.mdfeServiceThree.save(mdfe);
                log.info("Received in service Three'" + message);
            } catch (Exception e) {
                Map<String, Object> headers = new HashMap<>();
                headers.put("x-delay", 10000);
                AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
                message += " Retry";
                channel.basicPublish("retry_exchange_three", "rabbitmq", props.build(), message.getBytes());
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }
}
