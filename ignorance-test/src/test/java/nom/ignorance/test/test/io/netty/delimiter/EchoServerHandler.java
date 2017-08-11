package nom.ignorance.test.test.io.netty.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/10.
 */
@Slf4j
public class EchoServerHandler extends ChannelHandlerAdapter{
    int count = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String data = (String)msg;
        log.info("This is {} times receive [{}] from client", ++count, data);
        data += Constants.DELIMITER;
        ByteBuf buf = Unpooled.copiedBuffer(data.getBytes());
        ctx.writeAndFlush(buf);
    }
}
