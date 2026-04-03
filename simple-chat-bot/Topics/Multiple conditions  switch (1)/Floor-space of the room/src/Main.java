import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String shape = scanner.nextLine();

        switch (shape) {
            case "rectangle":
                double a = scanner.nextDouble();
                double b = scanner.nextDouble();
                System.out.println(a * b);
                break;
            case "circle":
                double r = scanner.nextDouble();
                System.out.println(3.14 * r * r);
                break;
            case "triangle":
                double aT = scanner.nextDouble();
                double bT = scanner.nextDouble();
                double cT = scanner.nextDouble();
                double p = (aT + bT + cT) / 2.0;
                double area = Math.sqrt(p * (p - aT) * (p - bT) * (p - cT));
                System.out.println(area);
                break;
        }
    }
}