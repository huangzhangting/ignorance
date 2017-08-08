package nom.ignorance.test.test.io.aio;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/8.
 */
@Slf4j
public class TimeClientTest {
    public static void main(String[] args){
        new Thread(new AsyncTimeClientHandler(Constants.HOST, Constants.PORT), "AsyncTimeClientHandler-001").start();
        new Thread(new AsyncTimeClientHandler(Constants.HOST, Constants.PORT), "AsyncTimeClientHandler-002").start();
        new Thread(new AsyncTimeClientHandler(Constants.HOST, Constants.PORT), "AsyncTimeClientHandler-003").start();
    }
}
