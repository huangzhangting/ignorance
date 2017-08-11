package nom.ignorance.test.test.io.netty.codec.example;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by huangzhangting on 2017/8/11.
 */
@Slf4j
public class EchoClientHandler2 extends ChannelHandlerAdapter {
    private int sendCount;

    public EchoClientHandler2(int sendCount) {
        this.sendCount = sendCount;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client channelActive");
        for(UserInfo userInfo : UserInfo.userInfoList(sendCount)){
            ctx.writeAndFlush(userInfo);
            log.info("write user info : {}", userInfo);
        }
//        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("Client receive [{}] from server", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
