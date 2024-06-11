package com.web.springboot.config;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Resource
    private MinIOInfo minIOInfo;

    //单例的，那么MinioClient对象有没有线程安全问题呢？答案是：没有线程安全问题
    @Bean
    public MinioClient minioClient() {
        //链式编程，构建MinioClient对象
        return MinioClient.builder()
                .endpoint(minIOInfo.getEndpoint())
                .credentials(minIOInfo.getAccessKey(), minIOInfo.getSecretKey())
                .build();
    }
}
