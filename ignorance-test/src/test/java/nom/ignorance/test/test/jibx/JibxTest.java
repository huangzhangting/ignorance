package nom.ignorance.test.test.jibx;

import org.jibx.binding.BindingGenerator;

/**
 * Created by huangzhangting on 2017/8/16.
 */
public class JibxTest {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("."));

        String testPath = "src/test/java/nom/ignorance/test/test";

        String path = "nom.ignorance.test.test.jibx";

        String[] params = {"-p", testPath+"/jibx"};
        BindingGenerator bg = new BindingGenerator();
        bg.main(params);
    }
}
