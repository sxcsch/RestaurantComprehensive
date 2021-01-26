package auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("optimize")
public class OptimizeConfig {

    private String optimizeQueueName;

    private String resourceBasePath;

    private String resultQueueName;
}
