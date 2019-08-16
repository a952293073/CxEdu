package com.xuecheng.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBoot {
    @RabbitListener(queues = {"queue_inform_sms"})
    public void smsTest(String msg){
        System.out.println(msg);
    }
}
