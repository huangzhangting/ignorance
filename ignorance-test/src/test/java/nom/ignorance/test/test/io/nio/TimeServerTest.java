package nom.ignorance.test.test.io.nio;

/**
 * Created by huangzhangting on 2017/8/4.
 */
public class TimeServerTest {
    public static void main(String[] args){
        int port = 8080;
        MultiplexerTimeServer server = new MultiplexerTimeServer(port);
        new Thread(server, "NIO-MultiplexerTimeServer").start();
    }
}
