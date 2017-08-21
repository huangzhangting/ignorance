package nom.ignorance.test.test.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/14.
 */
public class NtCommon {

    public static void bind(String host, int backlog,
                            ChannelInitializer<SocketChannel> childHandler) throws Exception{
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, backlog);
            b.handler(new LoggingHandler(LogLevel.INFO));
            b.childHandler(childHandler);

            ChannelFuture future = b.bind(host, Constants.PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
    public static void bind(int backlog,
                            ChannelInitializer<SocketChannel> childHandler) throws Exception{
        bind(Constants.HOST, backlog, childHandler);
    }
    public static void bind(ChannelInitializer<SocketChannel> childHandler) throws Exception{
        bind(1024, childHandler);
    }


    public static void connect(ChannelInitializer<SocketChannel> handler) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(handler);

            ChannelFuture future = b.connect(Constants.HOST, Constants.PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
