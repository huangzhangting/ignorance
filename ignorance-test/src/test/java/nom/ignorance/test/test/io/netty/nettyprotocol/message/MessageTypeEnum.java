package nom.ignorance.test.test.io.netty.nettyprotocol.message;

/**
 * Created by huangzhangting on 2017/8/21.
 */
public enum MessageTypeEnum {
    LOGIN_REQ((byte)0, "登录请求"),
    LOGIN_RESP((byte)1, "登录响应"),
    HEART_BEAT_REQ((byte)2, ""),
    HEART_BEAT_RESP((byte)3, "");

    private byte code;
    private String desc;

    MessageTypeEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
