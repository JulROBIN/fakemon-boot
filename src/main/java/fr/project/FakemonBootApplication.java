package fr.project;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.project.model.MonsterEntity;

@SpringBootApplication
public class FakemonBootApplication {
	
	public static int saisieInt(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		int i = sc.nextInt();
		return i;
	}
	public static String saisieString(String msg) {
		Scanner sc = new Scanner(System.in);
		System.out.println(msg);
		String i = sc.next();
		return i;
	}	
	public static void main(String[] args) {
		SpringApplication.run(FakemonBootApplication.class, args);
		MonsterEntity m = new MonsterEntity();
	}

}
