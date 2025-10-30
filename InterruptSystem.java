import java.io.*;
import java.time.*;
import java.util.*;

public class InterruptSystem {
    private static final int KEY_PRIORITY = 3;
    private static final int MOU_PRIORITY = 2;
    private static final int PRN_PRIORITY = 1;

    private static boolean keyFlag = false;
    private static boolean mouFlag = false;
    private static boolean prnFlag = false;

    private static boolean blockKey = false;
    private static boolean blockMou = false;
    private static boolean blockPrn = false;

    private static final Object monitor = new Object();

    private static void writeLog(String src) {
        try (FileWriter file = new FileWriter("interrupt_log.txt", true)) {
            LocalTime t = LocalTime.now();
            file.write(String.format("[%02d:%02d:%02d] %s Interrupt handled successfully\n",
                    t.getHour(), t.getMinute(), t.getSecond(), src));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleKeyISR() {
        if (blockKey) {
            System.out.println("Keyboard Interrupt Ignored (Masked)");
            return;
        }
        System.out.print("Keyboard Interrupt Triggered → Handling ISR");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        System.out.println(" → Completed");
        writeLog("Keyboard");
    }

    private static void handleMouseISR() {
        if (blockMou) {
            System.out.println("Mouse Interrupt Ignored (Masked)");
            return;
        }
        System.out.print("Mouse Interrupt Triggered → Handling ISR");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        System.out.println(" → Completed");
        writeLog("Mouse");
    }

    private static void handlePrinterISR() {
        if (blockPrn) {
            System.out.println("Printer Interrupt Ignored (Masked)");
            return;
        }
        System.out.print("Printer Interrupt Triggered → Handling ISR");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        System.out.println(" → Completed");
        writeLog("Printer");
    }

    private static class DeviceProcess extends Thread {
        private final String source;

        DeviceProcess(String source) {
            this.source = source;
        }

        @Override
        public void run() {
            Random rnd = new Random();
            while (true) {
                try { Thread.sleep((rnd.nextInt(5) + 1) * 1000L); } catch (InterruptedException ignored) {}
                synchronized (monitor) {
                    switch (source) {
                        case "Keyboard" -> keyFlag = true;
                        case "Mouse" -> mouFlag = true;
                        case "Printer" -> prnFlag = true;
                    }
                }
            }
        }
    }

    private static void checkInterrupts() {
        synchronized (monitor) {
            if (keyFlag) {
                keyFlag = false;
                handleKeyISR();
                return;
            }
            if (mouFlag) {
                mouFlag = false;
                handleMouseISR();
                return;
            }
            if (prnFlag) {
                prnFlag = false;
                handlePrinterISR();
                return;
            }
        }
        System.out.println("No pending interrupts.");
    }

    private static void displayMaskInfo() {
        System.out.println("\n--- Current Mask Status ---");
        System.out.println("Keyboard: " + (blockKey ? "Masked (Disabled)" : "Enabled"));
        System.out.println("Mouse: " + (blockMou ? "Masked (Disabled)" : "Enabled"));
        System.out.println("Printer: " + (blockPrn ? "Masked (Disabled)" : "Enabled"));
        System.out.println("---------------------------");
    }

    public static void main(String[] args) {
        new DeviceProcess("Keyboard").start();
        new DeviceProcess("Mouse").start();
        new DeviceProcess("Printer").start();

        Scanner input = new Scanner(System.in);
        System.out.println("==== Multithreaded Interrupt System Simulation (Java) ====");

        while (true) {
            System.out.println("\n1. Poll for Interrupts");
            System.out.println("2. Toggle Mask (Enable/Disable Device)");
            System.out.println("3. View Mask Status");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int option = input.nextInt();

            switch (option) {
                case 1 -> checkInterrupts();
                case 2 -> {
                    System.out.println("Select device to toggle mask:");
                    System.out.println("1. Keyboard\n2. Mouse\n3. Printer");
                    int dev = input.nextInt();
                    switch (dev) {
                        case 1 -> blockKey = !blockKey;
                        case 2 -> blockMou = !blockMou;
                        case 3 -> blockPrn = !blockPrn;
                        default -> System.out.println("Invalid device!");
                    }
                    displayMaskInfo();
                }
                case 3 -> displayMaskInfo();
                case 4 -> {
                    System.out.println("Exiting simulation... Goodbye!");
                    input.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
