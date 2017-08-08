package nom.ignorance.test.test.io.nio;

import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by huangzhangting on 2017/8/4.
 */
@Slf4j
public class TimeClientHandle implements Runnable {
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop = false;

    public TimeClientHandle(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> keySet = selector.selectedKeys();
                log.info("keys: " + keySet);
                Iterator<SelectionKey> it = keySet.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
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

            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }

        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean doConnect() throws IOException{
        if(socketChannel.connect(new InetSocketAddress(host, port))){
            //因为 socketChannel.configureBlocking(false); 所以不会走到这
            log.info("doConnect succeed");
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
            return true;
        }else{
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            return false;
        }
    }

    private void handleInput(SelectionKey key) throws IOException{
        if(key.isValid()){
            SocketChannel channel = (SocketChannel)key.channel();
            if(key.isConnectable()){
                log.info("key isConnectable");
                if(channel.finishConnect()){
                    log.info("channel finishConnect");
                    channel.register(selector, SelectionKey.OP_READ);
                    doWrite(channel);
                }else{
                    System.exit(1);
                }
            }

            if(key.isReadable()){
                log.info("key isReadable");
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = channel.read(buffer);
                log.info("read bytes : {}", readBytes);
                if(readBytes>0){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    log.info("Now is:"+body);
                    stop();
                }else if(readBytes<0){
                    key.cancel();
                    channel.close();
                }else{

                }
            }
        }
    }

    private void doWrite(SocketChannel channel) throws IOException{
        byte[] req = Constants.SEND_DATA.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(req.length);
        buffer.put(req);
        buffer.flip();
        channel.write(buffer);
        if(!buffer.hasRemaining()){
            log.info("Send order to server succeed.");
        }
    }
}
