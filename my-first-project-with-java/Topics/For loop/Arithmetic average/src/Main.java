import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        double sum = 0;
        int count = 0;
        double arithmeticAverage;
        for (int i = a; i <= b; i++) {
            if (i % 3 == 0) {
                sum += i;
                count++; // start coding here
            }
        }
        arithmeticAverage = sum / count;
        System.out.print(arithmeticAverage);

        scanner.close();
    }
}