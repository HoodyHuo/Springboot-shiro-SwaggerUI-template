package vip.hoody.pi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Hoody
 * @since 2019年4月27日
 * SpringBoot Xml 配置文件引入，及架构Spring配置类
 * bean配置尽量按模块单独引入xml文件。
 */
@Configuration
@ImportResource(locations = {"classpath:application-bean.xml"})
public class PlatformConfig {

}
