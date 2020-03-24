package core.mdfe.servicetwo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.extern.slf4j.Slf4j;
import core.mdfe.Mdfe;
import core.mdfe.MdfeRepositoryTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
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
        System.out.println("Waiting for messages. to exit press ctrl+c");

        channel.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            message += " NÃO ALTERADO";
            Mdfe mdfe = new Mdfe();
            mdfe.setChaveAcesso(message);
            mdfe.setDataProcessamento(LocalDate.now());
            this.mdfeRepository.save(mdfe);
            System.out.println("Received in service two'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }

}
