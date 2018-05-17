package nom.ignorance.test.test.stream;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

@Slf4j
public class StreamTest {

    @Test
    public void test_1(){
        List<Integer> list = Lists.newArrayList(1, null, 3, 4, null);
        long count = list.stream().filter(num -> num != null).count();
        log.info("非null数量：{}", count);
    }

}
