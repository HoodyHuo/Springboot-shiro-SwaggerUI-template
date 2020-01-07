package tech.hoody.platform

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties
class PlatformApplication {

    static void main(String[] args) {
        SpringApplication.run(PlatformApplication, args)
    }
}
