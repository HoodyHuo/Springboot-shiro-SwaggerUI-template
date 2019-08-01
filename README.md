# Springboot-shiro-SwaggerUI-template

> 模板项目  
便于快速开始项目  
集成包括:  
`Shiro`     安全组件  
`JPA+MySql` 数据库部分  
`Swagger`   API  
`logs`      日志配置

## 依赖列表

|名称|版本|备注
|-|-|-|
|Java|1.8|-|
|Springboot|2.1.6.RELEASE|-|
|spring-boot-starter-web|2.1.6.RELEASE|MVC|
|spring-boot-starter-data-jpa|2.1.6.RELEASE|数据库操作|
|spring-boot-starter-test|2.1.6.RELEASE|-|
|mysql-connector-java|8.0.16|mysql driver|
|shiro-spring-boot-web-starter|1.4.0|shiro|
|groovy-all|2.5.7|Groovy+util|
|springfox-swagger2|2.9.2|-|
|springfox-swagger-ui|2.9.2|-|

># 集成过程

## 集成Jpa+mysql
数据库初始化SQL  `db.sql`  
1.引入依赖

````xml
        <!--spring Data JPA-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!--MySQL 驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
            <scope>runtime</scope>
        </dependency>
````  

### 2.配置`application.yml`

````yml
spring:
#----------------------database-------------------------
  datasource:
    url: 'jdbc:mysql://localhost:3306/dbname?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&useSSL=true'
    username: 'root'
    password: 'AbcAbc'
    driverClassName: 'com.mysql.cj.jdbc.Driver'
  #----------------------JPA------------------------------
  jpa:
    database: MYSQL
    show-sql: false # Show or not log for each sql query
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    # Hibernate ddl auto (create, create-drop, update.none)
    #create： 每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。
    #create-drop ：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。
    #update：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据 model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等 应用第一次运行起来后才会。
    #validate ：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL5InnoDBDialect
````

## 集成shiro

> 这里只展示了部分配置  
相关用到的`User`,`UserRepository`,`User注册.登录的控制器和服务`请在源码查看  

### 1.引入依赖

````xml
     <!-- shiro -->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring-boot-web-starter</artifactId>
            <version>1.4.0</version>
        </dependency>
````

### 2.创建自定义Realm

`/src/main/groovy/vip/hoody/pi/shiro/CustomRealm.groovy`

````groovy
package vip.hoody.pi.shiro

import org.apache.shiro.authc.*
import org.apache.shiro.authz.AuthorizationException
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.springframework.beans.factory.annotation.Autowired
import vip.hoody.pi.domain.Role
import vip.hoody.pi.domain.User
import vip.hoody.pi.service.AuthService
import vip.hoody.pi.service.UserService

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
        //SecurityUtils.getSubject().getPrincipal()//就能拿出用户的所有信息，包括角色和权限
        Set<Role> roles = authService.findRolesByUser(user)
        Set<String> roleStrSet = roles.role
        Set<String> permsStrSet = authService.findPermissionByRoles(roles).permission

        /** 获取用户权限和角色*/
        user.getRoles().addAll(roleStrSet)
        user.getPerms().addAll(permsStrSet)

        /** 将用户输入密码使用盐值进行加密*/
        def encodePassword = authService.getInputPasswordCiph(upToken.getPassword().toString(), user.salt)
        upToken.setPassword(encodePassword.toCharArray())

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.password, getName())
        return info
    }
}
````

### 3.创建自定义Session管理对象

`/src/main/groovy/vip/hoody/pi/shiro/CustomSessionManager.groovy`

````groovy
package vip.hoody.pi.shiro

import org.apache.shiro.web.servlet.ShiroHttpServletRequest
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager
import org.apache.shiro.web.util.WebUtils
import org.springframework.util.StringUtils

import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

/**
 *  @author Hoody
 * 自定义sessionId获取方式
 * 从前端发送的header中获取SessionId,如果没有再从cookie中读取
 */
class CustomSessionManager extends DefaultWebSessionManager {

    /** 存放 sessionID 的header key */
    private static final String AUTHORIZATION = "X-Token"

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request"

    /**
     * 重写getSessionId方法, 从前端发送的header中获取SessionId,如果没有再从cookie中读取
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION)
        //如果请求头中有 Authorization 则其值为sessionId
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE)
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id)
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE)
            return id
        } else {
            //否则按默认规则从cookie取sessionId
            return super.getSessionId(request, response)
        }
    }
}
````

### 4.创建配置bean`ShiroConfig.groovy`

`/src/main/groovy/vip/hoody/pi/config/ShiroConfig.java`

````java
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
    public CustomRealm customRealm() String pwd) {
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
        //注入自定义realm
        securityManager.setRealm(customRealm);
        //注入自定义session管理
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
     * perms                 PermissionsAuthorizationFilter     需要指定权限才能访问
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
        /** 读取RequestMap数据表,将已经配置的路径加入Shiro管理,可通过数据库管理路径 */
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

        //配置其它路径均不需要登录,
        //在controller中加入shiro注解会生效,这是数据库配置与注解配置同时使用的方式
        chain.addPathDefinition("/**", "anon");
        return chain;
    }
}
````

## 集成Swagger-UI,进行API管理

### 1.添加依赖

````xml
        <!--swagger2 API 生成依赖-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>com.vaadin.external.google</groupId>
            <artifactId>android-json</artifactId>
            <version>0.0.20131108.vaadin1</version>
            <scope>compile</scope>
        </dependency>
````

### 2.创建配置bean `SwaggerConfig`

`/src/main/groovy/vip/hoody/pi/config/SwaggerConfig.java`

````java
package vip.hoody.pi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //swagger要扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("vip.hoody.pi.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /** 配置 在Swagger 界面显示的API信息 */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger 测试")
                .description("后端API测试")
                .termsOfServiceUrl("localhost:8080/api")
                .contact(new Contact("Swagger测试", "localhost:8080/api/swagger-ui.html", "xx@163.com"))
                .version("1.0")
                .build();
    }
}
````

### 3.使用Nginx进行转发时的配置

`application.yml`
````yml
#解决SwaggerUI 执行测试的问题，不过访问v2/api-doc 是通过nginx强制重定向完成的
springfox:
  documentation:
    swagger:
      v2:
        host: 'pi.hoody.vip/api'
````

`nginx.conf`
````nginx
 location = /api/v2/api-docs {  
            proxy_set_header   Host             $host;
            proxy_set_header   X-Real-IP        $remote_addr;
            proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
            proxy_set_header   X-Forwarded-Proto    $scheme;
            proxy_set_header   Referer $http_referer;

            proxy_pass http://localhost:8080/v2/api-docs;
 	    }
````

## 修改Log日志配置

### 1.创建日志配置文件

`/src/main/resources/logback-spring.xml`

````xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="10 seconds">
    <contextName>logback</contextName>
    <!-- 格式化输出：%date表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度 %msg：日志消息，%n是换行符-->
    <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{50} -%msg%n"/>

    <!-- 定义日志存储的路径 根据platform.logsPath 属性进行匹配-->
    <springProperty scope="context" name="LOGS_Path" source="platform.logsPath"/>
    <property name="FILE_PATH" value="${LOGS_Path}}"/>

    <!-- 控制台输出日志 -->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <!-- 日志级别过滤INFO以下 -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
        <encoder>
            <!-- 按照上面配置的LOG_PATTERN来打印日志 -->
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!--每天生成一个日志文件，保存30天的日志文件。rollingFile用来切分文件的 -->
    <appender name="rollingFile" class="ch.qos.logback.core.rolling.RollingFileAppender">

        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">

            <fileNamePattern>${FILE_PATH}/info/CPControl-infoLog.%d{yyyy-MM-dd}.%i.log</fileNamePattern>

            <!-- keep 15 days' worth of history -->
            <maxHistory>30</maxHistory>

            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- 日志文件的最大大小 -->
                <maxFileSize>2MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>

            <!-- 超出删除老文件 -->
            <totalSizeCap>1GB</totalSizeCap>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!--每天生成一个日志文件，保存30天的日志文件。rollingFile用来切分文件的 -->
    <appender name="error" class="ch.qos.logback.core.rolling.RollingFileAppender">

        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">

            <fileNamePattern>${FILE_PATH}/error/CPControl-errorLog.%d{yyyy-MM-dd}.%i.log</fileNamePattern>

            <!-- keep 15 days' worth of history -->
            <maxHistory>30</maxHistory>

            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <!-- 日志文件的最大大小 -->
                <maxFileSize>30MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>

            <!-- 超出删除老文件 -->
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <!-- project default level  发布时 要修改-->
    <logger name="vip.hoody" level="DEBUG"/>

    <!-- 日志输出级别 -->
    <root>
        <level value="info"/>
        <appender-ref ref="rollingFile"/>
        <level value="error"/>
        <appender-ref ref="error"/>
        <level value="info"/>
        <appender-ref ref="console"/>
    </root>
</configuration>
````

### 2.修改application配置添加读取日志配置

`application.yml`

````yml
  #-------------------------------log 日志----------------------
logging:
  config: 'classpath:logback-spring.xml'
````
