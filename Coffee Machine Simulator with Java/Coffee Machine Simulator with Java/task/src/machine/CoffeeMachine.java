package machine;

import java.util.Scanner;

public class CoffeeMachine {

    int moneyDollars = 550;
    int waterMl = 400;
    int milkMl = 540;
    int coffeeG = 120;
    int cupsDisp = 9;
    int counterOfTenCoffee = 0;
    Scanner scanner = new Scanner(System.in);

    public void buy(String coffeeType) {
        if (coffeeType.equals("1")) {
            if (waterMl < 250) {
                System.out.println("Sorry, not enough water!");
            } else if (coffeeG < 16) {
                System.out.println("Sorry, not enough coffee beans!");
            } else if (cupsDisp < 1) {
                System.out.println("Sorry, not enough disposable cups!");
                ;
            } else {
                waterMl -= 250;
                coffeeG -= 16;
                moneyDollars += 4;
                cupsDisp -= 1;
                counterOfTenCoffee++;
                System.out.println("I have enough resources, making you a coffee!");
            }
        } else if (coffeeType.equals("2")) {
            if (waterMl < 350) {
                System.out.println("Sorry, not enough water!");
            } else if (milkMl < 75) {
                System.out.println("Sorry, not enough milk!");
                ;
            } else if (coffeeG < 20) {
                System.out.println("Sorry, not enough coffee beans!");
            } else if (cupsDisp < 1) {
                System.out.println("Sorry, not enough disposable cups!");
            } else {
                waterMl -= 350;
                milkMl -= 75;
                coffeeG -= 20;
                moneyDollars += 7;
                cupsDisp -= 1;
                counterOfTenCoffee++;
                System.out.println("I have enough resources, making you a coffee!");
            }
        } else if (coffeeType.equals("3")) {
            if (waterMl < 200) {
                System.out.println("Sorry, not enough water!");
            } else if (milkMl < 100) {
                System.out.println("Sorry, not enough milk!");
            } else if (coffeeG < 12) {
                System.out.println("Sorry, not enough coffee beans!");
            } else if (cupsDisp < 1) {
                System.out.println("Sorry, not enough disposable cups!");
            } else {
                waterMl -= 200;
                milkMl -= 100;
                coffeeG -= 12;
                moneyDollars += 6;
                cupsDisp -= 1;
                counterOfTenCoffee++;
                System.out.println("I have enough resources, making you a coffee!");
            }
        }
    }

        public void fill(int addWater, int addMilk, int addCoffee, int addCups) {
        waterMl += addWater;
        milkMl += addMilk;
        coffeeG += addCoffee;
        cupsDisp += addCups;
    }

    public void take() {
        System.out.printf("I gave you $%d\n", moneyDollars);
        moneyDollars = 0;
    }

    public void displayState() {
        System.out.printf("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money""", waterMl, milkMl, coffeeG, cupsDisp, moneyDollars);
    }

    public void run() {

        while (true) {

            System.out.println("\nWrite action (buy, fill, take, clean, remaining, exit):\n");
            String action = scanner.nextLine();

            if (action.equals("exit")) {
                break;
            }
            if (action.equals("remaining")) {
                displayState();
            } else if (action.equals("buy")) {
                if (counterOfTenCoffee == 10) {
                    System.out.println("I need cleaning!");
                    continue;
                }
                System.out.println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
                String coffeeType = scanner.nextLine();
                buy(coffeeType);
            } else if (action.equals("fill")) {
                System.out.println("\nWrite how many ml of water you want to add:");
                int addWater = scanner.nextInt();
                System.out.println("Write how many ml of milk you want to add:");
                int addMilk = scanner.nextInt();
                System.out.println("Write how many grams of coffee beans you want to add:");
                int addCoffee = scanner.nextInt();
                System.out.println("Write how many disposable cups you want to add:");
                int addCups = scanner.nextInt();
                scanner.nextLine();
                fill(addWater, addMilk, addCoffee, addCups);
            } else if (action.equals("take")) {
                take();
            } else if (action.equals("clean")) {
                counterOfTenCoffee = 0;
                System.out.println("I have been cleaned!");
            }
        }
        scanner.close();
    }
}