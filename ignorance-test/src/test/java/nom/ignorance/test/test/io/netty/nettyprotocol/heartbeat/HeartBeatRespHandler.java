package nom.ignorance.test.test.io.netty.nettyprotocol.heartbeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.MessageTypeEnum;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.MessageUtil;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.NettyMessage;

/**
 * Created by huangzhangting on 2017/8/21.
 */
@Slf4j
public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(MessageUtil.checkMsg(msg, MessageTypeEnum.HEART_BEAT_REQ)){
            log.info("server receive heart beat message from client : {}", msg);
            NettyMessage resp = MessageUtil.build(MessageTypeEnum.HEART_BEAT_RESP);
            log.info("server send heart beat to client : {}", resp);
            ctx.writeAndFlush(resp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

}
