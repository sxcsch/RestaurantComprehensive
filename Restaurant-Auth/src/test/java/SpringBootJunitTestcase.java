import auth.AuthApplication;
import auth.client.LocalQueueClient;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageReceiver;
import com.microsoft.azure.servicebus.IMessageSender;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TransactionContext;
import com.microsoft.azure.servicebus.primitives.MessagingFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = AuthApplication.class)
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