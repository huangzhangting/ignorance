package nom.ignorance.test.test.io.netty.line;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Common;
import nom.ignorance.test.test.io.Constants;

/**
 * Created by huangzhangting on 2017/8/10.
 */
@Slf4j
public class TimeClientHandler extends ChannelHandlerAdapter {
    private final ByteBuf sendMsg;

    private int count = 0;
    private byte[] sendBytes;

    public TimeClientHandler() {
        byte[] bytes = Constants.SEND_DATA.getBytes();
        sendMsg = Unpooled.buffer(bytes.length);
        sendMsg.writeBytes(bytes);

        sendBytes = (Constants.SEND_DATA+System.getProperty("line.separator")).getBytes();
        log.info("send bytes : {}", sendBytes);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(sendMsg);

        ByteBuf msg = null;
        for(int i=0; i<100; i++){
            msg = Unpooled.buffer(sendBytes.length);
            msg.writeBytes(sendBytes);
            ctx.writeAndFlush(msg);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        String data = Common.getData(bytes);

        String data = (String)msg;

        log.info("Now is : {}, the count is : {}", data, ++count);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("出现异常啦", cause);
        ctx.close();
    }
}
