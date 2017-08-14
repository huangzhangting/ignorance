package nom.ignorance.test.test.io.netty.protobuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import nom.ignorance.test.test.protobuf.model.SubscribeReqModel;
import nom.ignorance.test.test.protobuf.model.SubscribeRespModel;

/**
 * Created by huangzhangting on 2017/8/11.
 */
@Slf4j
@ChannelHandler.Sharable
public class SubReqServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqModel.SubscribeReq req = (SubscribeReqModel.SubscribeReq)msg;
        if("hzt".equalsIgnoreCase(req.getUserName())){
            log.info("receive from  client : {}", req);
            ctx.writeAndFlush(buildResp(req.getReqId()));
        }
    }
    private SubscribeRespModel.SubscribeResp buildResp(int reqId){
        SubscribeRespModel.SubscribeResp.Builder builder = SubscribeRespModel.SubscribeResp.newBuilder();
        builder.setReqId(reqId);
        builder.setRespCode(10);
        builder.setDesc("花枝招展，黑珍珠");
        return builder.build();
    }
}
