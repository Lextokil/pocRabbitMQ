package core.servicethree;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import core.servicethree.mdfe.Mdfe;
import core.servicethree.mdfe.MdfeServiceThree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class ServiceThree {

    private final MdfeServiceThree mdfeServiceThree;

    @Autowired
    public ServiceThree(MdfeServiceThree mdfeServiceThree) {
        this.mdfeServiceThree = mdfeServiceThree;
    }


    public  void createConnection() {
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
                Mdfe mdfe = new Mdfe();
                mdfe.setChaveAcesso(message);
                mdfe.setDataProcessamento(LocalDate.now());
                mdfe.setCod(UUID.randomUUID().toString());
                this.mdfeServiceThree.save(mdfe);
                System.out.println("Received in service Three'" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {

        }

    }
}
