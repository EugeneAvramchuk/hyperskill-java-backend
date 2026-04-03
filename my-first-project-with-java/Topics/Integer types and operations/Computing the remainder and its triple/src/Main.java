import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        int remainder = input % 2;
        System.out.println(remainder);// The first operation goes here
        int triple = remainder * 3;
        System.out.println(triple);// The second operation goes here

        scanner.close();
    }
}
