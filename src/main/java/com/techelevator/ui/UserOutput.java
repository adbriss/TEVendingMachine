package com.techelevator.ui;

public class UserOutput {
    public static void displayMessage(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    public static void displayHomeScreen() {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("                      Home");
        System.out.println("***************************************************");
        System.out.println();
    }
    public static void displayPurchaseScreen(){
        System.out.println();
        System.out.println("***************************************************");
        System.out.println("                      Purchase Menu");
        System.out.println("***************************************************");
        System.out.println();
    }

    public static void invalidInput() {
        System.out.println("Please enter a valid input");
    }

    public static void thankYou() {
        System.out.println("Thank you for shopping!");
    }

    public static void divider() {
        System.out.println();
        System.out.println("***************************************************");
        System.out.println();
    }

    public static void change(int dollars, int quarters, int dimes, int nickels) {
        System.out.println("Clink!");
        System.out.println("Change dispensed: " + dollars + " dollar(s), " + quarters + " quarter(s), " + dimes + " dime(s), and " + nickels + " nickel(s)");
    }

    public static void itemSlotDNE() {
        System.out.println();
        System.out.println("Item slot does not exist");
        System.out.println();
    }

    public static void itemOutOfStock() {
        System.out.println();
        System.out.println("Item is out of stock");
        System.out.println();
    }

    public static void insufficientFunds() {
        System.out.println();
        System.out.println("Insufficient funds");
        System.out.println();
    }
}
