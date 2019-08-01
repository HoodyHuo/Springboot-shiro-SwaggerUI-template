
package vip.hoody.pi.config;

import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.hoody.pi.domain.Permission;
import vip.hoody.pi.domain.RequestMap;
import vip.hoody.pi.domain.Role;
import vip.hoody.pi.service.RequestMapService;
import vip.hoody.pi.shiro.CustomRealm;
import vip.hoody.pi.shiro.CustomSessionManager;

import java.util.List;

/**
 * shiro 配置类
 * 使用shiro权限时 按照角色 和  许可 permission 进行注解
 *
 * @see Role
 * @see Permission
 * @see RequestMap
 * W https://shiro.apache.org/spring-boot.html
 * <p>
 * <p>
 * Shiro 注解  用法
 * 可以在Action或控制器加入注解
 * '@RequiresGuest‘ 只有游客可以访问*
 * ’@RequiresAuthentication‘ 需要登录才能访问*
 * '@RequiresUser' 已登录的用户或“记住我”的用户能访问
 * '@RequiresRoles('rolename')' 已登录的用户需具有指定的角色才能访问*
 * '@RequiresPermissions('user:create')' 已登录的用户需具有指定的权限才能访问
 */

@Configuration
class ShiroConfig {
    /**
     * 已登录权限
     */
    private static final String IS_AUTHENTICATED = "authc";
    /**
     * 匿名权限
     */
    private static final String IS_ANONYMOUSLY = "anon";

    /**
     * 注入自定义权限验证对象
     */
    @Bean
    public CustomRealm customRealm() {
        CustomRealm realm = new CustomRealm();
        return new CustomRealm();
    }

    /**
     * 为了保证实现了Shiro内部lifecycle函数的bean执行 也是shiro的生命周期
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * SecurityManager是Shiro框架的核心，典型的Facade模式，
     * Shiro通过SecurityManager来管理内部组件实例，并通过它来提供安全管理的各种服务
     * SecurityUtils.getSubject()来进行门面获取
     */
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //自定义realm
        securityManager.setRealm(customRealm);
        //自定义session管理
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 自定义sessionManager
     *
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        CustomSessionManager customSessionManager = new CustomSessionManager();
        //这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
        //customSessionManager.setSessionDAO(redisSessionDAO());
        return customSessionManager;
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        /**
         * setUsePrefix(true)用于解决一个奇怪的bug。在引入spring aop的情况下。
         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，
         * 导致返回404。加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }


    /**
     * 配置缩写       	        对应的过滤器	                        功能
     * anon	             AnonymousFilter	                指定url可以匿名访问
     * authc	             FormticationFilter	        指定url需要form表单登录，默认会从请求中获取username、password,rememberMe等参数并尝试登录，如果登录不了就会跳转到loginUrl配置的路径。我们也可以用这个过滤器做默认的登录逻辑，但是一般都是我们自己在控制器写登录逻辑的，自己写的话出错返回的信息都可以定制嘛。
     * authcBasic	         BasicHttpAuthenticationFilter	    指定url需要basic登录
     * logout	             LogoutFilter	                    登出过滤器，配置指定url就可以实现退出功能，非常方便
     * noSessionCreation	 NoSessionCreationFilter	        禁止创建会话
     * perms	             PermissionsAuthorizationFilter	    需要指定权限才能访问
     * port	             PortFilter	                        需要指定端口才能访问
     * rest	             HttpMethodPermissionFilter	        将http请求方法转化成相应的动词来构造一个权限字符串，这个感觉意义不大，有兴趣自己看源码的注释
     * roles	             RolesAuthorizationFilter	        需要指定角色才能访问
     * ssl	                 SslFilter	                        需要https请求才能访问
     * user	             UserFilter	                        需要已登录或“记住我”的用户才能访问
     * <p>
     * 但经过实际测试，过滤器的过滤路径，是context-path下的路径，无需加上"/platform"前缀
     * chain.addPathDefinition("/my/mvnBuild", "authc,perms[mvn:install]");//需要mvn:build权限
     * chain.addPathDefinition("/my/npmClean", "authc,perms[npm:clean]");//需要npm:clean权限
     * chain.addPathDefinition("/my/docker", "authc,roles[docker]");//需要js角色
     * chain.addPathDefinition("/my/python", "authc,roles[python]");//需要python角色
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(RequestMapService requestMapService) {

        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
        List<RequestMap> requestMaps = requestMapService.findAllRequestMap();

        for (RequestMap requestMap : requestMaps) {
            if (IS_ANONYMOUSLY.equals(requestMap.getConfigAttribute())) {
                chain.addPathDefinition(requestMap.getUrl(), IS_ANONYMOUSLY);
            } else if (IS_AUTHENTICATED.equals(requestMap.getConfigAttribute())) {
                chain.addPathDefinition(requestMap.getUrl(), IS_AUTHENTICATED);
            } else {
                String str = "roles[" + requestMap.getConfigAttribute() + "]";
                chain.addPathDefinition(requestMap.getUrl(), str);
            }
        }
        // shiro 提供的登出过滤器，访问指定的请求，就会执行登录，默认跳转路径是"/"，或者是"shiro.loginUrl"配置的内容
        // 由于application-shiro.yml中配置了 shiro:loginUrl: /page/401，返回会返回对应的json内容
        // 可以结合/user/login和/t1/js接口来测试这个/t4/logout接口是否有效
        chain.addPathDefinition("/logout", "anon,logout");
        chain.addPathDefinition("/login", "anon");

        //其它路径均不需要登录
        chain.addPathDefinition("/**", "anon");

        return chain;
    }
}
