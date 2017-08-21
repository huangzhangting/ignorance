package nom.ignorance.test.test.io.netty.nettyprotocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public class MarshallingEncoderImpl extends MarshallingEncoder{
    public MarshallingEncoderImpl(MarshallerProvider provider) {
        super(provider);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
