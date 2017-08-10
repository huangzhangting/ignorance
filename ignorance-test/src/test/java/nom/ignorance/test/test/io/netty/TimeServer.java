package nom.ignorance.test.test.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class TimeServer {

    public void bind(int port) throws Exception {
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            /**
             * 解决TCP粘包和拆包问题
             * LineBasedFrameDecoder + StringDecoder 组合就是按行切换的文本解码器
             * StringDecoder 就是将接收到的对象转成字符串
             * LineBasedFrameDecoder 已换行为结束标志的解码器
             *
             * ChannelHandler 配置顺序必须有要求
             */
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());

            ch.pipeline().addLast(new TimeServerHandler());
        }
    }



    public static void main(String[] args){
        try {
            new TimeServer().bind(Constants.PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
