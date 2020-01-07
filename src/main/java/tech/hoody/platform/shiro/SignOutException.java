package tech.hoody.platform.shiro;

import org.apache.shiro.authc.AccountException;

/**
 * 账号登录已失效，或者异地登录
 */
public class SignOutException extends AccountException {

    public SignOutException(String message) {
        super(message);
    }
}
