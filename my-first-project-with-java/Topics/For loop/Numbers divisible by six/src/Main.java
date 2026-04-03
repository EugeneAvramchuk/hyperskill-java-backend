import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numberOfElements = scanner.nextInt();
        int element, sum = 0;

        for (int i = 0; i < numberOfElements; i++) {
            element = scanner.nextInt();
            if (element % 6 == 0) {
                sum += element;// start coding here
            }
        }
        System.out.println(sum);

        scanner.close();
    }
}