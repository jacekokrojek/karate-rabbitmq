package examples.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class RabbitMQ {

    public static void main(String[] args) throws IOException, TimeoutException {
        RabbitMQ rabbitMQ = new RabbitMQ();
        rabbitMQ.sendMessage("Hello");
        rabbitMQ.listen(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
                rabbitMQ.stop();
            }
        });
    }

    private final static String QUEUE_NAME = "sample";
    Connection connection;
    Channel channel;

    public RabbitMQ() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("3.122.149.90");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    }

    public void sendMessage(String message) throws IOException, TimeoutException {
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }

    public void listen(java.util.function.Consumer<String> handler) throws IOException, TimeoutException {

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            handler.accept(message);
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

    }

    public void stop() {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
