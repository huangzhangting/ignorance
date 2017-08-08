package nom.ignorance.test.test.io.nio;

/**
 * Created by huangzhangting on 2017/8/4.
 */
public class TimeClientTest {
    public static void main(String[] args){
        new Thread(new TimeClientHandle("127.0.0.1", 8080), "TimeClientHandle start").start();
    }
}
