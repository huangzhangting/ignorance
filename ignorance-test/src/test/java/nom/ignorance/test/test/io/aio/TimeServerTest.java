package nom.ignorance.test.test.io.aio;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/8.
 */
@Slf4j
public class TimeServerTest {
    public static void main(String[] args){
        new Thread(new AsyncTimeServerHandler(Constants.PORT), "AsyncTimeServerHandler-001").start();
    }
}
