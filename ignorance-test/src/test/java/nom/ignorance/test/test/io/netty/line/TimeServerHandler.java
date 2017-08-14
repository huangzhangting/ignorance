package nom.ignorance.test.test.io.netty.line;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.io.Common;

/**
 * Created by huangzhangting on 2017/8/10.
 */
@Slf4j
public class TimeServerHandler extends ChannelHandlerAdapter {
    private int count = 0;

    private int lineSeparatorLen;
    private String lineSeparator;

    public TimeServerHandler() {
        lineSeparator = System.getProperty("line.separator");
        lineSeparatorLen = lineSeparator.length();
        log.info("lineSeparatorLen : {}", lineSeparatorLen);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        String data = new String(bytes, "UTF-8").substring(0, bytes.length-lineSeparatorLen);

        String data = (String)msg;

        log.info("Time server receive order : {}, the count is : {}", data, ++count);
        String now = Common.getTime(data)+lineSeparator;
        ByteBuf resp = Unpooled.copiedBuffer(now.getBytes());
        //写入缓冲区
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将缓冲区的数据，写到 socket channel
        ctx.flush();
    }
}
