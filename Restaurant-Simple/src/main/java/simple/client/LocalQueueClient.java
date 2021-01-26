package simple.client;

import com.microsoft.azure.servicebus.ClientFactory;
import com.microsoft.azure.servicebus.ClientSettings;
import com.microsoft.azure.servicebus.IMessageReceiver;
import com.microsoft.azure.servicebus.IMessageSender;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.MessagingFactory;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.microsoft.azure.servicebus.primitives.TransportType;
import com.microsoft.azure.servicebus.primitives.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @author shenxiaochen (sxcsch@163.com)
 * @date 2021/01/26
 */
@Service
public class LocalQueueClient {
    @Autowired
    private Environment environment;

    private final String queue = "dev.orion.stardust.engine.optimize";
    private final String namespaceConnectionString = "Endpoint=sb://orion-scm-test.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=30zMPS5fHLaFVPBAToFE7F5RnOF/933TgL6Q7XMQAR0=";


    public IMessageSender init() throws ServiceBusException, InterruptedException{

        ConnectionStringBuilder connStrBuilder = new ConnectionStringBuilder(namespaceConnectionString,
                                                                             queue);
        connStrBuilder.setTransportType(TransportType.AMQP_WEB_SOCKETS);
        ClientSettings managementClientSettings = Util.getClientSettingsFromConnectionStringBuilder(connStrBuilder);
        MessagingFactory messagingFactory = MessagingFactory.createFromNamespaceEndpointURI(connStrBuilder.getEndpoint(),managementClientSettings);
        IMessageSender sender = ClientFactory.createMessageSenderFromEntityPath(messagingFactory, queue);
        return sender;
    }

    public MessagingFactory getAsync() throws Exception{

        ConnectionStringBuilder connStrBuilder = new ConnectionStringBuilder(namespaceConnectionString,
                                                                             queue);
        CompletableFuture<MessagingFactory> factoryFuture = MessagingFactory.createFromConnectionStringBuilderAsync(connStrBuilder);
        MessagingFactory messagingFactory = factoryFuture.get();
        return messagingFactory;
    }

    public IMessageReceiver getMessage() throws Exception{

        ConnectionStringBuilder connStrBuilder = new ConnectionStringBuilder(namespaceConnectionString,
                                                                             queue);
        ClientSettings managementClientSettings = Util.getClientSettingsFromConnectionStringBuilder(connStrBuilder);
        MessagingFactory messagingFactory = MessagingFactory.createFromNamespaceEndpointURI(connStrBuilder.getEndpoint(),managementClientSettings);
        IMessageReceiver receiver = ClientFactory.createMessageReceiverFromEntityPath(messagingFactory, queue, ReceiveMode.PEEKLOCK);
        return receiver;
    }

    public void close(QueueClient receiveClient) throws ServiceBusException{
//        receiveClient.getIsClosingOrClosed();
        receiveClient.close();
    }


}
