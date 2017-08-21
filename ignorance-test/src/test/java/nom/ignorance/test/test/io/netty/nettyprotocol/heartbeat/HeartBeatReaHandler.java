package nom.ignorance.test.test.io.netty.nettyprotocol.heartbeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.MessageTypeEnum;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.MessageUtil;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.NettyMessage;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangzhangting on 2017/8/21.
 */
@Slf4j
public class HeartBeatReaHandler extends ChannelHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(heartBeat!=null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(MessageUtil.checkMsg(msg, MessageTypeEnum.LOGIN_RESP)){
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
        }else if(MessageUtil.checkMsg(msg, MessageTypeEnum.HEART_BEAT_RESP)){
            log.info("client receive server heart beat message : {}", msg);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private class HeartBeatTask implements Runnable{
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage msg = MessageUtil.build(MessageTypeEnum.HEART_BEAT_REQ);
            log.info("client send heart beat message to server : {}", msg);
            ctx.writeAndFlush(msg);
        }
    }

}
