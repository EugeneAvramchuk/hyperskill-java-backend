package machine.coffee;

import machine.CoffeeMachine;

public class Espresso extends Coffee {

    @Override
    public void makeCoffee(CoffeeMachine coffeeMachine) {
        if (coffeeMachine.getWaterMl() < 250) {
            System.out.println("Sorry, not enough water!");
        } else if (coffeeMachine.getCoffeeG() < 16) {
            System.out.println("Sorry, not enough coffee beans!");
        } else if (coffeeMachine.getCupsDisp() < 1) {
            System.out.println("Sorry, not enough disposable cups!");
        } else {
            coffeeMachine.setWaterMl(coffeeMachine.getWaterMl() - 250);
            coffeeMachine.setCoffeeG(coffeeMachine.getCoffeeG() - 16);
            coffeeMachine.setMoneyDollars(coffeeMachine.getMoneyDollars() + 4);
            coffeeMachine.setCupsDisp(coffeeMachine.getCupsDisp() - 1);
            coffeeMachine.setCounterOfTenCoffee(coffeeMachine.getCounterOfTenCoffee() + 1);
            System.out.println("I have enough resources, making you a coffee!");
        }
    }
}
