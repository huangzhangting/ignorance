package nom.ignorance.test.test.io.netty.codec.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import nom.ignorance.test.test.io.Constants;
import nom.ignorance.test.test.io.netty.codec.MsgpackDecoder;
import nom.ignorance.test.test.io.netty.codec.MsgpackEncoder;

/**
 * Created by huangzhangting on 2017/8/11.
 */
public class EchoClient2 {
    private String host;
    private int port;
    private int sendCount;

    public EchoClient2(String host, int port, int sendCount) {
        this.host = host;
        this.port = port;
        this.sendCount = sendCount;
    }

    public void connect() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            ch.pipeline().addLast("MsgpackDecoder", new MsgpackDecoder());

                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast("MsgpackEncoder", new MsgpackEncoder());

                            ch.pipeline().addLast(new EchoClientHandler2(sendCount));
                        }
                    });

            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }



    public static void main(String[] args){
        try {
            new EchoClient2(Constants.HOST, Constants.PORT, 2).connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
