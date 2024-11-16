import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CinemaManagement {
    // defining the global variables
    private final static Scanner inputScanner = new Scanner(System.in);
    private static final String[][] seatAllocation = new String[3][16];
    private static final Ticket[] ticketsPurchased = new Ticket[48];
    private static int ticketAllocationCounter = 0; // to keep track of the number of tickets added

    public static void main(String[] args) {
        // initialize the seats when starting the application
        initializeAllSeats();
        showApplicationMenu();
    }

    private static void showApplicationMenu() {
        System.out.println("Welcome to The London Lumiere");
        System.out.println("-----------------------------------------------------");
        System.out.println(" Please select an option:");
        boolean isContinue = true; // to keep track, user decide to quit or not
        while (isContinue) {
            System.out.println();
            System.out.println("1) Buy a Ticket");
            System.out.println("2) Cancel a Booked Ticket");
            System.out.println("3) See Seating Plan");
            System.out.println("4) Find First Seat Available ");
            System.out.println("5) Print Ticket Information and Total Prices ");
            System.out.println("6) Search Ticket ");
            System.out.println("7) Sort Tickets by Prices ");
            System.out.println("8) Exit");
            System.out.println("--------------------------------------------------");
            System.out.println();
            System.out.println("Select an Option: ");
            int choice = 0;
            try {
                choice = inputScanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid option ");
                isContinue = false;
            }
            switch (choice) {
                case 1:
                    buySeat();
                    break;
                case 2:
                    cancelSeat();
                    break;
                case 3:
                    displayTheatreSeats();
                    break;
                case 4:
                    findFirstSeatAvailable();
                    break;
                case 5:
                    printTicketInformation();
                    break;
                case 6:
                    searchBookedTicket();
                    break;
                case 7:
                    sortTickets(ticketsPurchased);
                    break;
                case 8:
                    isContinue = false;
                    System.out.println("Thank you!!!");
                    break;
                default:
                    System.out.println("Invalid Option... Please Select a Valid Option.");
            }
        }
    }

    private static void initializeAllSeats() {
        for (int i = 0; i < seatAllocation.length; i++) {
            for (int j= 0; j < seatAllocation[i].length; j++) {
                seatAllocation[i][j] = "O";
            }
        }
    }

    private static void displayTheatreSeats() {
        System.out.println("*************************************");
        System.out.println("*              SCREEN               *");
        System.out.println("*************************************");
        System.out.println();

        for (int i = 0; i < seatAllocation.length; i++) {
            // Add left space for alignment
            System.out.print("  ");
            for (int j = 0; j < seatAllocation[i].length; j++) {
                System.out.print(seatAllocation[i][j] + " ");
                if (j == 7) {
                    // Add space after the 8th seat
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    private static void searchBookedTicket() {
        System.out.println("Enter the seat number you are searching for: ");
        int seat;
        int rowName;
        try {
            seat = inputScanner.nextInt();
            if (seat < 1 || seat > 16) {
                System.out.println("Invalid seat number. Select a seat number between 1-16.");
                return;
            }
            System.out.println("Enter the row number (1,2 or 3)");
            rowName = inputScanner.nextInt();
            if (rowName < 1 || rowName > 3) {
                System.out.println("Invalid row number. Select a row number between 1-3.");
                return;
            }
            boolean found = false;
            for (Ticket ticket : ticketsPurchased) {
                // checking for the given seat
                if (ticket != null && ticket.getRow() == rowName && ticket.getSeat() == seat) {
                    System.out.println(ticket);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("This seat is available");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
        }
    }

    public static void sortTickets(Ticket[] tickets) {
        int n = tickets.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (tickets[j] != null && tickets[j + 1] != null && tickets[j].getPrice() > 0 && tickets[j + 1].getPrice() > 0) {
                    if (tickets[j].getPrice() > tickets[j + 1].getPrice()) {
                        Ticket temp = tickets[j];
                        tickets[j] = tickets[j + 1];
                        tickets[j + 1] = temp;
                    }
                }
            }
        }

        boolean isArrayNotEmpty = false;

        for (Ticket ticket : tickets) {
            if (ticket != null && ticket.getPrice() > 0) {
                isArrayNotEmpty = true;
                System.out.println(ticket);
            }
        }

        if (!isArrayNotEmpty) {
            System.out.println("No tickets available to sort.");
        }
    }

    private static void printTicketInformation() {
        double total = 0.0;
        for (int i = 0; i < ticketsPurchased.length; i++) {
            // check if the ticket is not empty and the price is greater than 0
            if (ticketsPurchased[i] != null && ticketsPurchased[i].getPrice() > 0) {
                System.out.println(ticketsPurchased[i]);
                // adding all ticket costs
                total += ticketsPurchased[i].getPrice();
            }
        }
        System.out.println();
        System.out.println("Total sales: £" + total);
    }

    private static void findFirstSeatAvailable() {
        for (int i = 0; i < seatAllocation.length; i++) {
            for (int j = 0; j < seatAllocation[i].length; j++) {
                if (seatAllocation[i][j] == null || seatAllocation[i][j].equals("O")) {
                    int row = i + 1;
                    int seatNumber = j + 1;
                    System.out.println("First available seat is in row " + row + ", seat " + seatNumber);
                    return;
                }
            }
        }
        System.out.println("No available seats found.");
    }

    private static void cancelSeat() {
        System.out.println("Enter row name (1, 2, 3):");

        try {
            int rowName = inputScanner.nextInt();

            if (rowName >= 1 && rowName <= 3) {
                System.out.println("Enter the seat number:");
                int seatNumber = inputScanner.nextInt();

                if (seatNumber < 1 || seatNumber > 16) {
                    System.out.println("Invalid seat number. Select a seat number between 1-16.");
                } else {
                    if (isSeatNotAvailable(rowName, seatNumber)) {
                        // Make seat available
                        seatAllocation[rowName - 1][seatNumber - 1] = "O";

                        // Remove ticket from the ticket list
                        for (int i = 0; i < ticketsPurchased.length; i++) {
                            if (ticketsPurchased[i] != null && ticketsPurchased[i].getRow() == rowName && ticketsPurchased[i].getSeat() == seatNumber) {
                                ticketsPurchased[i] = null;
                                break;
                            }
                        }
                        System.out.println("Seat cancelled successfully.");
                    } else {
                        System.out.println("Seat already available.");
                    }
                }
            } else {
                System.out.println("Row name should be either 1, 2, or 3.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter a valid row number and seat number.");
        }
    }

    private static void buySeat() {
        System.out.println("Enter row name (1, 2, or 3):");
        try {
            int rowName = inputScanner.nextInt();
            if (rowName < 1 || rowName > 3) {
                System.out.println("Row number should be either 1, 2, or 3.");
                return;
            }

            System.out.println("Enter the seat number:");
            int seatNumber = inputScanner.nextInt();
            if (seatNumber < 1 || seatNumber > 16) {
                System.out.println("Invalid seat number. Select a seat number between 1-16.");
                return;
            }

            if (isSeatNotAvailable(rowName, seatNumber)) {
                System.out.println("Seat already booked.");
                return;
            }

            System.out.println("Enter your first name:");
            String firstName = inputScanner.next();
            System.out.println("Enter your surname:");
            String surname = inputScanner.next();
            System.out.println("Enter your email:");
            String email = inputScanner.next();

            // Create person and ticket objects
            Person person = new Person(firstName, surname, email);
            Ticket ticket = new Ticket();
            ticket.setSeat(seatNumber);
            ticket.setRow(rowName);
            ticket.setPerson(person);
            ticket.setPrice(getTicketCost(rowName));

            // Check if tickets are available for purchase
            if (ticketAllocationCounter < ticketsPurchased.length) {
                ticketsPurchased[ticketAllocationCounter++] = ticket;
                seatAllocation[rowName - 1][seatNumber - 1] = "X";  // Mark seat as booked
                System.out.println("Ticket purchased successfully.");
                storeBookingDetails(rowName, seatNumber, ticket.getPrice(), person);
                System.out.println(ticket);
            } else {
                System.out.println("Sorry, no more seats available.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter a valid row number and seat number.");
            inputScanner.next();
        }
    }

    public static void storeBookingDetails(int row, int bookedSeatNumber, double ticketCost, Person ownerDetails) {
        String fileName = row + "" + bookedSeatNumber + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Ticket Information:");
            writer.println("Row: " + row);
            writer.println("Seat: " + bookedSeatNumber);
            writer.println("Price: £" + ticketCost);
            writer.println("Ticket Owner Information:");
            writer.println(ownerDetails.toString());
            System.out.println("Ticket information saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error happened while saving information.");
        }
    }

    private static boolean isSeatNotAvailable(int row, int seatNumber) {
        // return true if the seat in the row is not available only
        if (seatAllocation[row - 1][seatNumber - 1].equals("O")) {
            return false;
        } else {
            return true;
        }
    }

    private static double getTicketCost(int rowName) {
        // return ticket cost according to the row number
        if (rowName == 1) {
            return 12.00;
        } else if (rowName == 2) {
            return 10.00;
        } else {
            return 8.00;
        }
    }
}