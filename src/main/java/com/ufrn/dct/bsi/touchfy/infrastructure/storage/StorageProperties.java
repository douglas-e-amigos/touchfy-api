package com.ufrn.dct.bsi.touchfy.infrastructure.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
  private String endpoint;
  private String accessKey;
  private String secretKey;
  private String bucketName;
  private String bucketUrl;
  private String region;
}
