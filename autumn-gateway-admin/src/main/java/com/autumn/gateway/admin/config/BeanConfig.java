package com.autumn.gateway.admin.config;

import com.autumn.gateway.admin.user.entity.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qiushi
 * @program autumn-gateway
 * @description
 * @since 2022-07-18 13:56
 */
@Configuration
public class BeanConfig {

    static {
        System.out.println(1111);
    }

    @Bean
    public UserInfo agwUserInfo() {
        return new UserInfo().setId(1);
    }
}
