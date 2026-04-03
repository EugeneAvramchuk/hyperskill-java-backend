import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int sumD = 0, sumC = 0, sumB = 0, sumA = 0;
        for (int i = 0; i <= n; i++) {
            String number = scanner.nextLine();
            switch (number) {
                case "D" -> ++sumD;
                case "C" -> ++sumC;
                case "B" -> ++sumB;
                case "A" -> ++sumA;// start coding here
            }
        }
        System.out.println(sumD + " " + sumC + " " + sumB + " " + sumA);

        scanner.close();
    }
}