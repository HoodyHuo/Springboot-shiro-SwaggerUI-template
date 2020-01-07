package tech.hoody.platform.shiro;

import org.apache.shiro.session.mgt.SimpleSession;

/**
 * 自定义Session ，增加了用户名信息
 */
public class CustomSession extends SimpleSession {
    private String usernmae;

    public String getUsernmae() {
        return usernmae;
    }

    public void setUsernmae(String usernmae) {
        this.usernmae = usernmae;
    }

    public CustomSession() {
        this.usernmae = null;
    }

    public CustomSession(String host, String usernmae) {
        super(host);
        this.usernmae = usernmae;
    }
}
