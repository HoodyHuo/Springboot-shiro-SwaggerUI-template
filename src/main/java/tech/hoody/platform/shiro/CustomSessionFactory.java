package tech.hoody.platform.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;

/**
 * 自定义SessionFactory
 * 提供CustomSession的创建接口实现
 */
public class CustomSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext initData) {

        if (initData != null) {
            String host = initData.getHost();
            String username = ((DefaultWebSessionContext) initData).getServletRequest().getParameter("username");
            if (host != null && username != null) {
                return new CustomSession(host, username);
            }
        }
        return new CustomSession();
    }
}
