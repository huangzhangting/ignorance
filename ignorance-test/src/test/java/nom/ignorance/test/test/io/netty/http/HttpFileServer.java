package nom.ignorance.test.test.io.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/14.
 */
@Slf4j
public class HttpFileServer {
    private static final String DEFAULT_URL = "/";

    public void run(final String host, final int port, final String url) throws Exception{
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    /* 请求消息解码器 */
                    ch.pipeline().addLast(new HttpRequestDecoder());
                    /* 目的是将多个消息转换为单一的request或者response对象 */
                    ch.pipeline().addLast(new HttpObjectAggregator(65536));
                    /* 响应消息编码器 */
                    ch.pipeline().addLast(new HttpResponseEncoder());
                    /* 目的是支持异步大文件传输 */
                    ch.pipeline().addLast(new ChunkedWriteHandler());

                    ch.pipeline().addLast(new HttpFileServerHandler(url));
                }
            });
            log.info("HTTP文件目录服务器启动，网址是 : http://{}:{}{}", host, port, url);
            ChannelFuture future = b.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        try {
            new HttpFileServer().run(Constants.HOST, Constants.PORT, DEFAULT_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
