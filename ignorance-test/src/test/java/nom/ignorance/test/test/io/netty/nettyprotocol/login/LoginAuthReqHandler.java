package nom.ignorance.test.test.io.netty.nettyprotocol.login;

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
public class LoginAuthReqHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(MessageUtil.build(MessageTypeEnum.LOGIN_REQ));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage)msg;
        if(MessageUtil.checkMessage(message, MessageTypeEnum.LOGIN_RESP)){
            byte loginResult = (byte)message.getBody();
            if(loginResult != 0){
                ctx.close();
            }else{
                log.info("Login ok : {}", message);
                ctx.fireChannelRead(msg);
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

}
