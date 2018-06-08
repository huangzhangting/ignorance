package nom.ignorance.test.test.guava;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class RateLimiterTest {

    @Test
    public void test(){
        RateLimiter rateLimiter = RateLimiter.create(5);

    }
}
