package core.mdfe.servicetwo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.rabbitmq.client.*;
import com.sun.org.apache.xpath.internal.objects.XNull;
import core.mdfe.Mdfe;
import core.mdfe.MdfeRepositoryTwo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ServiceTwo {

    private MdfeRepositoryTwo mdfeRepository;

    @Autowired
    public ServiceTwo(MdfeRepositoryTwo mdfeRepository) {
        this.mdfeRepository = mdfeRepository;
    }

    public void createConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String queueName = "servicetwo";
        String queueRetry = "retry";
        log.info("Waiting for messages. to exit press ctrl+c");


        channel.basicQos(1);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            try {
                if (ThreadLocalRandom.current().nextInt(0, 10 + 1) == 7) {
                    log.info("Xml: " + message + " Foi para fila de retry");
                    throw new RuntimeException("Cabum");
                }
                message += " NÃO ALTERADO";
                Mdfe mdfe = new Mdfe();
                mdfe.setChaveAcesso(message);
                mdfe.setDataProcessamento(LocalDate.now());
                //this.mdfeRepository.save(mdfe);
                log.info("Received in service two'" + message);


            } catch (Exception e) {

                Map<String, Object> headers = new HashMap<>();
                headers.put("x-delay", 10000);
                AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder().headers(headers);
                message += " Retry";
                channel.basicPublish("retry_exchange_two", "rabbitmq", props.build(), message.getBytes());
            }


        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}