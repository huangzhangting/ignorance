package nom.ignorance.test.test.io.netty.codec.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import nom.ignorance.test.test.io.Constants;
import nom.ignorance.test.test.io.netty.codec.MsgpackDecoder;
import nom.ignorance.test.test.io.netty.codec.MsgpackEncoder;

/**
 * Created by huangzhangting on 2017/8/11.
 */
public class EchoServer2 {
    private int port;
    private int sendCount;

    public EchoServer2(int port, int sendCount) {
        this.port = port;
        this.sendCount = sendCount;
    }

    public void bind() throws Exception{
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             * 解决TCP粘包/半包问题
                             * LengthFieldBasedFrameDecoder + LengthFieldPrepender
                             */
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            ch.pipeline().addLast("MsgpackDecoder", new MsgpackDecoder());

                            /**
                             * 长度字段的长度，与上面对应
                             * 长度字段：定义详细的长度
                             */
                            ch.pipeline().addLast(new LengthFieldPrepender(2));
                            ch.pipeline().addLast("MsgpackEncoder", new MsgpackEncoder());

                            ch.pipeline().addLast(new EchoServerHandler2(sendCount));
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
            new EchoServer2(Constants.PORT, 4).bind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
