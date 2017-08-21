package nom.ignorance.test.test.io.netty.nettyprotocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import nom.ignorance.test.test.io.Constants;
import nom.ignorance.test.test.io.netty.NtCommon;
import nom.ignorance.test.test.io.netty.nettyprotocol.codec.NettyMessageDecoder;
import nom.ignorance.test.test.io.netty.nettyprotocol.codec.NettyMessageEncoder;
import nom.ignorance.test.test.io.netty.nettyprotocol.heartbeat.HeartBeatRespHandler;
import nom.ignorance.test.test.io.netty.nettyprotocol.login.LoginAuthRespHandler;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class NettyServer {
    public void bind(String host) throws Exception{
        NtCommon.bind(host, 100, new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new NettyMessageDecoder(1024*1024, 4, 4));
                ch.pipeline().addLast(new NettyMessageEncoder());
                ch.pipeline().addLast(new ReadTimeoutHandler(50));
                ch.pipeline().addLast(new LoginAuthRespHandler());
                ch.pipeline().addLast(new HeartBeatRespHandler());
            }
        });
    }


    public static void main(String[] args) {
        try {
            new NettyServer().bind(Constants.REMOTE_HOST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
