import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageReceiver;
import com.microsoft.azure.servicebus.IMessageSender;
import com.microsoft.azure.servicebus.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import simple.SimpleApplication;
import simple.client.LocalQueueClient;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = SimpleApplication.class)
public class SpringBootJunitTestcase {

    @Autowired
    LocalQueueClient localQueueClient;

    @Test
    public void test(){
        try {
            IMessageReceiver receiver = localQueueClient.getMessage();
            IMessage message = receiver.receive();

            IMessageSender iMessageSender = localQueueClient.init();
            Message message1 = new Message("message");
            message1.setMessageId("1");
            iMessageSender.send(message1);
//            MessagingFactory messagingFactory = localQueueClient.getAsync();
//            TransactionContext transaction = messagingFactory.startTransactionAsync().get();
//            receiver.complete(message.getLockToken(), transaction);



            System.out.println(message.getMessageBody().getValueData().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}