package com.ufrn.dct.bsi.touchfy.infrastructure.config;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.storage.S3FileStorageImpl;
import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.infrastructure.storage.StorageProperties;
import java.net.URI;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {

  @Bean
  public S3Client s3Client(final StorageProperties properties) {
    final AwsBasicCredentials credentials =
        AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey());

    return S3Client.builder()
        .endpointOverride(URI.create(properties.getEndpoint()))
        .region(Region.of(properties.getRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .forcePathStyle(true)
        .serviceConfiguration(
            S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(false)
                .build())
        .build();
  }

  @Bean
  public FileStorageService fileStorageService(
      final S3Client s3Client, final StorageProperties properties) {
    return new S3FileStorageImpl(s3Client, properties.getBucketName(), properties.getBucketUrl());
  }
}
