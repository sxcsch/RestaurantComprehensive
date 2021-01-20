package callsqueue.controller;

import callsqueue.config.OptimizeConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RefreshScope
@Api(tags = { "msg" })
public class SendController {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private OptimizeConfig optimizeConfig;

    @Autowired
    SendQueue sendQueue;

    @GetMapping("/messages")
    @ApiOperation(value = "message")
    public String postMessage(String  req) {
        log.info("Sending message");
        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) jmsTemplate.getConnectionFactory();
        connectionFactory.setCacheConsumers(false);
        connectionFactory.setCacheProducers(false);
        jmsTemplate.convertAndSend(optimizeConfig.getOptimizeQueueName(), req);
        sendQueue.sendMessage();
        try{
            sendQueue.receiveMessages();
        }catch (Exception e){
            e.printStackTrace();
        }

        return req;
    }
}
