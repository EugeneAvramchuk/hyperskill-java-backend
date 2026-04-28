# OOP Refactoring — Coffee Machine Simulator

**Language:** English | [Українська версія](OOP_REFACTORING_uk.md)

**Reviewer:** Claude (Sonnet 4.5)
**Review date:** April 28, 2026
**Project state at review:** after the first OOP refactoring stage (extracting coffee types into separate classes inheriting from abstract `Coffee`)

---

## Table of Contents

1. [What's done well](#1-whats-done-well)
2. [Four levels of further improvement](#2-four-levels-of-further-improvement)
3. [Level 1 — eliminate duplication in coffee classes](#level-1--eliminate-duplication-in-coffee-classes)
4. [Level 2 — Tell, Don't Ask](#level-2--tell-dont-ask)
5. [Level 3 — separate method for resource checking](#level-3--separate-method-for-resource-checking)
6. [Level 4 — composition of smaller objects](#level-4--composition-of-smaller-objects)
7. [Progress checklist](#progress-checklist)
8. [Overall recommendation](#overall-recommendation)

---

## 1. What's done well

Before talking about improvements — fixing what's already on the right track. This matters because refactoring never starts from scratch; it builds on what's already done.

| What | Why this is good |
|---|---|
| Coffee types extracted into separate classes (`Espresso`, `Latte`, `Cappuccino`) inheriting from abstract `Coffee` | Fundamental OOP step. Instead of a string parameter — proper objects that know their own recipe. Encapsulation of knowledge at the right level. |
| Subpackage `coffee` inside `machine` | Logical code grouping. Files organized by content. |
| Child classes marked as `final` | "This class is not meant for further inheritance" — sensible decision for concrete implementations. |
| `Main` is minimal — only creates the machine and starts it | Correct principle: entry point should do nothing but launch. |
| "Needs cleaning" state (`counterOfTenCoffee == 10`) checked in `run()`, not inside coffee | Correct responsibility split: coffee doesn't decide whether to be brewed — the machine decides whether it's ready to brew. |

---

## 2. Four levels of further improvement

Levels are ordered by decreasing significance: Level 1 is the most useful, Level 4 is the most abstract.

| Level | Name | Effort | Learning value |
|---|---|---|---|
| 1 | Eliminate duplication | ~30 min | **Very high** |
| 2 | Tell, Don't Ask | ~1 hr | **High** |
| 3 | Separate resource-check method | ~30 min | Medium |
| 4 | Composition of smaller objects | ~2 hrs | High (but mature OOP) |

---

## Level 1 — eliminate duplication in coffee classes

### What we see now

Three classes (`Espresso`, `Latte`, `Cappuccino`) are almost identical. They differ only in numbers (resource volumes, price) and one detail (Espresso has no milk).

The structure, validation logic, error messages, deduction sequence — identical in all three. This is **classic code duplication**.

### Which OOP principle this violates

**DRY** (Don't Repeat Yourself). When identical logic lives in three places, any change to that logic (e.g. a new error message, a new check) must be made three times. Three times means three chances to make a mistake.

### How to fix

Move common logic into the **parent class** `Coffee`. Concrete classes pass their numbers via constructor. This is called the **Template Method pattern** — parent has the general algorithm, children supply parameters.

#### Before (Espresso.java)

```java
public final class Espresso extends Coffee {
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
```

The same is repeated in `Latte.java` and `Cappuccino.java`.

#### After (Coffee.java)

```java
public abstract class Coffee {
    private final int waterNeeded;
    private final int milkNeeded;
    private final int beansNeeded;
    private final int price;

    protected Coffee(int water, int milk, int beans, int price) {
        this.waterNeeded = water;
        this.milkNeeded = milk;
        this.beansNeeded = beans;
        this.price = price;
    }

    public void makeCoffee(CoffeeMachine machine) {
        if (machine.getWaterMl() < waterNeeded) {
            System.out.println("Sorry, not enough water!");
            return;
        }
        if (milkNeeded > 0 && machine.getMilkMl() < milkNeeded) {
            System.out.println("Sorry, not enough milk!");
            return;
        }
        if (machine.getCoffeeG() < beansNeeded) {
            System.out.println("Sorry, not enough coffee beans!");
            return;
        }
        if (machine.getCupsDisp() < 1) {
            System.out.println("Sorry, not enough disposable cups!");
            return;
        }
        machine.setWaterMl(machine.getWaterMl() - waterNeeded);
        machine.setMilkMl(machine.getMilkMl() - milkNeeded);
        machine.setCoffeeG(machine.getCoffeeG() - beansNeeded);
        machine.setMoneyDollars(machine.getMoneyDollars() + price);
        machine.setCupsDisp(machine.getCupsDisp() - 1);
        machine.setCounterOfTenCoffee(machine.getCounterOfTenCoffee() + 1);
        System.out.println("I have enough resources, making you a coffee!");
    }
}
```

#### After (Espresso.java, Latte.java, Cappuccino.java)

```java
public final class Espresso extends Coffee {
    public Espresso() { super(250, 0, 16, 4); }
}

public final class Latte extends Coffee {
    public Latte() { super(350, 75, 20, 7); }
}

public final class Cappuccino extends Coffee {
    public Cappuccino() { super(200, 100, 12, 6); }
}
```

Each class collapses to a single line — a constructor passing its parameters to the parent.

### Alternative — use enum

Since coffee types are a **fixed limited set**, you can replace three classes with one `enum`:

```java
public enum CoffeeType {
    ESPRESSO(250, 0, 16, 4),
    LATTE(350, 75, 20, 7),
    CAPPUCCINO(200, 100, 12, 6);

    private final int water;
    private final int milk;
    private final int beans;
    private final int price;

    CoffeeType(int water, int milk, int beans, int price) {
        this.water = water;
        this.milk = milk;
        this.beans = beans;
        this.price = price;
    }

    public void makeCoffee(CoffeeMachine machine) {
        // same logic as Coffee.makeCoffee()
    }
}
```

**Which approach to choose:** for learning purposes, first do Template Method (abstract class + descendants) to reinforce the inheritance concept. Then, as a separate exercise, try the enum variant. Both are important OOP forms worth mastering.

### What changes in code after Level 1

- Line count in three coffee classes drops from ~25 to 1 per class.
- Any change to brewing logic happens in one place.
- It becomes obvious that coffee types are **data** (numbers in constructor), not **code** (repeated logic).

### Theoretical references

- **DRY** — Don't Repeat Yourself, from *The Pragmatic Programmer* (Hunt & Thomas, 1999)
- **Template Method Pattern** — from *Design Patterns* (GoF, 1994)

---

## Level 2 — Tell, Don't Ask

### What we see now

Each coffee class **deducts resources from the machine itself** via getters and setters:

```java
coffeeMachine.setWaterMl(coffeeMachine.getWaterMl() - 250);
```

This means: coffee knows the machine's internal state (the `waterMl` field) and directly manipulates it. The machine is just a passive field container. Getters and setters on all 6 fields (`moneyDollars`, `waterMl`, `milkMl`, `coffeeG`, `cupsDisp`, `counterOfTenCoffee`) make the machine's state public *de facto* — anyone can read and change it however they want.

### Which OOP principle this violates

**Encapsulation** in the classic sense. Fields are private, but through setters they're just as accessible to the outside world as if they were public. This is called the **anemic model** — an object with no behavior, only data.

**Tell, Don't Ask** — don't ask an object about its state and don't change it from outside; tell the object what to do, and let it figure out its state itself.

### How to fix

Instead of getters and setters — **meaningful action methods**:

```java
machine.consumeResources(water, milk, beans);
machine.addRevenue(price);
machine.dispenseCup();
machine.recordCoffeeSold();
```

Or unified into one:

```java
machine.brewCoffee(water, milk, beans, price);
```

Then coffee just tells the machine how much of each to take, and the machine decides how to do it itself.

#### Before

```java
// Inside Coffee.makeCoffee():
machine.setWaterMl(machine.getWaterMl() - waterNeeded);
machine.setMilkMl(machine.getMilkMl() - milkNeeded);
machine.setCoffeeG(machine.getCoffeeG() - beansNeeded);
machine.setMoneyDollars(machine.getMoneyDollars() + price);
machine.setCupsDisp(machine.getCupsDisp() - 1);
machine.setCounterOfTenCoffee(machine.getCounterOfTenCoffee() + 1);
```

#### After

```java
// In CoffeeMachine.java a new method appears:
public void brew(int water, int milk, int beans, int price) {
    waterMl -= water;
    milkMl -= milk;
    coffeeG -= beans;
    moneyDollars += price;
    cupsDisp -= 1;
    counterOfTenCoffee += 1;
}

// In Coffee.makeCoffee() it becomes:
machine.brew(waterNeeded, milkNeeded, beansNeeded, price);
```

Most setters become unnecessary and are deleted. Only the getters that are truly needed externally remain (e.g. for `displayState()`), and there are far fewer of them.

### What changes in code after Level 2

- `CoffeeMachine` fields become truly private. No one outside can change them except via meaningful actions.
- If validation needs to be added later (e.g. "can't brew if machine is dirty") — it's done in one place, in `brew()`, not in every coffee class.
- The machine becomes an **acting entity**, not a **data structure**.

### Theoretical references

- **Tell, Don't Ask** — Alec Sharp, *Smalltalk by Example* (1997); popularized by Andy Hunt and Dave Thomas
- **Anemic Domain Model (anti-pattern)** — Martin Fowler, 2003

---

## Level 3 — separate method for resource checking

### What we see now

The "is there enough resources" check lives inside `makeCoffee` (or, after Level 1, inside the parent `Coffee.makeCoffee()`). Brewing logic and validation logic are mixed together.

### Which OOP principle this violates

**SRP** (Single Responsibility Principle) at the method level — the method does two things: validates and executes. In mature code these are often separated.

### How to fix

Extract validation into a separate machine method:

```java
public Optional<String> checkResources(int water, int milk, int beans) {
    if (waterMl < water) return Optional.of("Sorry, not enough water!");
    if (milkMl < milk) return Optional.of("Sorry, not enough milk!");
    if (coffeeG < beans) return Optional.of("Sorry, not enough coffee beans!");
    if (cupsDisp < 1) return Optional.of("Sorry, not enough disposable cups!");
    return Optional.empty();
}
```

`Optional<String>` is a standard Java type for a value that may or may not exist. If `Optional.empty()` — all good, can brew. If there's a string — that's the error message.

#### Before

```java
public void makeCoffee(CoffeeMachine machine) {
    if (machine.getWaterMl() < waterNeeded) {
        System.out.println("Sorry, not enough water!");
        return;
    }
    // ...3 more checks...
    machine.brew(waterNeeded, milkNeeded, beansNeeded, price);
    System.out.println("I have enough resources, making you a coffee!");
}
```

#### After

```java
public void makeCoffee(CoffeeMachine machine) {
    Optional<String> error = machine.checkResources(waterNeeded, milkNeeded, beansNeeded);
    if (error.isPresent()) {
        System.out.println(error.get());
        return;
    }
    machine.brew(waterNeeded, milkNeeded, beansNeeded, price);
    System.out.println("I have enough resources, making you a coffee!");
}
```

### Alternative — exceptions

A more mature approach is using exceptions (`InsufficientResourceException`). But `try/catch` is a separate Java learning topic, too early for now. So `Optional` is a good intermediate step.

### What changes in code after Level 3

- The `makeCoffee` method is shorter and reads linearly: "check — if error, say so; if not, brew".
- Resource checking can be used independently of brewing (e.g. for UI: "which recipes are currently available?").

### Theoretical references

- **Single Responsibility Principle** — Robert C. Martin, part of SOLID
- **Optional<T>** — standard part of Java since version 8

---

## Level 4 — composition of smaller objects

### What we see now

`CoffeeMachine` has 6 fields of different categories:
- brewing resources: `waterMl`, `milkMl`, `coffeeG`, `cupsDisp`
- monetary part: `moneyDollars`
- maintenance state: `counterOfTenCoffee`

These are three **different responsibilities** mixed in one class.

### Which OOP principle this violates

**SRP** at the class level — a class should do one thing. A cash register and a water tank are different things just physically placed in one body.

### How to fix

Composition — `CoffeeMachine` contains smaller meaningful objects inside:

```java
public class CoffeeMachine {
    private final ResourceTank tank = new ResourceTank(400, 540, 120, 9);
    private final CashRegister register = new CashRegister(550);
    private final MaintenanceCounter maintenance = new MaintenanceCounter(10);

    public void brew(int water, int milk, int beans, int price) {
        tank.consume(water, milk, beans);
        tank.dispenseCup();
        register.deposit(price);
        maintenance.recordOperation();
    }

    public boolean needsCleaning() {
        return maintenance.isAtLimit();
    }
}
```

Each of three internal objects (`ResourceTank`, `CashRegister`, `MaintenanceCounter`) is a small class with one responsibility.

### What changes in code after Level 4

- Code structure fully reflects the real coffee machine structure: resource tank, cash register, maintenance counter.
- Each component can be tested independently (important for unit tests).
- If a second tank needs to be added tomorrow (e.g. for hot milk) — add another `ResourceTank` without changes to other classes.

### Theoretical references

- **Composition over Inheritance** — *Design Patterns* (GoF, 1994)
- **Single Responsibility Principle** at the class level
- **Domain-Driven Design** — Eric Evans, 2003 (for mature understanding)

---

## Progress checklist

Replace `[ ]` with `[x]` after each completed item.

### Level 1 — eliminate duplication

- [ ] Add fields `waterNeeded`, `milkNeeded`, `beansNeeded`, `price` to abstract `Coffee` class
- [ ] Add constructor `protected Coffee(int water, int milk, int beans, int price)`
- [ ] Move `makeCoffee()` logic from three child classes into `Coffee.makeCoffee()`
- [ ] Simplify `Espresso`, `Latte`, `Cappuccino` to one constructor each
- [ ] Verify project compiles and runs
- [ ] Commit: `refactor: extract coffee recipe to base class (Template Method)`
- [ ] (Optional) Try the enum variant as a separate exercise

### Level 2 — Tell, Don't Ask

- [ ] Add method `brew(int water, int milk, int beans, int price)` to `CoffeeMachine`
- [ ] Replace direct setters in `Coffee.makeCoffee()` with `machine.brew(...)` call
- [ ] Delete setters that became unnecessary
- [ ] Check which getters are still used — keep only those
- [ ] Verify project compiles and runs
- [ ] Commit: `refactor: tell don't ask — replace setters with brew() action`

### Level 3 — separate resource-check method

- [ ] Add method `Optional<String> checkResources(...)` to `CoffeeMachine`
- [ ] Replace check block in `Coffee.makeCoffee()` with `checkResources()` call
- [ ] Verify project compiles and runs
- [ ] Commit: `refactor: extract resource check into separate method`

### Level 4 — composition of smaller objects

- [ ] Create `ResourceTank` class with water/milk/beans/cups fields
- [ ] Create `CashRegister` class with money field and `deposit()`, `withdraw()` methods
- [ ] Create `MaintenanceCounter` class with `recordOperation()`, `reset()`, `isAtLimit()` methods
- [ ] Replace fields in `CoffeeMachine` with these three objects
- [ ] Delegate `CoffeeMachine` methods to corresponding components
- [ ] Verify project compiles and runs
- [ ] Commit: `refactor: decompose CoffeeMachine into smaller objects`

---

## Overall recommendation

**Level 1 — mandatory.** Duplication is so obvious that leaving it in equals "stopping halfway". This is the most useful refactoring for understanding the real power of inheritance.

**Level 2 — highly desirable.** Tell, Don't Ask is one of the key principles of mature OOP. After it, you'll see the difference between "object-as-structure" and "object-as-acting-entity" in all subsequent code.

**Level 3 — useful for practice.** Not mandatory, but it nicely reinforces `Optional` and SRP at the method level.

**Level 4 — for mature experience.** May be excessive complexity for a learning project, but very valuable as a composition exercise. Save for later, when you encounter this topic in Hyperskill naturally.

**Don't do all levels at once.** Each level — separate commit. After each — verify project still works. If something breaks — revert to previous state and think about why.

---

*Document is updated as refactoring progresses. Ukrainian version: [OOP_REFACTORING_uk.md](OOP_REFACTORING_uk.md).*
