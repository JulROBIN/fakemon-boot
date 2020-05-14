package fr.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FakemonBootApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testMonstre() {
		MonsterEntity me = cs.tableRencontre(1).get(0);
		System.out.println(me);
		assertNull(me);
	}

}
