package com.ir.project.client;

import java.util.Scanner;

public class Firas {
	public static void main(String[] args) {

		String name = "Fatima";

		Scanner scanner = new Scanner(System.in);

		int numberOfAttemp = 3;

		int numberOfSucced = 0;

		while (numberOfAttemp > 0) {

			System.out.print("Try a Character: ");
			char ch = scanner.next().charAt(0);

			if (name.toLowerCase().contains(String.valueOf(ch))) {
				numberOfSucced++;
				int count = name.length() - numberOfSucced;
				if (count == 0){
					System.out.println("Congratulation you Win !!!");
					break;
				}
				else
					System.out.println("Correct you still have " + count + " Chraracter");

			}

			else {
				numberOfAttemp--;
				if (numberOfAttemp == 0)
					System.out.println("Sorry you loose please try Again");
				else
					System.out.println("Wrong you still have " + numberOfAttemp + " Attemp");
			}
		}
		scanner.close();

	}
}
