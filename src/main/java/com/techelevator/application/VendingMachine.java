package com.techelevator.application;

import com.techelevator.ui.UserInput;
import com.techelevator.ui.UserOutput;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class VendingMachine {
    //Instance variables
    private BigDecimal balance = new BigDecimal("0.00");
    //private Map<String, ItemSlot> inventory = new HashMap<>();

    //Getters
    public BigDecimal getBalance() {
        return balance;
    }

    Inventory inventory = new Inventory();
    AuditWriter auditWriter = new AuditWriter();
    private BigDecimal totalSales = new BigDecimal("0.00");

    //Setters
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    //Program flow methods

    public void startUp() {
        inventory.setInventory();
        run();
    }

    public void run() {
        while (true) {
            UserOutput.displayHomeScreen();
            String choice = UserInput.getHomeScreenOption();
            System.out.println(choice);
            if (choice.equals("")) {
                UserOutput.invalidInput();
            } else if (choice.equals("display")) {
                inventory.displayInventory();
            } else if (choice.equals("purchase")) {
                runPurchaseMenu();
            } else if (choice.equals("exit")) {
                UserOutput.thankYou();
                System.exit(0);
            } else if (choice.equals("secret")) {
                salesReport();
                UserOutput.salesReport();
            }
        }
    }

    public void runPurchaseMenu() {
        while (true) {
            UserOutput.displayPurchaseScreen();
            String purchaseChoice = UserInput.getPurchaseScreenOption(balance);
            System.out.println(purchaseChoice);
            if (purchaseChoice.equals("")) {
                UserOutput.invalidInput();
            } else if (purchaseChoice.equals("feed")) {
                String feed = UserInput.promptFeedMachine();
                if (feed.equals("0")) {
                    continue;
                }
                feedMachine(feed);
            } else if (purchaseChoice.equals("select")) {
                // open selectItemSlot method
                inventory.displayInventory();
                UserOutput.divider();
                String itemSlot = UserInput.selectItemSlot();
                dispenseItem(itemSlot);

            } else if (purchaseChoice.equals("finish")) {
                returnChange();
                run();
                //break;
            }
        }
    }

    //Balance management methods
    public int[] returnChange() {
        int[] coinsReturned = new int[4];
        BigDecimal dollar = new BigDecimal("1.00");
        BigDecimal quarter = new BigDecimal("0.25");
        BigDecimal dime = new BigDecimal("0.10");
        BigDecimal nickel = new BigDecimal("0.05");
        auditWriter.write("CHANGE GIVEN:", balance, new BigDecimal("0.00"));
        while (balance.compareTo(dollar) >= 0) {
            balance = balance.subtract(dollar);
            coinsReturned[0]++;
        }
        while (balance.compareTo(quarter) >= 0) {
            balance = balance.subtract(quarter);
            coinsReturned[1]++;
        }
        while (balance.compareTo(dime) >= 0) {
            balance = balance.subtract(dime);
            coinsReturned[2]++;
        }
        while (balance.compareTo(nickel) >= 0) {
            balance = balance.subtract(nickel);
            coinsReturned[3]++;
        }
        UserOutput.change(coinsReturned[0], coinsReturned[1], coinsReturned[2], coinsReturned[3]);
        return coinsReturned;
    }

    public void feedMachine(String userInput) {
        switch (userInput) {
            case "1":
                balance = balance.add(new BigDecimal("1.00"));
                break;
            case "5":
                balance = balance.add(new BigDecimal("5.00"));
                break;
            case "10":
                balance = balance.add(new BigDecimal("10.00"));
                break;
            case "20":
                balance = balance.add(new BigDecimal("20.00"));
                break;
            default:
                break;
        }
        auditWriter.write("MONEY FED:", balance.subtract(new BigDecimal(userInput)), balance);
    }

    public void dispenseItem(String itemSlot) {
        ItemSlot slot = inventory.getInventory().get(itemSlot);
        if (!inventory.getInventory().containsKey(itemSlot)) {
            UserOutput.itemSlotDNE();
        } else {
            BigDecimal price = slot.getPrice();
            if (slot.getQuantity() < 1) {
                UserOutput.itemOutOfStock();
            } else if (balance.compareTo(price) == -1) {
                UserOutput.insufficientFunds();
            } else {
                slot.decrementQuantity();
                UserOutput.printPurchase(slot);
                auditWriter.write(slot.getItemName() + " " + slot.getSlot(), balance, balance.subtract(price));
                balance = balance.subtract(price);
                totalSales = totalSales.add(price);
                UserOutput.showBalance(balance);
            }
        }
    }

    public void salesReport() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String formatDateTime = LocalDateTime.now().format(format);
        String fileName = "SalesReports/sales_report_" + formatDateTime + ".txt";
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            PrintWriter writer = new PrintWriter(fileOutputStream);
            writer.println("Taste Elevator Sales Report");
            for (Map.Entry<String, ItemSlot> element : inventory.getInventory().entrySet()) {
                ItemSlot slot = element.getValue();
                writer.println(slot.getItemName() + "|" + (6 - slot.getQuantity()));
            }
            writer.println("TOTAL SALES $" + totalSales.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            UserOutput.fileNotFound();
        }
    }

}

