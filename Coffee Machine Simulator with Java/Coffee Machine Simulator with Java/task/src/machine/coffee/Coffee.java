package machine.coffee;

import machine.CoffeeMachine;

public abstract class Coffee {

    private final int waterNeeded;
    private final int milkNeeded;
    private final int beensNeeded;
    private final int price;

    protected Coffee(int water, int milk, int beens, int price) {
        this.waterNeeded = water;
        this.milkNeeded = milk;
        this.beensNeeded = beens;
        this.price = price;
    }

    public void makeCoffee(CoffeeMachine machine) {
        if (machine.getWaterMl() < waterNeeded) {
            System.out.println("Sorry, not enough water!");
            return;
        } else if (machine.getMilkMl() < milkNeeded) {
            System.out.println("Sorry, not enough milk!");
            return;
        } else if (machine.getCoffeeG() < beensNeeded) {
            System.out.println("Sorry, not enough coffee beans!");
            return;
        } else if (machine.getCupsDisp() < 1) {
            System.out.println("Sorry, not enough disposable cups!");
            return;
        } else {
            machine.setWaterMl(machine.getWaterMl() - waterNeeded);
            machine.setMilkMl(machine.getMilkMl() - milkNeeded);
            machine.setCoffeeG(machine.getCoffeeG() - beensNeeded);
            machine.setCupsDisp(machine.getCupsDisp() - 1);
            machine.setMoneyDollars(machine.getMoneyDollars() + price);
            machine.setCounterOfTenCoffee(machine.getCounterOfTenCoffee() + 1);
            System.out.println("I have enough resources, making you a coffee!");
        }
    }
}
