package callsqueue.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RefreshScope
@Api(tags = { "msg" })
public class SendController {

//    @Autowired
//    private JmsTemplate jmsTemplate;
//
//    @Autowired
//    private OptimizeConfig optimizeConfig;

    @Autowired
    TopicReceiveController controller;

    @Autowired
    SendQueue sendQueue;

    @GetMapping("/messages")
    @ApiOperation(value = "message")
    public String postMessage(String  req) {
//        CachingConnectionFactory connectionFactory = (CachingConnectionFactory) jmsTemplate.getConnectionFactory();
//        connectionFactory.setCacheProducers(false);
//        connectionFactory.setCacheConsumers(false);
//        jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
//        jmsTemplate.setMessageTimestampEnabled(true);
//        jmsTemplate.setPubSubNoLocal(false);
//        jmsTemplate.setSessionTransacted(true);
//        jmsTemplate.convertAndSend(optimizeConfig.getOptimizeQueueName(), req);
        try{
            sendQueue.runs();
//            controller.runs();
        }catch (Exception e){
            e.printStackTrace();
        }

        return req;
    }
}
