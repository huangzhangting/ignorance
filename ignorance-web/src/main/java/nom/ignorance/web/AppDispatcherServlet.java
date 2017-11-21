package nom.ignorance.web;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppDispatcherServlet extends DispatcherServlet {

    @Override
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.noHandlerFound(request, response);
    }

}
