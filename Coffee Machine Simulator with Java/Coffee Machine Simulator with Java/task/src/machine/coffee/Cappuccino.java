package machine.coffee;

import machine.CoffeeMachine;

public final class Cappuccino extends Coffee {

    @Override
    public void makeCoffee(CoffeeMachine coffeeMachine) {
        if (coffeeMachine.getWaterMl() < 200) {
            System.out.println("Sorry, not enough water!");
        } else if (coffeeMachine.getMilkMl() < 100) {
            System.out.println("Sorry, not enough milk!");
        } else if (coffeeMachine.getCoffeeG() < 12) {
            System.out.println("Sorry, not enough coffee beans!");
        } else if (coffeeMachine.getCupsDisp() < 1) {
            System.out.println("Sorry, not enough disposable cups!");
        } else {
            coffeeMachine.setWaterMl(coffeeMachine.getWaterMl() - 200);
            coffeeMachine.setMilkMl(coffeeMachine.getMilkMl() - 100);
            coffeeMachine.setCoffeeG(coffeeMachine.getCoffeeG() - 12);
            coffeeMachine.setMoneyDollars(coffeeMachine.getMoneyDollars() + 6);
            coffeeMachine.setCupsDisp(coffeeMachine.getCupsDisp() - 1);
            coffeeMachine.setCounterOfTenCoffee(coffeeMachine.getCounterOfTenCoffee() + 1);
            System.out.println("I have enough resources, making you a coffee!");
        }
    }
}
