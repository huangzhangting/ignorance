package nom.ignorance.test.test.io.netty.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.protobuf.model.SubscribeReqModel;

import java.util.ArrayList;

/**
 * Created by huangzhangting on 2017/8/14.
 */
@Slf4j
@ChannelHandler.Sharable
public class SubReqClientHandler extends ChannelHandlerAdapter {
    private int sendCount;

    public SubReqClientHandler(int sendCount) {
        this.sendCount = sendCount;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i=0; i<sendCount; i++){
//            ctx.writeAndFlush(buildReq(i+1));
            ctx.write(buildReq(i + 1));
        }
        ctx.flush();
    }
    private SubscribeReqModel.SubscribeReq buildReq(int reqId){
        SubscribeReqModel.SubscribeReq.Builder builder = SubscribeReqModel.SubscribeReq.newBuilder();
        builder.setReqId(reqId);
        builder.setUserName("hzt");
        builder.setProductName("netty");
        builder.addAllAddress(new ArrayList<String>(){{
            add("南极");
            add("地球");
        }});

        return builder.build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("receive from server : {}", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
