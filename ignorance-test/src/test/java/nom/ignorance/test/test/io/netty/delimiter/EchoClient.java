package nom.ignorance.test.test.io.netty.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class EchoClient {
    public void connect(String host, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ByteBuf delimit = Unpooled.copiedBuffer(Constants.DELIMITER.getBytes());
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimit));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
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
            new EchoClient().connect(Constants.HOST, Constants.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
