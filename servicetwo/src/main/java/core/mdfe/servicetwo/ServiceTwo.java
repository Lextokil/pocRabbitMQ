package core.mdfe.servicetwo;

import com.rabbitmq.client.*;
import core.mdfe.Mdfe;
import core.mdfe.MdfeRepositoryTwo;
import core.mdfe.util.CabumException;
import core.mdfe.util.ConnectionFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class ServiceTwo {

    private MdfeRepositoryTwo mdfeRepository;

    @Autowired
    public ServiceTwo(MdfeRepositoryTwo mdfeRepository) {
        this.mdfeRepository = mdfeRepository;
    }

    public void createConnection() {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("localhost");
            connectionFactory.setUsername("admin");
            connectionFactory.setPassword("admin");
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            String queueName = "servicetwo";
            log.info("Waiting for messages. to exit press ctrl+c");


            channel.basicQos(1);
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                try {
                    if (ThreadLocalRandom.current().nextInt(0, 10 + 1) == 7) {
                        log.info("Xml: " + message + " Foi para fila de retry");
                        throw new CabumException("Cabum");
                    }
                    message += " NÃO ALTERADO";
                    Mdfe mdfe = new Mdfe();
                    mdfe.setChaveAcesso(message);
                    mdfe.setDataProcessamento(LocalDate.now());
                    this.mdfeRepository.save(mdfe);
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
        } catch (Exception e) {
            log.error("Erro: " + e.getStackTrace());
            throw new ConnectionFailException(e);
        }

    }
}