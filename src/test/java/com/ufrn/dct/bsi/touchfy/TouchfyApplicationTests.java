package com.ufrn.dct.bsi.touchfy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "spring.flyway.enabled=true",
      "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
      "spring.datasource.username=sa",
      "spring.datasource.password=",
      "jwt.access.secret=8cABD04wFPGJ5R//OH/pv1/7JUwkl4l2/VmkF996Uak=",
      "jwt.access.expiration=300000",
      "jwt.refresh.secret=ONnbOgTffqv48saB7YR3dUcsiuJv8Ynz8y+MH8n+Gws=",
      "jwt.refresh.expiration=604800000",
      "storage.endpoint=http://localhost:9000",
      "storage.access-key=test",
      "storage.secret-key=test",
      "storage.bucket-name=test-bucket",
      "storage.bucket-url=http://localhost:9000/test-bucket",
      "storage.region=test-region"
    })
class TouchfyApplicationTests {

  @Test
  void contextLoads() {}
}