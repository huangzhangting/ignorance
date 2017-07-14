package nom.ignorance.test.test.concurrenttest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by huangzhangting on 2017/6/27.
 */
public class ExchangerTest {
    public static void main(String[] args){
        final Exchanger<List<Integer>> exchanger = new Exchanger<>();

        new Thread(){
            @Override
            public void run() {
                List<Integer> list = new ArrayList<Integer>(){{
                    add(1);
                    add(2);
                }};
                try {
                    list = exchanger.exchange(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread1="+list);
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                List<Integer> list = new ArrayList<Integer>(){{
                    add(3);
                    add(4);
                }};
                try {
                    list = exchanger.exchange(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Thread2="+list);
            }
        }.start();

    }
}
