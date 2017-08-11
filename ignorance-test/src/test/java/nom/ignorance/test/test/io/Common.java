package nom.ignorance.test.test.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class Common {

    public static String getTime(String data){
        return Constants.SEND_DATA.equalsIgnoreCase(data)?new Date().toString():"BAD ORDER";
    }

    public static String getData(byte[] bytes){
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void bind(int backlog,
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

            ChannelFuture future = b.bind(Constants.PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

}
