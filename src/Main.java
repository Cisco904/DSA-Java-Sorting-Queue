import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    static Scanner input = new Scanner(System.in);

    static ArrayList<Integer> orderIds = new ArrayList<>();
    static ArrayList<String> customerNames = new ArrayList<>();
    static ArrayList<String> itemNames = new ArrayList<>();
    static ArrayList<Integer> quantities = new ArrayList<>();
    static ArrayList<Double> totalAmounts = new ArrayList<>();
    static ArrayList<String> statuses = new ArrayList<>();

    static Queue<Integer> prepQueue = new LinkedList<>();

    static int nextOrderId = 1001;

    static final String WAITING = "Waiting";
    static final String IN_QUEUE = "In Queue";
    static final String COMPLETED = "Completed";

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ", 0, 15);

            switch (choice) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    displayAllOrders();
                    break;
                case 3:
                    searchOrders();
                    break;
                case 4:
                    updateOrder();
                    break;
                case 5:
                    deleteOrder();
                    break;
                case 6:
                    enqueueOrder();
                    break;
                case 7:
                    enqueueAllWaiting();
                    break;
                case 8:
                    dequeueOrder();
                    break;
                case 9:
                    peekQueue();
                    break;
                case 10:
                    displayQueue();
                    break;
                case 11:
                    checkQueueEmpty();
                    break;
                case 12:
                    runBubbleSort();
                    break;
                case 13:
                    runSelectionSort();
                    break;
                case 14:
                    showPendingAndCompleted();
                    break;
                case 15:
                    loadSampleOrders();
                    break;
                case 0:
                    running = false;
                    break;
            }
        }

        System.out.println();
        System.out.println("Thank you for using the Food Ordering and Preparation Queue System.");
        System.out.println("Orders still in the queue: " + prepQueue.size());
        System.out.println("Goodbye!");
    }

    static void showMenu() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("   FOOD ORDERING AND PREPARATION QUEUE SYSTEM");
        System.out.println("==============================================");
        System.out.println(" ORDER RECORDS");
        System.out.println("   1.  Add new order");
        System.out.println("   2.  Display all orders");
        System.out.println("   3.  Search for an order");
        System.out.println("   4.  Update an order");
        System.out.println("   5.  Delete an order");
        System.out.println(" PREPARATION QUEUE");
        System.out.println("   6.  Add an order to the queue (enqueue)");
        System.out.println("   7.  Add all waiting orders to the queue");
        System.out.println("   8.  Prepare next order (dequeue)");
        System.out.println("   9.  Peek at next order");
        System.out.println("   10. Display the queue");
        System.out.println("   11. Check if the queue is empty");
        System.out.println(" SORTING");
        System.out.println("   12. Bubble Sort orders by total amount");
        System.out.println("   13. Selection Sort orders by customer name");
        System.out.println(" OTHER");
        System.out.println("   14. Show pending and completed orders");
        System.out.println("   15. Load sample orders");
        System.out.println("   0.  Exit");
        System.out.println("----------------------------------------------");
    }

    static String readLine(String prompt) {
        System.out.print(prompt);
        if (!input.hasNextLine()) {
            System.out.println();
            System.out.println("No more input available. Closing the program.");
            System.exit(0);
        }
        return input.nextLine().trim();
    }

    static int readInt(String prompt, int min, int max) {
        while (true) {
            String text = readLine(prompt);
            try {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Error: enter a number from " + min + " to " + max + ".");
            } catch (NumberFormatException e) {
                if (text.isEmpty()) {
                    System.out.println("Error: input cannot be empty.");
                } else {
                    System.out.println("Error: \"" + text + "\" is not a valid whole number.");
                }
            }
        }
    }

    static double readPrice(String prompt) {
        while (true) {
            String text = readLine(prompt);
            try {
                double value = Double.parseDouble(text);
                if (value > 0 && value <= 10000) {
                    return Math.round(value * 100.0) / 100.0;
                }
                System.out.println("Error: price must be more than 0 and not above 10000.");
            } catch (NumberFormatException e) {
                System.out.println("Error: \"" + text + "\" is not a valid price.");
            }
        }
    }

    static String readName(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (text.isEmpty()) {
                System.out.println("Error: name cannot be empty.");
            } else if (text.length() > 25) {
                System.out.println("Error: name must not exceed 25 characters.");
            } else if (!text.matches("[A-Za-z][A-Za-z .'-]*")) {
                System.out.println("Error: name may only contain letters, spaces, periods, hyphens and apostrophes.");
            } else {
                return text;
            }
        }
    }

    static String readItem(String prompt) {
        while (true) {
            String text = readLine(prompt);
            if (text.isEmpty()) {
                System.out.println("Error: food item cannot be empty.");
            } else if (text.length() > 25) {
                System.out.println("Error: food item must not exceed 25 characters.");
            } else if (!text.matches("[A-Za-z0-9][A-Za-z0-9 .,'&()-]*")) {
                System.out.println("Error: food item contains invalid characters.");
            } else {
                return text;
            }
        }
    }

    static boolean readYesNo(String prompt) {
        while (true) {
            String text = readLine(prompt).toLowerCase();
            if (text.equals("y") || text.equals("yes")) {
                return true;
            }
            if (text.equals("n") || text.equals("no")) {
                return false;
            }
            System.out.println("Error: please answer y or n.");
        }
    }

    static boolean readAscending() {
        System.out.println("Sort direction:");
        System.out.println("  1. Ascending");
        System.out.println("  2. Descending");
        return readInt("Choose direction: ", 1, 2) == 1;
    }

    static int findIndexById(int id) {
        for (int i = 0; i < orderIds.size(); i++) {
            if (orderIds.get(i) == id) {
                return i;
            }
        }
        return -1;
    }

    static void printTableHeader() {
        System.out.printf("%-8s %-26s %-26s %-4s %-11s %-10s%n",
                "Order#", "Customer", "Food Item", "Qty", "Total", "Status");
        System.out.println("-----------------------------------------------------------------------------------");
    }

    static void printRow(int i) {
        System.out.printf("%-8d %-26s %-26s %-4d %-11.2f %-10s%n",
                orderIds.get(i), customerNames.get(i), itemNames.get(i),
                quantities.get(i), totalAmounts.get(i), statuses.get(i));
    }

    static void printAllRows() {
        printTableHeader();
        for (int i = 0; i < orderIds.size(); i++) {
            printRow(i);
        }
    }

    static void printQueueLine() {
        if (prepQueue.isEmpty()) {
            System.out.println("Queue order: (empty)");
            return;
        }
        StringBuilder line = new StringBuilder("Queue order (front to back): ");
        boolean first = true;
        for (int id : prepQueue) {
            if (!first) {
                line.append(" -> ");
            }
            line.append("#").append(id);
            first = false;
        }
        System.out.println(line);
    }

    static void addOrder() {
        System.out.println();
        System.out.println("--- ADD NEW ORDER ---");
        String name = readName("Customer name: ");
        String item = readItem("Food item: ");
        int qty = readInt("Quantity (1-50): ", 1, 50);
        double price = readPrice("Price per item: ");

        int id = nextOrderId;
        nextOrderId++;

        orderIds.add(id);
        customerNames.add(name);
        itemNames.add(item);
        quantities.add(qty);
        totalAmounts.add(Math.round(qty * price * 100.0) / 100.0);
        statuses.add(WAITING);

        System.out.println();
        System.out.println("Order #" + id + " has been saved. Status: " + WAITING);

        if (readYesNo("Add this order to the preparation queue now? (y/n): ")) {
            prepQueue.add(id);
            statuses.set(orderIds.size() - 1, IN_QUEUE);
            System.out.println("Order #" + id + " is now in the queue at position " + prepQueue.size() + ".");
        }
    }

    static void displayAllOrders() {
        System.out.println();
        System.out.println("--- ALL ORDERS ---");
        if (orderIds.isEmpty()) {
            System.out.println("No orders have been recorded yet.");
            return;
        }
        printAllRows();
        System.out.println("Total orders: " + orderIds.size());
    }

    static void searchOrders() {
        System.out.println();
        System.out.println("--- SEARCH ORDERS ---");
        if (orderIds.isEmpty()) {
            System.out.println("There are no orders to search.");
            return;
        }
        System.out.println("  1. Search by order number");
        System.out.println("  2. Search by customer name");
        int option = readInt("Choose search type: ", 1, 2);

        if (option == 1) {
            int id = readInt("Enter order number: ", 1, 999999);
            int index = findIndexById(id);
            if (index == -1) {
                System.out.println("No order found with number #" + id + ".");
            } else {
                System.out.println();
                printTableHeader();
                printRow(index);
            }
        } else {
            String keyword = readName("Enter customer name (or part of it): ").toLowerCase();
            int matches = 0;
            for (int i = 0; i < orderIds.size(); i++) {
                if (customerNames.get(i).toLowerCase().contains(keyword)) {
                    if (matches == 0) {
                        System.out.println();
                        printTableHeader();
                    }
                    printRow(i);
                    matches++;
                }
            }
            if (matches == 0) {
                System.out.println("No customer name matches \"" + keyword + "\".");
            } else {
                System.out.println(matches + " matching order(s) found.");
            }
        }
    }

    static void updateOrder() {
        System.out.println();
        System.out.println("--- UPDATE ORDER ---");
        if (orderIds.isEmpty()) {
            System.out.println("There are no orders to update.");
            return;
        }
        int id = readInt("Enter order number to update: ", 1, 999999);
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println("No order found with number #" + id + ".");
            return;
        }
        if (statuses.get(index).equals(COMPLETED)) {
            System.out.println("Order #" + id + " is already completed and cannot be changed.");
            return;
        }

        System.out.println();
        printTableHeader();
        printRow(index);
        System.out.println();
        System.out.println("  1. Customer name");
        System.out.println("  2. Food item");
        System.out.println("  3. Quantity");
        System.out.println("  4. Price per item");
        System.out.println("  0. Cancel");
        int option = readInt("What do you want to change? ", 0, 4);

        int qty = quantities.get(index);
        double unitPrice = totalAmounts.get(index) / qty;

        if (option == 1) {
            customerNames.set(index, readName("New customer name: "));
        } else if (option == 2) {
            itemNames.set(index, readItem("New food item: "));
        } else if (option == 3) {
            int newQty = readInt("New quantity (1-50): ", 1, 50);
            quantities.set(index, newQty);
            totalAmounts.set(index, Math.round(newQty * unitPrice * 100.0) / 100.0);
        } else if (option == 4) {
            double newPrice = readPrice("New price per item: ");
            totalAmounts.set(index, Math.round(qty * newPrice * 100.0) / 100.0);
        } else {
            System.out.println("Update cancelled.");
            return;
        }

        System.out.println();
        System.out.println("Order #" + id + " has been updated:");
        printTableHeader();
        printRow(index);
    }

    static void deleteOrder() {
        System.out.println();
        System.out.println("--- DELETE ORDER ---");
        if (orderIds.isEmpty()) {
            System.out.println("There are no orders to delete.");
            return;
        }
        int id = readInt("Enter order number to delete: ", 1, 999999);
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println("No order found with number #" + id + ".");
            return;
        }

        System.out.println();
        printTableHeader();
        printRow(index);
        if (!readYesNo("Delete this order? (y/n): ")) {
            System.out.println("Delete cancelled.");
            return;
        }

        if (statuses.get(index).equals(IN_QUEUE)) {
            prepQueue.remove(Integer.valueOf(id));
            System.out.println("Order #" + id + " was also removed from the preparation queue.");
        }

        orderIds.remove(index);
        customerNames.remove(index);
        itemNames.remove(index);
        quantities.remove(index);
        totalAmounts.remove(index);
        statuses.remove(index);

        System.out.println("Order #" + id + " has been deleted.");
    }

    static void enqueueOrder() {
        System.out.println();
        System.out.println("--- ADD ORDER TO QUEUE (ENQUEUE) ---");
        if (orderIds.isEmpty()) {
            System.out.println("There are no orders to queue. Add an order first.");
            return;
        }
        int id = readInt("Enter order number: ", 1, 999999);
        int index = findIndexById(id);
        if (index == -1) {
            System.out.println("No order found with number #" + id + ".");
            return;
        }
        String status = statuses.get(index);
        if (status.equals(IN_QUEUE)) {
            System.out.println("Order #" + id + " is already in the queue.");
            return;
        }
        if (status.equals(COMPLETED)) {
            System.out.println("Order #" + id + " has already been completed.");
            return;
        }

        prepQueue.add(id);
        statuses.set(index, IN_QUEUE);
        System.out.println("Order #" + id + " was added to the back of the queue (position " + prepQueue.size() + ").");
        printQueueLine();
    }

    static void enqueueAllWaiting() {
        System.out.println();
        System.out.println("--- ADD ALL WAITING ORDERS TO QUEUE ---");
        int added = 0;
        for (int id = 1001; id < nextOrderId; id++) {
            int index = findIndexById(id);
            if (index != -1 && statuses.get(index).equals(WAITING)) {
                prepQueue.add(id);
                statuses.set(index, IN_QUEUE);
                System.out.println("Queued order #" + id + " (" + customerNames.get(index) + ")");
                added++;
            }
        }
        if (added == 0) {
            System.out.println("There are no waiting orders to add.");
        } else {
            System.out.println(added + " order(s) added in order-number sequence.");
            printQueueLine();
        }
    }

    static void dequeueOrder() {
        System.out.println();
        System.out.println("--- PREPARE NEXT ORDER (DEQUEUE) ---");
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is empty. There is no order to prepare.");
            return;
        }
        int id = prepQueue.poll();
        int index = findIndexById(id);
        statuses.set(index, COMPLETED);

        System.out.println("Now preparing order #" + id + ":");
        printTableHeader();
        printRow(index);
        System.out.println();
        System.out.println("Order #" + id + " is done and marked as Completed.");

        if (prepQueue.isEmpty()) {
            System.out.println("No more orders are waiting in the queue.");
        } else {
            int nextId = prepQueue.peek();
            System.out.println("Next in line: order #" + nextId + " (" + customerNames.get(findIndexById(nextId)) + ")");
        }
    }

    static void peekQueue() {
        System.out.println();
        System.out.println("--- PEEK AT NEXT ORDER ---");
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is empty. Nothing to peek at.");
            return;
        }
        int id = prepQueue.peek();
        int index = findIndexById(id);
        System.out.println("The next order to be prepared is (it stays in the queue):");
        printTableHeader();
        printRow(index);
    }

    static void displayQueue() {
        System.out.println();
        System.out.println("--- PREPARATION QUEUE ---");
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is empty.");
            return;
        }
        System.out.printf("%-5s %-8s %-26s %-26s %-4s%n", "Pos", "Order#", "Customer", "Food Item", "Qty");
        System.out.println("-------------------------------------------------------------------------");
        int position = 1;
        for (int id : prepQueue) {
            int index = findIndexById(id);
            String marker = (position == 1) ? "  <- next" : "";
            System.out.printf("%-5d %-8d %-26s %-26s %-4d%s%n", position, id,
                    customerNames.get(index), itemNames.get(index), quantities.get(index), marker);
            position++;
        }
        System.out.println("Orders in queue: " + prepQueue.size());
    }

    static void checkQueueEmpty() {
        System.out.println();
        System.out.println("--- QUEUE STATUS ---");
        if (prepQueue.isEmpty()) {
            System.out.println("The queue is EMPTY.");
        } else {
            System.out.println("The queue is NOT empty. It has " + prepQueue.size() + " order(s).");
        }
    }

    static void swapRecords(int a, int b) {
        int tempId = orderIds.get(a);
        orderIds.set(a, orderIds.get(b));
        orderIds.set(b, tempId);

        String tempName = customerNames.get(a);
        customerNames.set(a, customerNames.get(b));
        customerNames.set(b, tempName);

        String tempItem = itemNames.get(a);
        itemNames.set(a, itemNames.get(b));
        itemNames.set(b, tempItem);

        int tempQty = quantities.get(a);
        quantities.set(a, quantities.get(b));
        quantities.set(b, tempQty);

        double tempTotal = totalAmounts.get(a);
        totalAmounts.set(a, totalAmounts.get(b));
        totalAmounts.set(b, tempTotal);

        String tempStatus = statuses.get(a);
        statuses.set(a, statuses.get(b));
        statuses.set(b, tempStatus);
    }

    static int bubbleSortByTotal(boolean ascending) {
        int n = orderIds.size();
        int swaps = 0;
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - pass; j++) {
                double left = totalAmounts.get(j);
                double right = totalAmounts.get(j + 1);
                boolean outOfOrder = ascending ? left > right : left < right;
                if (outOfOrder) {
                    swapRecords(j, j + 1);
                    swapped = true;
                    swaps++;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return swaps;
    }

    static int selectionSortByName(boolean ascending) {
        int n = orderIds.size();
        int swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            int target = i;
            for (int j = i + 1; j < n; j++) {
                int result = customerNames.get(j).compareToIgnoreCase(customerNames.get(target));
                boolean better = ascending ? result < 0 : result > 0;
                if (better) {
                    target = j;
                }
            }
            if (target != i) {
                swapRecords(i, target);
                swaps++;
            }
        }
        return swaps;
    }

    static void runBubbleSort() {
        System.out.println();
        System.out.println("--- BUBBLE SORT BY TOTAL AMOUNT ---");
        if (orderIds.isEmpty()) {
            System.out.println("There are no orders to sort.");
            return;
        }
        if (orderIds.size() == 1) {
            System.out.println("Only one order is recorded, so there is nothing to sort.");
            return;
        }
        boolean ascending = readAscending();

        System.out.println();
        System.out.println("BEFORE sorting:");
        printAllRows();
        printQueueLine();

        int swaps = bubbleSortByTotal(ascending);

        System.out.println();
        System.out.println("AFTER Bubble Sort (" + (ascending ? "lowest" : "highest") + " total first, " + swaps + " swap(s)):");
        printAllRows();
        printQueueLine();
        System.out.println("Note: sorting only rearranges the records. The queue was not changed.");
    }

    static void runSelectionSort() {
        System.out.println();
        System.out.println("--- SELECTION SORT BY CUSTOMER NAME ---");
        if (orderIds.isEmpty()) {
            System.out.println("There are no orders to sort.");
            return;
        }
        if (orderIds.size() == 1) {
            System.out.println("Only one order is recorded, so there is nothing to sort.");
            return;
        }
        boolean ascending = readAscending();

        System.out.println();
        System.out.println("BEFORE sorting:");
        printAllRows();
        printQueueLine();

        int swaps = selectionSortByName(ascending);

        System.out.println();
        System.out.println("AFTER Selection Sort (" + (ascending ? "A to Z" : "Z to A") + ", " + swaps + " swap(s)):");
        printAllRows();
        printQueueLine();
        System.out.println("Note: sorting only rearranges the records. The queue was not changed.");
    }

    static void showPendingAndCompleted() {
        System.out.println();
        System.out.println("--- PENDING AND COMPLETED ORDERS ---");
        if (orderIds.isEmpty()) {
            System.out.println("No orders have been recorded yet.");
            return;
        }

        int pending = 0;
        int completed = 0;

        System.out.println("PENDING (Waiting or In Queue):");
        printTableHeader();
        for (int i = 0; i < orderIds.size(); i++) {
            if (!statuses.get(i).equals(COMPLETED)) {
                printRow(i);
                pending++;
            }
        }
        if (pending == 0) {
            System.out.println("(none)");
        }

        System.out.println();
        System.out.println("COMPLETED:");
        printTableHeader();
        for (int i = 0; i < orderIds.size(); i++) {
            if (statuses.get(i).equals(COMPLETED)) {
                printRow(i);
                completed++;
            }
        }
        if (completed == 0) {
            System.out.println("(none)");
        }

        System.out.println();
        System.out.println("Pending: " + pending + "   Completed: " + completed);
    }

    static void loadSampleOrders() {
        System.out.println();
        System.out.println("--- LOAD SAMPLE ORDERS ---");
        String[] names = {"Maria Santos", "Juan Dela Cruz", "Ana Reyes", "Carlo Mendoza", "Liza Garcia"};
        String[] items = {"Chicken Adobo", "Beef Tapsilog", "Halo-Halo", "Pancit Canton", "Lumpia Shanghai"};
        int[] qtys = {2, 1, 3, 2, 4};
        double[] prices = {120.00, 95.50, 85.00, 70.00, 45.00};

        for (int i = 0; i < names.length; i++) {
            orderIds.add(nextOrderId);
            nextOrderId++;
            customerNames.add(names[i]);
            itemNames.add(items[i]);
            quantities.add(qtys[i]);
            totalAmounts.add(Math.round(qtys[i] * prices[i] * 100.0) / 100.0);
            statuses.add(WAITING);
        }
        System.out.println(names.length + " sample orders were added with status " + WAITING + ".");
        System.out.println("Use option 7 to put them in the queue.");
    }
}
