package io.github.soupedog.config;

import feign.Contract;
import feign.Logger;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;

public class FeignConfig {
    @Bean
    public Contract feignConfiguration() {
        return new SpringMvcContract();
    }

    // 日志是 debug 级别，你要想观测到结果，日志输出级别要注意
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}