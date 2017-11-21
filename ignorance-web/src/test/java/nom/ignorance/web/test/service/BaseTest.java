package nom.ignorance.web.test.service;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by huangzhangting on 2017/5/24.
 */
//由于部分service中直接依赖注入了HttpServletRequest，所以需要使用@WebAppConfiguration
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-context.xml"})
public class BaseTest {

    @Before
    public void beforeTest(){
        System.out.println("\n\n==================== 开始 Test ====================\n");

    }


    @After
    public void afterTest(){

        System.out.println("\n==================== Test 结束 ====================\n\n");
    }
}
