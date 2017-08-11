package nom.ignorance.test.test.io.netty.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class EchoServer {

    public void bind(int port) throws Exception{
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 100);
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ByteBuf delimit = Unpooled.copiedBuffer(Constants.DELIMITER.getBytes());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimit));
                    ch.pipeline().addLast(new StringDecoder());
                    ch.pipeline().addLast(new EchoServerHandler());
                }
            });

            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }



    public static void main(String[] args){
        try {
            new EchoServer().bind(Constants.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
