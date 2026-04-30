package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {

        int numberOfSeatsTotal = 0;
        int numberOfPurchasedTickets = 0;
        double percentage = 0;
        int currentIncome = 0;
        int totalIncome = 0;


        Scanner scanner = new Scanner(System.in);

        //Cinema room
        System.out.println("Enter the number of rows:");
        int numberOfRows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        int numberOfSeats = scanner.nextInt();

        String[][] cinemaRoom = new String[numberOfRows + 1][numberOfSeats + 1];
        cinemaRoom[0][0] = " ";
        for (int j = 1; j < cinemaRoom[0].length; j++) {
            cinemaRoom[0][j] = String.valueOf(j);
        }

        for (int i = 1; i < cinemaRoom.length; i++) {
            cinemaRoom[i][0] = String.valueOf(i);
        }

        for (int i = 1; i < cinemaRoom.length; i++) {
            for (int j = 1; j < cinemaRoom[i].length; j++) {
                cinemaRoom[i][j] = "S";
            }
        }
        //End of cinema room

        // Calculation of numberOfSeatsTotal && totalincome
        numberOfSeatsTotal = numberOfSeats * numberOfRows;

        if (numberOfSeatsTotal <= 60) {
            totalIncome = 10 * numberOfSeatsTotal;
        } else {
            totalIncome = (numberOfRows / 2) * numberOfSeats * 10 + (numberOfRows - numberOfRows / 2) * numberOfSeats * 8;
        }
        // End of calculation of numberOfSeatsTotal && totalincome

        //Main menu
        while (true) {
            System.out.println("""
                    
                    1. Show the seats
                    2. Buy a ticket
                    3. Statistics
                    0. Exit""");
            int action = scanner.nextInt();
            if (action == 0) {
                break;
            }

            switch (action) {
                case 1 -> { // Show the seats
                    System.out.println("\nCinema:");

                    for (int i = 0; i < cinemaRoom.length; i++) {
                        for (int j = 0; j < cinemaRoom[i].length; j++) {
                            System.out.print(cinemaRoom[i][j] + " ");
                        }
                        System.out.println();
                    }
                } //End of show the seats

                case 2 -> { //Buy a ticket
                    while (true) {
                        System.out.println("\nEnter a row number:");
                        int rowNumber = scanner.nextInt();
                        System.out.println("Enter a seat number in that row:");
                        int seatNumber = scanner.nextInt();

                        if (rowNumber < 1 || rowNumber > numberOfRows || seatNumber < 1 || seatNumber > numberOfSeats) {
                            System.out.println("Wrong input!");
                            continue;
                        }

                        if (cinemaRoom[rowNumber][seatNumber].equals("B")) {
                            System.out.println("That ticket has already been purchased!");
                            continue;
                        }

                        int ticketPrice = 0;

                        if (numberOfSeatsTotal <= 60 || rowNumber <= (numberOfRows / 2)) {
                            ticketPrice = 10;
                        } else {
                            ticketPrice = 8;
                        }
                        cinemaRoom[rowNumber][seatNumber] = "B";

                        // Calculation of the statistics
                        numberOfPurchasedTickets += 1;
                        percentage = 100.00 * numberOfPurchasedTickets / numberOfSeatsTotal;
                        currentIncome += ticketPrice;
                        // End of calculation of the statistics

                        System.out.println("\nTicket price: $" + ticketPrice);

                        break;
                    }
                } //End of buy a ticket

                case 3 -> { // Statistics
                    System.out.println("""
                            \nNumber of purchased tickets: %d
                            Percentage: %.2f%%
                            Current income: $%d
                            Total income: $%d""".formatted(numberOfPurchasedTickets, percentage, currentIncome, totalIncome));
                } // End of statistics

            }
        } //End of main menu

        scanner.close();
    }
}