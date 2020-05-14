package fr.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.project.model.Monster;

@SpringBootTest
class FakemonBootApplicationTests {

	@Autowired
	FakemonBootApplication aap;
	
	
	@Test
	void contextLoads() {
	}

	@Test
	void calcPointsEquipeTestMonolv1() {
		Monster m1 = new Monster();
		LinkedList<Monster> lm = new LinkedList<Monster>();
		lm.add(m1);
		int i = aap.calcPointsEquipe(lm);
		assertEquals(3, i);
	}
		
	@Test
	void calcPointsEquipeTestMonolv4() {
		Monster m1 = new Monster();
		m1.setLevel(4);
		LinkedList<Monster> lm = new LinkedList<Monster>();
		lm.add(m1);
		int i = aap.calcPointsEquipe(lm);
		assertEquals((3+5+10+16), i);
	}
	
	@Test
	void calcPointsEquipeTestMonolv10() {
		Monster m1 = new Monster();
		m1.setLevel(10);
		LinkedList<Monster> lm = new LinkedList<Monster>();
		lm.add(m1);
		int i = aap.calcPointsEquipe(lm);
		assertEquals((3+5+10+16+23+31+40+50+61+73), i);
	}
	
	@Test
	void calcPointsEquipeTestMultilv1() {
		Monster m1 = new Monster();
		Monster m2 = new Monster();
		LinkedList<Monster> lm = new LinkedList<Monster>();
		lm.add(m1); lm.add(m2);
		int i = aap.calcPointsEquipe(lm);
		assertEquals(6, i);
	}
	
	@Test
	void calcPointsEquipeTestMultilv3() {
		Monster m1 = new Monster();
		m1.setLevel(3);
		Monster m2 = new Monster();
		m2.setLevel(3);
		LinkedList<Monster> lm = new LinkedList<Monster>();
		lm.add(m1); lm.add(m2);
		int i = aap.calcPointsEquipe(lm);
		assertEquals(36, i);
	}
	
	@Test
	void calcPointsEquipeTestMultilvMix() {
		Monster m1 = new Monster();
		m1.setLevel(5);
		Monster m2 = new Monster();
		m2.setLevel(8);
		Monster m3 = new Monster();
		m3.setLevel(7);
		Monster m4 = new Monster();
		m4.setLevel(2);
		LinkedList<Monster> lm = new LinkedList<Monster>();
		lm.add(m1); lm.add(m2);lm.add(m3); lm.add(m4);
		int i = aap.calcPointsEquipe(lm);
		assertEquals(371, i);
	}
	
}
