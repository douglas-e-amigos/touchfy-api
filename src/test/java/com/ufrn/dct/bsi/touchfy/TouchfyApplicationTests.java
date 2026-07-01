package com.ufrn.dct.bsi.touchfy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    properties = {
      "spring.flyway.enabled=true",
      "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
      "spring.datasource.username=sa",
      "spring.datasource.password="
    })
class TouchfyApplicationTests {

  @Test
  void contextLoads() {}
}
