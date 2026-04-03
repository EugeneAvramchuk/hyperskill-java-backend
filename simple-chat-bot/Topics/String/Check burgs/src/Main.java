import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();
        System.out.println(name.endsWith("burg"));

        scanner.close(); // start coding here
    }
}