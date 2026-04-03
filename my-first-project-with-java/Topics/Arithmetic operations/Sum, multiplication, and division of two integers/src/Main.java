import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create a Scanner object to read input
        Scanner scan = new Scanner(System.in);

        // Read the first integer
        int num1 = scan.nextInt();

        // Read the second integer
        int num2 = scan.nextInt();

        System.out.println(num1 + num2);
        System.out.println(num1 * num2);
        System.out.println(num1 / num2);

        scan.close();// TODO: Perform addition, multiplication, and division operations
    }
}