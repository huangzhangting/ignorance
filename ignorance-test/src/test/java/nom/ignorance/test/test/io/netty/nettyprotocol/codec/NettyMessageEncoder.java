package nom.ignorance.test.test.io.netty.nettyprotocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.Header;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.NettyMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    MarshallingEncoderImpl marshallingEncoder;

    public NettyMessageEncoder() {
        this.marshallingEncoder = MarshallingCodecFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if(msg==null || msg.getHeader()==null){
            throw new RuntimeException("the message is null");
        }
        Header h = msg.getHeader();
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(h.getCrcCode());
        buf.writeInt(h.getLength());
        buf.writeLong(h.getSessionId());
        buf.writeByte(h.getType());
        buf.writeByte(h.getPriority());
        buf.writeInt(h.getAttachment().size());
        byte[] keyArr = null;
        for(Map.Entry<String, Object> entry : h.getAttachment().entrySet()){
            keyArr = entry.getKey().getBytes("UTF-8");
            buf.writeInt(keyArr.length);
            buf.writeBytes(keyArr);
            marshallingEncoder.encode(ctx, entry.getValue(), buf);
        }
        keyArr = null;

        if(msg.getBody() != null){
            marshallingEncoder.encode(ctx, msg.getBody(), buf);
        }else{
            buf.writeInt(0);
            buf.setInt(4, buf.readableBytes());
        }
    }
}
