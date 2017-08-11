package nom.ignorance.test.test.io.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("MsgpackEncoder: "+msg);
        MessagePack pack = new MessagePack();
        byte[] bytes = pack.write(msg);
        out.writeBytes(bytes);
    }
}
