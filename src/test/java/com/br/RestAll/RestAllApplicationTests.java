package com.br.RestAll;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfig.class)
class RestAllApplicationTests {

	@Test
	void contextLoads() {
	}

}
