package nom.ignorance.test.test.io.nio;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by huangzhangting on 2017/8/4.
 */
@Slf4j
public class MultiplexerTimeServer implements Runnable {
    private int port;

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop = false;

    public MultiplexerTimeServer(int port) {
        this.port = port;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            //channel注册到selector，监听OP_ACCEPT操作位
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("MultiplexerTimeServer is start in port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop){
            try {
                //休眠时间
                selector.select(1000);
                Set<SelectionKey> keySet = selector.selectedKeys();
                log.info("key set : {}", keySet);
                Iterator<SelectionKey> it = keySet.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if(key!=null){
                            key.cancel();
                            if(key.channel()!=null){
                                try {
                                    key.channel().close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //多路复用器关闭后，所有注册在上面的 channel 和 pipe 等资源
        //都会被自动去注册并关闭，所以不需要重复释放资源
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            if(key.isAcceptable()){
                //接受客户端链接请求，并建立链接
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel channel = ssc.accept();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
                log.info("key isAcceptable, channel : {}", channel);
            }

            if(key.isReadable()){
                SocketChannel channel = (SocketChannel)key.channel();
                log.info("key isReadable, channel : {}", channel);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = channel.read(buffer);
                log.info("read bytes : {}", readBytes);
                if(readBytes>0){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    log.info("MultiplexerTimeServer receive order : {}", body);
                    String now = Constants.SEND_DATA.equalsIgnoreCase(body)?new Date().toString():"BAD ORDER";
                    doWrite(channel, now);
                }else if(readBytes<0){
                    key.cancel();
                    channel.close();
                }else{

                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException{
        if(response==null || response.trim().length()==0){
            return;
        }
        byte[] bytes = response.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        channel.write(buffer);
    }

}
