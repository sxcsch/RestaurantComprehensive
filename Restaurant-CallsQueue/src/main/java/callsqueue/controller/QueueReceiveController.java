package callsqueue.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QueueReceiveController {

    @JmsListener(destination = "${optimize.error-queue-name}", containerFactory = "topicJmsListenerContainerFactory"
                    , subscription = "${optimize.error-queue-name}")
    public void receiveMessage(String user) {
        log.error("Received message: {}", user);
    }
}
