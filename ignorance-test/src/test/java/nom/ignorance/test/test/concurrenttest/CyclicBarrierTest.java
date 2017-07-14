package nom.ignorance.test.test.concurrenttest;


import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhangting on 2017/5/27.
 */
public class CyclicBarrierTest {

    public static void main(String[] args){

        Random random = new Random();
        int num = 3;
        ExecutorService service = Executors.newFixedThreadPool(num);
        CyclicBarrier barrier = new CyclicBarrier(num, new Runnable() {
            @Override
            public void run() {
                System.out.println("**********我最先执行**********");
            }
        });
        for(int i=0; i<num; i++){
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        toWait("集合点1");
                        doSomething("集合点1");

                        toWait("集合点2");
                        doSomething("集合点2");

                        toWait("集合点3");
                        doSomething("集合点3");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private void toWait(String msg) throws Exception{
                    Thread.sleep(1000 * (random.nextInt(10) + 1));
                    System.out.println("线程" + Thread.currentThread().getName() + "即将到达" + msg + "，当前已有"
                            + barrier.getNumberWaiting() + "个已到达，正在等待。");
                    barrier.await();
                }
                private void doSomething(String msg) throws Exception{
//                    Thread.sleep(1000 * (random.nextInt(10) + 1));
                    System.out.println(Thread.currentThread().getName()+"：我在 "+msg+" 干了些什么！");
                }
            };
            service.submit(run);
        }
        service.shutdown();
    }

}
