package nom.ignorance.test.test.io;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by huangzhangting on 2017/8/10.
 */
public class Common {

    public static String getTime(String data){
        return Constants.SEND_DATA.equalsIgnoreCase(data)?new Date().toString():"BAD ORDER";
    }

    public static String getData(byte[] bytes){
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
