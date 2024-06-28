package com.web.springboot.service;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MinIOService {

    @Resource
    private MinioClient minioClient;

    public void testMinIOClient() {
        System.out.println(minioClient);
    }
}
