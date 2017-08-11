package nom.ignorance.test.test.io.netty.codec.example;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

/**
 * Created by huangzhangting on 2017/8/11.
 */
@Slf4j
public class EchoServerHandler2 extends ChannelHandlerAdapter {
    private int sendCount;

    public EchoServerHandler2(int sendCount) {
        this.sendCount = sendCount;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("The server receive [{}] from client.", msg);
        handleData(msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    private void handleData(Object msg) throws Exception{
        System.out.println(msg.getClass());
        Value data = (Value)msg;
        MessagePack pack = new MessagePack();
        UserInfo userInfo = pack.convert(data, UserInfo.class);
        log.info("receive user info : {}", userInfo);
    }
}
