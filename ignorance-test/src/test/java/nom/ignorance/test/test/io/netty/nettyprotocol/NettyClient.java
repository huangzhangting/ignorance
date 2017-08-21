package nom.ignorance.test.test.io.netty.nettyprotocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import nom.ignorance.test.test.io.Constants;
import nom.ignorance.test.test.io.netty.nettyprotocol.codec.NettyMessageDecoder;
import nom.ignorance.test.test.io.netty.nettyprotocol.codec.NettyMessageEncoder;
import nom.ignorance.test.test.io.netty.nettyprotocol.heartbeat.HeartBeatReaHandler;
import nom.ignorance.test.test.io.netty.nettyprotocol.login.LoginAuthReqHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class NettyClient {
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();

    private void connect(String host) throws Exception{
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
                    ch.pipeline().addLast(new NettyMessageEncoder());
                    ch.pipeline().addLast(new ReadTimeoutHandler(50));
                    ch.pipeline().addLast(new LoginAuthReqHandler());
                    ch.pipeline().addLast(new HeartBeatReaHandler());
                }
            });
            ChannelFuture future = b.connect(
                    new InetSocketAddress(host, Constants.PORT),
                    new InetSocketAddress(Constants.HOST, Constants.PORT)).sync();
            future.channel().closeFuture().sync();
        } finally {
            reTryConnect();
        }
    }

    private void reTryConnect(){
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    connect(Constants.HOST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        try {
            new NettyClient().connect(Constants.REMOTE_HOST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
