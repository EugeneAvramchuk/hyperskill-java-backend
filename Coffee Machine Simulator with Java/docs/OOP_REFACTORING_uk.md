# OOP Refactoring — Coffee Machine Simulator

**Мова:** українська | [English version](OOP_REFACTORING_en.md)

**Автор рев'ю:** Claude (Sonnet 4.5)
**Дата рев'ю:** 28 квітня 2026
**Стан проекту на момент рев'ю:** після першого етапу ООП-рефакторингу (виділення типів кави в окремі класи зі спадкоємством від абстрактного `Coffee`)

---

## Зміст

1. [Що зроблено добре](#1-що-зроблено-добре)
2. [Чотири рівні подальших покращень](#2-чотири-рівні-подальших-покращень)
3. [Рівень 1 — усунення дублювання в класах кави](#рівень-1--усунення-дублювання-в-класах-кави)
4. [Рівень 2 — Tell, Don't Ask](#рівень-2--tell-dont-ask)
5. [Рівень 3 — окремий метод перевірки ресурсів](#рівень-3--окремий-метод-перевірки-ресурсів)
6. [Рівень 4 — композиція менших об'єктів](#рівень-4--композиція-менших-обєктів)
7. [Чек-лист прогресу](#чек-лист-прогресу)
8. [Загальна рекомендація](#загальна-рекомендація)

---

## 1. Що зроблено добре

Перш ніж говорити про покращення — фіксую те, що вже на правильному шляху. Це важливо, бо рефакторинг ніколи не починається з нуля; він спирається на вже зроблене.

| Що | Чому це добре |
|---|---|
| Типи кави винесені в окремі класи (`Espresso`, `Latte`, `Cappuccino`) зі спадкоємством від абстрактного `Coffee` | Принциповий ООП-крок. Замість строкового параметру — повноцінні об'єкти, що знають свій рецепт. Інкапсуляція знань на правильному рівні. |
| Підпакет `coffee` всередині `machine` | Логічне групування коду. Файли організовані за змістом. |
| Класи-нащадки помічені як `final` | "Цей клас не призначений для подальшого успадкування" — розумне рішення для конкретних реалізацій. |
| `Main` мінімалістичний — лише створює машину і запускає | Правильний принцип: точка входу не має робити нічого, крім запуску. |
| Стан "потребую чищення" (`counterOfTenCoffee == 10`) перевіряється в `run()`, не всередині кави | Правильний розподіл відповідальності: кава не вирішує, чи її готувати — машина вирішує, чи готова варити. |

---

## 2. Чотири рівні подальших покращень

Рівні розташовані за зменшенням значущості: Рівень 1 — найкорисніший, Рівень 4 — найабстрактніший.

| Рівень | Назва | Зусилля | Корисність для навчання |
|---|---|---|---|
| 1 | Усунення дублювання | ~30 хв | **Дуже висока** |
| 2 | Tell, Don't Ask | ~1 год | **Висока** |
| 3 | Окремий метод перевірки ресурсів | ~30 хв | Середня |
| 4 | Композиція менших об'єктів | ~2 год | Висока (але вже зрілий ООП) |

---

## Рівень 1 — усунення дублювання в класах кави

### Що бачимо зараз

Три класи (`Espresso`, `Latte`, `Cappuccino`) майже ідентичні. Різняться тільки числами (об'єми ресурсів, ціна) і одною деталлю (Espresso не має молока).

Структура, логіка перевірок, повідомлення про брак ресурсів, послідовність списання — однакові у всіх трьох. Це **класичне дублювання коду**.

### Який принцип ООП це порушує

**DRY** (Don't Repeat Yourself) — "не повторюйся". Якщо однакова логіка живе в трьох місцях, це означає, що зміна в логіці (наприклад, нове повідомлення про помилку, нова перевірка) має бути зроблена тричі. Тричі — це тричі шанс помилитись.

### Як виправити

Винести спільну логіку в **батьківський клас** `Coffee`. Конкретні класи передають свої числа через конструктор. Це називається **шаблонний метод (Template Method)** — у батька є загальний алгоритм, у дітей — параметри.

#### Було (Espresso.java)

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

Те саме повторюється в `Latte.java` і `Cappuccino.java`.

#### Стало (Coffee.java)

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

#### Стало (Espresso.java, Latte.java, Cappuccino.java)

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

Кожен клас зводиться до одного рядка — конструктора, що передає свої параметри батьку.

### Альтернатива — використати enum

Оскільки типи кави — це **фіксований обмежений набір**, можна повністю замінити три класи одним `enum`:

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
        // та сама логіка, що у Coffee.makeCoffee()
    }
}
```

**Який підхід обрати:** для навчальних цілей — спочатку зробити Template Method (абстрактний клас + спадкоємці), щоб закріпити концепцію спадкоємства. Потім, як окрему вправу, спробувати enum-варіант. Це дві важливі ООП-форми, обидві варто опанувати.

### Що зміниться в коді після Рівня 1

- Кількість рядків у трьох класах кави зменшиться з ~25 до 1 на клас.
- Будь-яка зміна логіки приготування буде в одному місці.
- Стане очевидно, що типи кави — це **дані** (числа в конструкторі), а не **код** (повторювана логіка).

### Теоретичні посилання

- **DRY** — Don't Repeat Yourself, з книги *The Pragmatic Programmer* (Hunt & Thomas, 1999)
- **Template Method Pattern** — з книги *Design Patterns* (GoF, 1994)

---

## Рівень 2 — Tell, Don't Ask

### Що бачимо зараз

Кожен клас кави **сам віднімає ресурси з машини** через геттери і сеттери:

```java
coffeeMachine.setWaterMl(coffeeMachine.getWaterMl() - 250);
```

Це означає: кава знає внутрішній стан машини (поле `waterMl`) і безпосередньо ним маніпулює. Машина — лише пасивне сховище полів. Геттери і сеттери на всі 6 полів (`moneyDollars`, `waterMl`, `milkMl`, `coffeeG`, `cupsDisp`, `counterOfTenCoffee`) роблять стан машини публічним *de facto* — будь-хто може його прочитати і змінити як завгодно.

### Який принцип ООП це порушує

**Інкапсуляція** в класичному розумінні. Поля приватні, але через сеттери вони так само доступні зовнішньому світу, як були б публічними. Це називається **анемічна модель** — об'єкт без поведінки, лише дані.

**Tell, Don't Ask** — "скажи, не питай". Не питай об'єкт про його стан і не змінюй його ззовні; скажи об'єкту, що зробити, і нехай він сам розбирається зі своїм станом.

### Як виправити

Замість геттерів і сеттерів — **осмислені методи дій**:

```java
machine.consumeResources(water, milk, beans);
machine.addRevenue(price);
machine.dispenseCup();
machine.recordCoffeeSold();
```

Або об'єднати в один:

```java
machine.brewCoffee(water, milk, beans, price);
```

Тоді кава просто каже машині, скільки чого взяти, а машина сама вирішує, як це зробити.

#### Було

```java
// Всередині Coffee.makeCoffee():
machine.setWaterMl(machine.getWaterMl() - waterNeeded);
machine.setMilkMl(machine.getMilkMl() - milkNeeded);
machine.setCoffeeG(machine.getCoffeeG() - beansNeeded);
machine.setMoneyDollars(machine.getMoneyDollars() + price);
machine.setCupsDisp(machine.getCupsDisp() - 1);
machine.setCounterOfTenCoffee(machine.getCounterOfTenCoffee() + 1);
```

#### Стало

```java
// У CoffeeMachine.java з'являється метод:
public void brew(int water, int milk, int beans, int price) {
    waterMl -= water;
    milkMl -= milk;
    coffeeG -= beans;
    moneyDollars += price;
    cupsDisp -= 1;
    counterOfTenCoffee += 1;
}

// У Coffee.makeCoffee() стає:
machine.brew(waterNeeded, milkNeeded, beansNeeded, price);
```

Більшість сеттерів стають непотрібні і видаляються. Геттери лишаються тільки ті, що справді потрібні зовні (наприклад, для `displayState()`), і їх стає набагато менше.

### Що зміниться в коді після Рівня 2

- Поля `CoffeeMachine` стають по-справжньому приватними. Ніхто ззовні не може їх змінити інакше, ніж через осмислені дії.
- Якщо колись треба буде додати валідацію (наприклад, "не можна приготувати каву, якщо машина брудна") — це робиться в одному місці, в методі `brew()`, а не в кожному класі кави.
- Машина стає **діючою сутністю**, а не **структурою даних**.

### Теоретичні посилання

- **Tell, Don't Ask** — Alec Sharp, *Smalltalk by Example* (1997); популяризовано Andy Hunt і Dave Thomas
- **Anemic Domain Model (anti-pattern)** — Martin Fowler, 2003

---

## Рівень 3 — окремий метод перевірки ресурсів

### Що бачимо зараз

Перевірка "чи вистачає ресурсів" живе всередині `makeCoffee` (або, після Рівня 1, всередині батьківського `Coffee.makeCoffee()`). Логіка приготування і логіка перевірки змішані.

### Який принцип ООП це порушує

**SRP** (Single Responsibility Principle) на рівні методу — метод робить дві речі: перевіряє і виконує. У зрілому коді це часто розділяють.

### Як виправити

Винести перевірку в окремий метод машини:

```java
public Optional<String> checkResources(int water, int milk, int beans) {
    if (waterMl < water) return Optional.of("Sorry, not enough water!");
    if (milkMl < milk) return Optional.of("Sorry, not enough milk!");
    if (coffeeG < beans) return Optional.of("Sorry, not enough coffee beans!");
    if (cupsDisp < 1) return Optional.of("Sorry, not enough disposable cups!");
    return Optional.empty();
}
```

`Optional<String>` — стандартний Java-тип для значення, яке може бути або не бути. Якщо `Optional.empty()` — все добре, можна варити. Якщо там рядок — це повідомлення про помилку.

#### Було

```java
public void makeCoffee(CoffeeMachine machine) {
    if (machine.getWaterMl() < waterNeeded) {
        System.out.println("Sorry, not enough water!");
        return;
    }
    // ...ще 3 перевірки...
    machine.brew(waterNeeded, milkNeeded, beansNeeded, price);
    System.out.println("I have enough resources, making you a coffee!");
}
```

#### Стало

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

### Альтернатива — exceptions

Зріліший підхід — використати виключення (`InsufficientResourceException`). Але `try/catch` — це окрема тема Java-навчання, поки що зарано. Тому `Optional` — гарний проміжний крок.

### Що зміниться в коді після Рівня 3

- Метод `makeCoffee` коротший і читається лінійно: "перевір — якщо помилка, скажи; якщо ні, варимо".
- Перевірку ресурсів можна використовувати незалежно від приготування (наприклад, для UI: "які з цих рецептів зараз доступні?").

### Теоретичні посилання

- **Single Responsibility Principle** — Robert C. Martin, частина SOLID
- **Optional<T>** — стандартна частина Java з версії 8

---

## Рівень 4 — композиція менших об'єктів

### Що бачимо зараз

`CoffeeMachine` має 6 полів різних категорій:
- ресурси для варіння: `waterMl`, `milkMl`, `coffeeG`, `cupsDisp`
- грошова частина: `moneyDollars`
- стан обслуговування: `counterOfTenCoffee`

Це три **різні відповідальності**, що змішані в одному класі.

### Який принцип ООП це порушує

**SRP** на рівні класу — клас має робити одну річ. Касовий апарат і резервуар з водою — це різні речі, які просто фізично поміщені в один корпус.

### Як виправити

Композиція — `CoffeeMachine` має всередині менші осмислені об'єкти:

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

Кожен з трьох внутрішніх об'єктів (`ResourceTank`, `CashRegister`, `MaintenanceCounter`) — невеликий клас з однією відповідальністю.

### Що зміниться в коді після Рівня 4

- Структура коду повністю відображає реальну структуру кавоварки: бак з ресурсами, касовий апарат, лічильник чищення.
- Кожен з цих компонентів можна тестувати незалежно (важливо для unit-тестів).
- Якщо завтра треба буде додати другий бак (наприклад, для гарячого молока) — додаємо ще один `ResourceTank`, без змін в інших класах.

### Теоретичні посилання

- **Composition over Inheritance** — *Design Patterns* (GoF, 1994)
- **Single Responsibility Principle** на рівні класу
- **Domain-Driven Design** — Eric Evans, 2003 (для зрілого розуміння)

---

## Чек-лист прогресу

Відмічай `[x]` замість `[ ]` після кожного зробленого пункту.

### Рівень 1 — усунення дублювання

- [ ] Додати поля `waterNeeded`, `milkNeeded`, `beansNeeded`, `price` в абстрактний клас `Coffee`
- [ ] Додати конструктор `protected Coffee(int water, int milk, int beans, int price)`
- [ ] Перенести логіку `makeCoffee()` з трьох класів-нащадків у `Coffee.makeCoffee()`
- [ ] Спростити `Espresso`, `Latte`, `Cappuccino` до одного конструктора кожен
- [ ] Перевірити, що проект компілюється і запускається
- [ ] Закомітити: `refactor: extract coffee recipe to base class (Template Method)`
- [ ] (Опційно) Спробувати enum-варіант як окрему вправу

### Рівень 2 — Tell, Don't Ask

- [ ] Додати метод `brew(int water, int milk, int beans, int price)` в `CoffeeMachine`
- [ ] Замінити прямі сеттери в `Coffee.makeCoffee()` на виклик `machine.brew(...)`
- [ ] Видалити сеттери, які стали непотрібні
- [ ] Перевірити, які геттери ще використовуються — лишити тільки ті
- [ ] Перевірити, що проект компілюється і запускається
- [ ] Закомітити: `refactor: tell don't ask — replace setters with brew() action`

### Рівень 3 — окремий метод перевірки ресурсів

- [ ] Додати метод `Optional<String> checkResources(...)` в `CoffeeMachine`
- [ ] Замінити блок перевірок у `Coffee.makeCoffee()` на виклик `checkResources()`
- [ ] Перевірити, що проект компілюється і запускається
- [ ] Закомітити: `refactor: extract resource check into separate method`

### Рівень 4 — композиція менших об'єктів

- [ ] Створити клас `ResourceTank` з полями води, молока, зерен, чашок
- [ ] Створити клас `CashRegister` з полем грошей і методами `deposit()`, `withdraw()`
- [ ] Створити клас `MaintenanceCounter` з методами `recordOperation()`, `reset()`, `isAtLimit()`
- [ ] Замінити поля в `CoffeeMachine` на ці три об'єкти
- [ ] Делегувати методи `CoffeeMachine` до відповідних компонентів
- [ ] Перевірити, що проект компілюється і запускається
- [ ] Закомітити: `refactor: decompose CoffeeMachine into smaller objects`

---

## Загальна рекомендація

**Рівень 1 — обов'язковий.** Дублювання настільки очевидне, що його залишення дорівнює "зупинитись на півдорозі". Це найкорисніший рефакторинг для розуміння справжньої сили спадкоємства.

**Рівень 2 — дуже бажаний.** Tell, Don't Ask — один з ключових принципів зрілого ООП. Після нього ти будеш бачити різницю між "об'єктом-структурою" і "об'єктом-діючою сутністю" в усьому подальшому коді.

**Рівень 3 — корисний для тренування.** Не обов'язковий, але добре закріплює `Optional` і SRP на рівні методу.

**Рівень 4 — для зрілого досвіду.** Може бути зайвим ускладненням для навчального проекту, але дуже цінний як вправа з композиції. Залиш на потім, коли в Hyperskill зустрінеш цю тему природно.

**Не роби всі рівні одразу.** Кожен рівень — окремий коміт. Після кожного — перевір, що проект ще працює. Якщо щось зламалось — повертайся на попередній стан і думай чому.

---

*Документ оновлюється в міру прогресу рефакторингу. Версія англійською: [OOP_REFACTORING_en.md](OOP_REFACTORING_en.md).*
