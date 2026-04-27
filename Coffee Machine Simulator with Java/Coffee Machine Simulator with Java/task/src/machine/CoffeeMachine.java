package machine;

import java.util.Scanner;
import machine.coffee.Cappuccino;
import machine.coffee.Espresso;
import machine.coffee.Latte;

public class CoffeeMachine {

    private int moneyDollars = 550;
    private int waterMl = 400;
    private int milkMl = 540;
    private int coffeeG = 120;
    private int cupsDisp = 9;
    private int counterOfTenCoffee = 0;
    private Cappuccino cappuccino = new Cappuccino();
    private Espresso espresso = new Espresso();
    private Latte latte = new Latte();

    public int getMoneyDollars() {
        return moneyDollars;
    }

    public void setMoneyDollars(int moneyDollars) {
        this.moneyDollars = moneyDollars;
    }

    public int getWaterMl() {
        return waterMl;
    }

    public void setWaterMl(int waterMl) {
        this.waterMl = waterMl;
    }

    public int getMilkMl() {
        return milkMl;
    }

    public void setMilkMl(int milkMl) {
        this.milkMl = milkMl;
    }

    public int getCoffeeG() {
        return coffeeG;
    }

    public void setCoffeeG(int coffeeG) {
        this.coffeeG = coffeeG;
    }

    public int getCupsDisp() {
        return cupsDisp;
    }

    public void setCupsDisp(int cupsDisp) {
        this.cupsDisp = cupsDisp;
    }

    public int getCounterOfTenCoffee() {
        return counterOfTenCoffee;
    }

    public void setCounterOfTenCoffee(int counterOfTenCoffee) {
        this.counterOfTenCoffee = counterOfTenCoffee;
    }

    Scanner scanner = new Scanner(System.in);

    public void buy(String coffeeType) {
        switch (coffeeType) {
            case "1" -> espresso.makeCoffee(this);
            case "2" -> latte.makeCoffee(this);
            case "3" -> cappuccino.makeCoffee(this);
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