import callsqueue.CallsQueueApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = CallsQueueApplication.class)
public class SpringBootJunitTestcase {
    /**
     * 获取属性配制文件中的值
     */
    @Autowired
    private Environment environment;

    @Test
    public void test(){

    }

}