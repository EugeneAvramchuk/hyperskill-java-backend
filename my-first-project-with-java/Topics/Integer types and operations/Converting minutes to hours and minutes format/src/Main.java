import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int minutes = scanner.nextInt();

        int resultHours = minutes / 60;
        int resultMinutes = minutes % 60;
        System.out.println(resultHours + " hours and " + resultMinutes + " minutes");// Your code comes here!

        scanner.close();
    }
}