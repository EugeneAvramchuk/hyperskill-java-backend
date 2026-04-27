package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {

        System.out.println("""
                                 Cinema:
                                   1 2 3 4 5 6 7 8
                                 1 S S S S S S S S
                                 2 S S S S S S S S
                                 3 S S S S S S S S
                                 4 S S S S S S S S
                                 5 S S S S S S S S
                                 6 S S S S S S S S
                                 7 S S S S S S S S""");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        int numberOfRows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int numberOfSeats = scanner.nextInt();
        int totalIncome = 0;
        if (numberOfRows * numberOfSeats <= 60) {
            totalIncome = 10 * numberOfRows * numberOfSeats;
        } else {
            totalIncome = (10 * (numberOfRows / 2) * numberOfSeats) + (8 * (numberOfRows - numberOfRows / 2) * numberOfSeats); // Write your code here
        }
        System.out.println("""
                    Total income:
                    $""" + totalIncome);
    }
}