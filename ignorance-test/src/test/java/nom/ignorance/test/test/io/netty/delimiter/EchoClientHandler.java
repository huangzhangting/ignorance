package nom.ignorance.test.test.io.netty.delimiter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/10.
 */
@Slf4j
public class EchoClientHandler extends ChannelHandlerAdapter{
    int count = 0;
    static final String ECHO_MSG = "Hi hzt, welcome to netty!" + Constants.DELIMITER;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0; i<10; i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_MSG.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("This is {} times receive [{}] from server.", ++count, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
