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