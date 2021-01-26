package auth.client;

import auth.config.OptimizeConfig;
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
    @Autowired
    private OptimizeConfig optimizeConfig;


    public IMessageSender init() throws ServiceBusException, InterruptedException{
        String namespaceConnectionString = environment.getProperty("spring.jms.servicebus.connection-string");
        ConnectionStringBuilder connStrBuilder = new ConnectionStringBuilder(namespaceConnectionString,
                                                                             optimizeConfig.getOptimizeQueueName());
        connStrBuilder.setTransportType(TransportType.AMQP_WEB_SOCKETS);
        ClientSettings managementClientSettings = Util.getClientSettingsFromConnectionStringBuilder(connStrBuilder);
        MessagingFactory messagingFactory = MessagingFactory.createFromNamespaceEndpointURI(connStrBuilder.getEndpoint(),managementClientSettings);
        IMessageSender sender = ClientFactory.createMessageSenderFromEntityPath(messagingFactory, optimizeConfig.getOptimizeQueueName());
        return sender;
    }

    public MessagingFactory getAsync() throws Exception{
        String namespaceConnectionString = environment.getProperty("spring.jms.servicebus.connection-string");
        ConnectionStringBuilder connStrBuilder = new ConnectionStringBuilder(namespaceConnectionString,
                                                                             optimizeConfig.getOptimizeQueueName());
        CompletableFuture<MessagingFactory> factoryFuture = MessagingFactory.createFromConnectionStringBuilderAsync(connStrBuilder);
        MessagingFactory messagingFactory = factoryFuture.get();
        return messagingFactory;
    }

    public IMessageReceiver getMessage() throws Exception{
        String namespaceConnectionString = environment.getProperty("spring.jms.servicebus.connection-string");
        ConnectionStringBuilder connStrBuilder = new ConnectionStringBuilder(namespaceConnectionString,
                                                                             optimizeConfig.getOptimizeQueueName());
        ClientSettings managementClientSettings = Util.getClientSettingsFromConnectionStringBuilder(connStrBuilder);
        MessagingFactory messagingFactory = MessagingFactory.createFromNamespaceEndpointURI(connStrBuilder.getEndpoint(),managementClientSettings);
        IMessageReceiver receiver = ClientFactory.createMessageReceiverFromEntityPath(messagingFactory, optimizeConfig.getOptimizeQueueName(), ReceiveMode.PEEKLOCK);
        return receiver;
    }

    public void close(QueueClient receiveClient) throws ServiceBusException{
//        receiveClient.getIsClosingOrClosed();
        receiveClient.close();
    }


}
