import java.util.Scanner;

public class Main {
    // implement the method below
    public static void ... {
        // your code goes here ...
    }

    // please do not change the code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        try {
            int number = Integer.parseInt(input);
            checkNumber(number);
        } catch (NumberFormatException e) {
            checkNumber(input);
        }

        scanner.close();
    }
}