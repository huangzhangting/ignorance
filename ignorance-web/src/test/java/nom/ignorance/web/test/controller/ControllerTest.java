package nom.ignorance.web.test.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by huangzhangting on 2017/6/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/application-context.xml", "classpath:spring/web-dispatcher.xml"})
@WebAppConfiguration
public class ControllerTest {
    MockHttpServletRequest request;
    MockHttpServletResponse response;

    @Before
    public void beforeTest(){
        System.out.println("\n\n==================== 开始 ControllerTest ====================\n");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");

        response = new MockHttpServletResponse();
    }


    @After
    public void afterTest(){

        System.out.println("\n==================== ControllerTest 结束 ====================\n\n");
    }
}
