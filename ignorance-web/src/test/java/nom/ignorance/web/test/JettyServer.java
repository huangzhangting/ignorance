package nom.ignorance.web.test;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by huangzhangting
 */
public class JettyServer {
    public static void main(String[] args) {
        Server server = new Server(8080);
        WebAppContext context = new WebAppContext();
        //项目根路径
        context.setContextPath("/");
        context.setDescriptor("ignorance-web/src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("ignorance-web/src/main/webapp");

        context.setMaxFormContentSize(10485760);
        context.setParentLoaderPriority(true);
        server.setHandler(context);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
