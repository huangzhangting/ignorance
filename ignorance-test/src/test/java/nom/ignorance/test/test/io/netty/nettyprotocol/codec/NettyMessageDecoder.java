package nom.ignorance.test.test.io.netty.nettyprotocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.Header;
import nom.ignorance.test.test.io.netty.nettyprotocol.message.NettyMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
    MarshallingDecoderImpl marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        marshallingDecoder = MarshallingCodecFactory.buildMarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf buf = (ByteBuf)super.decode(ctx, in);
        if(buf==null){
            return null;
        }
        NettyMessage message = new NettyMessage();
        Header h = new Header();
        h.setCrcCode(in.readInt());
        h.setLength(in.readInt());
        h.setSessionId(in.readLong());
        h.setType(in.readByte());
        h.setPriority(in.readByte());

        int size = in.readInt();
        if(size>0){
            Map<String, Object> attachment = new HashMap<>(size);
            int keySize;
            byte[] keyArr;
            String key;
            for(int i=0; i<size; i++){
                keySize = in.readInt();
                keyArr = new byte[keySize];
                in.readBytes(keyArr);
                key = new String(keyArr, "UTF-8");
                attachment.put(key, marshallingDecoder.decode(ctx, in));
            }
            keyArr = null;
            key = null;
            h.setAttachment(attachment);
        }

        message.setHeader(h);
        if(in.readableBytes()>4){
            message.setBody(marshallingDecoder.decode(ctx, in));
        }
        return message;
    }
}
