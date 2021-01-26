package auth.controller;

import auth.client.LocalQueueClient;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageReceiver;
import com.microsoft.azure.servicebus.IMessageSender;
import com.microsoft.azure.servicebus.Message;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TopicReceiveController {

    @Autowired
    LocalQueueClient localQueueClient;

    @GetMapping("/messages")
    @ApiOperation(value = "message")
    public void runs() {

        try {
            IMessageSender iMessageSender = localQueueClient.init();
            Message message1 = new Message("message");
            message1.setMessageId("1");
            iMessageSender.send(message1);

            IMessageReceiver receiver = localQueueClient.getMessage();
            IMessage message = receiver.receive();
            System.out.println(message);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
