package tech.hoody.platform.shiro

import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationException
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired
import tech.hoody.platform.domain.Role
import tech.hoody.platform.domain.User
import tech.hoody.platform.service.AuthService
import tech.hoody.platform.service.UserService

import javax.security.auth.login.AccountException

/**
 * @auth Hoody* 自定义shiro 权限验证对象
 */
class CustomRealm extends AuthorizingRealm {


    @Autowired
    UserService userService

    @Autowired
    AuthService authService


    /**
     * 授权
     * 定义如何获取用户的角色和权限的逻辑，给shiro做权限判断
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        User user = (User) getAvailablePrincipal(principals);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(user.getRoles())
        info.setStringPermissions(user.getPerms())
        return info
    }

    /**
     * 定义如何获取用户信息的业务逻辑，给shiro做登录
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        // Null username is invalid
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }
        User user = userService.findUserByName(username);
        if (user == null) {
            throw new UnknownAccountException("No account found for admin [" + username + "]");
        }
        if (user.isLocked()) {
            throw new LockedAccountException("Account [" + username + "] is locked.");
        }
        if (user.isCredentialsExpired()) {
            String msg = "The credentials for account [" + username + "] are expired";
            throw new ExpiredCredentialsException(msg);
        }

        //查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方
//        SecurityUtils.getSubject().getPrincipal()//就能拿出用户的所有信息，包括角色和权限
        Set<Role> roles = authService.findRolesByUser(user)
        Set<String> roleStrSet = roles.role
        Set<String> permsStrSet = authService.findPermissionByRoles(roles).permission

        /** 获取用户权限和角色*/
        user.getRoles().addAll(roleStrSet)
        user.getPerms().addAll(permsStrSet)

        /** 将用户输入密码安装盐进行加密*/
        def encodePassword = authService.getInputPasswordCiph(upToken.getPassword().toString(), user.salt)
        upToken.setPassword(encodePassword.toCharArray())

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.password, getName())
        return info
    }
}
