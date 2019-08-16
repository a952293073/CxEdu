package com.xuecheng.rabbitmq;

import com.rabbitmq.client.*;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_FANOUT_INFORM = "exchange_fanout_inform";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.203.22");
            factory.setPort(5672);
            factory.setUsername("user");
            factory.setPassword("user");
            factory.setVirtualHost("/");
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_FANOUT_INFORM, BuiltinExchangeType.FANOUT);

            channel.queueDeclare(QUEUE_INFORM_SMS, true, false, false, null);
            channel.queueBind(QUEUE_INFORM_SMS, EXCHANGE_FANOUT_INFORM, "");

            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    long deliveryTag = envelope.getDeliveryTag();
                   String exchange = envelope.getExchange();
                    String message = new String(body,"utf-8");
                    System.out.println("receive:"+message);
                }
            };
            channel.basicConsume(QUEUE_INFORM_SMS,true,defaultConsumer);


    }
}
