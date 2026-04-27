package machine.coffee;

import machine.CoffeeMachine;

public class Latte extends Coffee {

    @Override
    public void makeCoffee(CoffeeMachine coffeeMachine) {
        if (coffeeMachine.getWaterMl() < 350) {
            System.out.println("Sorry, not enough water!");
        } else if (coffeeMachine.getMilkMl() < 75) {
            System.out.println("Sorry, not enough milk!");
        } else if (coffeeMachine.getCoffeeG() < 20) {
            System.out.println("Sorry, not enough coffee beans!");
        } else if (coffeeMachine.getCupsDisp() < 1) {
            System.out.println("Sorry, not enough disposable cups!");
        } else {
            coffeeMachine.setWaterMl(coffeeMachine.getWaterMl() - 350);
            coffeeMachine.setMilkMl(coffeeMachine.getMilkMl() - 75);
            coffeeMachine.setCoffeeG(coffeeMachine.getCoffeeG() - 20);
            coffeeMachine.setMoneyDollars(coffeeMachine.getMoneyDollars() + 7);
            coffeeMachine.setCupsDisp(coffeeMachine.getCupsDisp() - 1);
            coffeeMachine.setCounterOfTenCoffee(coffeeMachine.getCounterOfTenCoffee() + 1);
            System.out.println("I have enough resources, making you a coffee!");
        }
    }
}
