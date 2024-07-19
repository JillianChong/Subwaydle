package com.example.subwaydle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SubwaydleApplication {

	private static Checker check;

	public static void main(String[] args) {
		SpringApplication.run(SubwaydleApplication.class, args);
	}

	public static String[] generatePathToController() {
		String[] pathInfo = Main.generatePath();
		String path = Main.printPath(pathInfo);
		System.out.println(path);

		// TODO: Implement keyboard box coloring
		check = new Checker(pathInfo[0], pathInfo[1], pathInfo[2]);

		return pathInfo;
	}

	public static int[] sendAnswerToController(char[] guess) {
		return check.checkAnswer(guess);
	}

}
