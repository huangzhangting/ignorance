package nom.ignorance.test.test.concurrenttest.forkjoin;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by huangzhangting on 2017/12/13.
 */
@Slf4j
public class ForkJoinTest {
    @Test
    public void test_computeTask() throws ExecutionException, InterruptedException {
        int start = 1;
        int end = 10;
        ComputeTask task = new ComputeTask(start, end);

//        task.fork();
//        int sum = task.join();

//        task.fork();
//        System.out.println(task.get());

        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(task);

        if(task.isCompletedAbnormally()){
            log.error("", task.getException());
        }else{
            log.info("start={}, end={}, sum={}", start, end, task.get());
        }
    }

}
