package nom.ignorance.test.test.property;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class TestResourceBundle {

    @Test
    public void test_1(){
        ResourceBundle bundle = ResourceBundle.getBundle("res", new Locale("zh", "CN"));
        Enumeration<String> keys = bundle.getKeys();
        System.out.println(keys);

        String name = bundle.getString("test.name");
        Integer age = Integer.valueOf(bundle.getString("test.age"));
        log.info("name:{}, age:{}", name, age);

    }

}
