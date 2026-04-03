package calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int bubblegumEarned = 202;
        int toffeeEarned = 118;
        int iceCreamEarned = 2250;
        int milkChocolateEarned = 1680;
        int doughnutEarned = 1075;
        int pancakeEarned = 80;

        double earnedAmount = bubblegumEarned + toffeeEarned + iceCreamEarned + milkChocolateEarned + doughnutEarned + pancakeEarned;

        System.out.println("Earned amount:");
        System.out.println("Bubblegum: $" + bubblegumEarned);
        System.out.println("Toffee: $ " + toffeeEarned);
        System.out.println("Ice cream: $" + iceCreamEarned);
        System.out.println("Milk chocolate: $" + milkChocolateEarned);
        System.out.println("Doughnut: $" + doughnutEarned);
        System.out.println("Pancake: $" + pancakeEarned);
        System.out.println();
        System.out.println("Income: $" + earnedAmount);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Staff expenses:");
        int staffExpenses = scanner.nextInt();

        System.out.println("Other expenses:");
        int otherExpenses = scanner.nextInt();

        int netIncome = (int) earnedAmount - staffExpenses - otherExpenses;
        System.out.printf("Net income: $%d", netIncome);

        scanner.close();
    }
}