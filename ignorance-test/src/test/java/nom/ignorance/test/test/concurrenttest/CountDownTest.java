package nom.ignorance.test.test.concurrenttest;

import nom.ignorance.test.test.BaseTest;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhangting on 2017/6/27.
 */
public class CountDownTest extends BaseTest{

    int threadNum = 5;
    int jobNum = 20;
    CountDownLatch latch;
    ExecutorService service;

    class MyRunnable implements Runnable{
        @Override
        public void run() {
            LOG.info(Thread.currentThread().getName()+" 开始处理任务");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        }
    }

    @Test
    public void test_等待子任务完成() throws Exception{
        latch = new CountDownLatch(jobNum);
        service = Executors.newFixedThreadPool(threadNum);
        LOG.info("主线程开始");
        for(int i=0; i<jobNum; i++){
            service.execute(new MyRunnable());
        }
        latch.await();
        LOG.info("主线程去做其他事");
    }


    @Test
    public void test_并发执行() throws Exception{
        int num = 40;
        // 开始的倒数锁
        final CountDownLatch begin = new CountDownLatch(1);
        // 结束的倒数锁
        final CountDownLatch end = new CountDownLatch(num);
        final ExecutorService exec = Executors.newFixedThreadPool(num);

        for (int index = 0; index < num; index++) {
            final int NO = index + 1;
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        // 等待
                        begin.await();
                        Thread.sleep(2000);
                        String name = "No." + NO;
                        System.out.println(name+" finished");
                    } catch (InterruptedException e) {
                    } finally {
                        // 任务执行完，end就减一
                        end.countDown();
                    }
                }
            };
            exec.submit(run);
        }
        System.out.println("Start");
        // begin减一，开始执行任务
        begin.countDown();
        // 等待end变为0，即所有任务执行完成
        end.await();
        System.out.println("Over");
        exec.shutdown();
    }
}
