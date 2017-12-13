package nom.ignorance.test.test.concurrenttest.threadpool;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by huangzhangting on 2017/12/12.
 */
public class ThreadPoolTest {
    private static final Random random = new Random();

    @Test
    public void test(){
        final int size = 5;
        final int count = size * 2;
        ExecutorService executorService = Executors.newFixedThreadPool(size);
        for(int i=0; i<count; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("test thread pool. NO=" + random.nextInt(count));
                }
            });
        }

        executorService = new ThreadPoolExecutor(10, 20, 100, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("test execute.");
            }
        });

    }

}
