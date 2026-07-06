package com.youthlink.server;

import com.youthlink.server.config.TestAiConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestAiConfig.class)
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
