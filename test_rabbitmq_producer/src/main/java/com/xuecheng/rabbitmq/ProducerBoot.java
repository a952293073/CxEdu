package com.xuecheng.rabbitmq;

import com.xuecheng.config.ProducerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProducerBoot {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void sendMsg(){
        String msg="send message to consumer";
        rabbitTemplate.convertAndSend(ProducerConfig.EXCHANGE_TOPICS_INFORM,"inform.sms",msg);
    }

}
