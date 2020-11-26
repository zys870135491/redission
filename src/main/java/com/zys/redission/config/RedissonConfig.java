package com.zys.redission.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redisson() throws IOException {

        // 默认连接地址 127.0.0.1:6379
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
        RedissonClient redisson = Redisson.create(config);
        return redisson;

/*        Config config = new Config();
        config.useSingleServer().setAddress("127.0.0.1:6379");
        RedissonClient redisson = Redisson.create(config);
        return  redisson;*/
    }


}
