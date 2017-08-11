package nom.ignorance.test.test.io.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import nom.ignorance.test.test.io.netty.codec.example.UserInfo;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int len = msg.readableBytes();
        System.out.println("MsgpackDecoder : "+len);

        byte[] bytes = new byte[len];
        msg.getBytes(msg.readerIndex(), bytes, 0, len);
//        msg.readBytes(bytes);

        MessagePack pack = new MessagePack();
        out.add(pack.read(bytes));
    }
}
