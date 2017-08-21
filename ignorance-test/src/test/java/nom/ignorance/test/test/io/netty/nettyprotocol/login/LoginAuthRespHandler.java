package nom.ignorance.test.test.io.netty.nettyprotocol.login;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.MessageTypeEnum;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.MessageUtil;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.NettyMessage;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huangzhangting on 2017/8/21.
 */
@Slf4j
public class LoginAuthRespHandler extends ChannelHandlerAdapter {
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(getNodeIndex(ctx));
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(MessageUtil.checkMsg(msg, MessageTypeEnum.LOGIN_REQ)){
            String nodeIndex = getNodeIndex(ctx);
            NettyMessage loginResp;
            if(nodeCheck.containsKey(nodeIndex)){
                loginResp = buildResponse((byte)-2);
            }else{
                InetSocketAddress address = (InetSocketAddress)ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for(String wip : Constants.WHITE_IP_LIST){
                    if(wip.equals(ip)){
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK?buildResponse((byte)0):buildResponse((byte)-1);
                if(isOK){
                    nodeCheck.put(nodeIndex, true);
                }
            }
            log.info("the login response is : {}", loginResp);
            ctx.writeAndFlush(loginResp);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    private String getNodeIndex(ChannelHandlerContext ctx){
        return ctx.channel().remoteAddress().toString();
    }

    private NettyMessage buildResponse(byte result){
        return MessageUtil.build(MessageTypeEnum.LOGIN_RESP.getCode(), result);
    }

}
