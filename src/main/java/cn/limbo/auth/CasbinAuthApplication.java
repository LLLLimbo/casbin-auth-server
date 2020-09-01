package cn.limbo.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "cn.limbo.auth.repo")
//@EnableEurekaClient
//@EnableFeignClients(basePackages ={"com.limbo.auth"})
public class CasbinAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasbinAuthApplication.class, args);
    }

}
