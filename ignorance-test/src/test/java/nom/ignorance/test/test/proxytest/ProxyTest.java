package nom.ignorance.test.test.proxytest;

import org.junit.Test;
import nom.ignorance.test.test.proxytest.cglib.CglibProxy;
import nom.ignorance.test.test.proxytest.jdk.Handler;

import java.lang.reflect.Proxy;

/**
 * Created by huangzhangting on 17/4/23.
 */
public class ProxyTest {

    @Test
    public void test_jdk(){
        UserBiz userBiz = new UserBizImpl();
        Handler handler = new Handler(userBiz);
        UserBiz biz = (UserBiz)Proxy.newProxyInstance(userBiz.getClass().getClassLoader(), userBiz.getClass().getInterfaces(), handler);
        System.out.println(biz.getUser());
    }

    @Test
    public void test_cglib(){
        UserBizImpl userBiz = CglibProxy.proxyTarget(new UserBizImpl());
        userBiz.getUser();
    }

}
