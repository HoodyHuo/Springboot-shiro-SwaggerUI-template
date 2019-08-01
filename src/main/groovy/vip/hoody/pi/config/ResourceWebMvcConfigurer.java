package vip.hoody.pi.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 开发模式下,映射本地目录到静态资源
 * 部署 prod模式下,使用Nginx进行静态资源映射
 * @author  Hoody
 */
@Configuration
@Profile(value = "dev")
public class ResourceWebMvcConfigurer implements WebMvcConfigurer {

    private String staticPath;

    public ResourceWebMvcConfigurer(@Value("${platform.staticPath}") String path) {
        this.staticPath = path;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("addResource dir :" + staticPath);
        registry.addResourceHandler("/storage/**")
                .addResourceLocations("file:/" + staticPath);
    }
}
