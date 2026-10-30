# Food Ordering and Preparation Queue System

A console-based Java program for a small restaurant or cafeteria. It keeps a record of customer orders, puts them in a preparation queue, and serves them in the order they were added. Bubble Sort and Selection Sort are used to arrange the order records.

**Name:** Francis Silva  
**Course / Section:** BSIT/IT2A  
**Instructor:** Miguel Edward Rosal

## Project Description

When orders pile up in a cafeteria, they should be prepared in the order customers placed them, otherwise the people who came first end up waiting the longest. Staff also need a way to look through the orders, for example to see the biggest orders or to find a customer by name.

This program handles both. A queue keeps the preparation line first in, first out (FIFO), and two sorting algorithms rearrange the order records so they are easier to review. Sorting never changes the queue, so the preparation order stays fair.

The program is written without OOP. Everything is done with static methods, ArrayLists, and one Queue.

## Objectives

- Store food orders in ArrayLists and keep the lists in sync.
- Write Bubble Sort and Selection Sort by hand, without Java's built-in sort methods.
- Use a Queue to prepare orders in FIFO order.
- Make sure sorting the records does not change the queue.
- Validate all input so the program does not crash on bad entries.

## Use Cases and Features

**Order records**
- Add a new order (customer name, food item, quantity, price per item). The total is computed automatically.
- Display all orders.
- Search by order number or by customer name.
- Update an order that is not yet completed.
- Delete an order. If it is in the queue, it is removed from the queue too.

**Preparation queue**
- Enqueue one order, or enqueue all waiting orders at once.
- Dequeue to prepare the next order. It is marked as Completed.
- Peek at the next order without removing it.
- Display the queue from front to back.
- Check if the queue is empty.

**Sorting**
- Bubble Sort by total amount, ascending or descending.
- Selection Sort by customer name, A to Z or Z to A.

**Other**
- Show pending and completed orders separately.
- Load sample orders for quick testing.

An order has one of three statuses: Waiting (saved but not queued), In Queue, or Completed.

## Data Structure Explanation

### ArrayLists

Each order is stored across six ArrayLists. The same index in every list belongs to the same order.

| ArrayList | Holds |
|-----------|-------|
| `orderIds` | Order number, starting at 1001 |
| `customerNames` | Customer name |
| `itemNames` | Food item |
| `quantities` | Number of items |
| `totalAmounts` | Quantity times price |
| `statuses` | Waiting, In Queue, or Completed |

Whenever an order is added, deleted, or moved during sorting, all six lists change together so the indexes stay matched. The `swapRecords()` method swaps all six lists at once.

### Queue

```java
static Queue<Integer> prepQueue = new LinkedList<>();
```

The queue stores only order numbers, not the full records. When an order is dequeued, `findIndexById()` looks up its details in the ArrayLists. Because of this, the lists can be sorted freely without touching the queue.

| Operation | Code |
|-----------|------|
| Enqueue | `prepQueue.add(id)` |
| Dequeue | `prepQueue.poll()` |
| Peek | `prepQueue.peek()` |
| Display | for-each loop over the queue |
| Empty check | `prepQueue.isEmpty()` |

## Sorting Algorithm Explanation

Both sorts are written by hand and use `swapRecords()` so every ArrayList stays aligned. The program prints the records and the queue before and after each sort.

### Bubble Sort (by total amount)

Bubble Sort compares each pair of neighbors and swaps them if they are in the wrong order. After every pass, the largest remaining value ends up at the end. If a whole pass has no swaps, the list is already sorted and the loop stops.

It fits this program because order totals are numbers and the list of orders is small. It is used to see the cheapest or most expensive orders first.

### Selection Sort (by customer name)

Selection Sort goes through the unsorted part of the list, finds the smallest name (or the largest, for Z to A), and swaps it into the next position. Names are compared with `compareToIgnoreCase`, so capital letters do not change the result.

It fits this program because it makes at most one swap per position, and each swap here moves six lists. It is used to list customers alphabetically.

## How to Run the Program

You need a JDK (version 8 or newer).

### Using IntelliJ IDEA

1. Open IntelliJ IDEA and choose **File > Open**, then select the project folder that contains `Main.java`.
2. If IntelliJ asks for a JDK, go to **File > Project Structure > Project** and pick an installed JDK.
3. Open `Main.java`.
4. Click the green run arrow next to `public static void main`, or press **Shift + F10**.
5. The program runs in the Run window at the bottom. Click inside it, type your input, and press Enter.

### Using the terminal

From the folder that contains `Main.java`:

```
javac Main.java
java Main
```

## Screenshots of the System

### 1. Main menu
![Main menu](screenshots/Main%20Menu.png)

*The main menu.*

### 2. Adding and displaying orders
![Adding and displaying orders](screenshots/Adding%20and%20Displaying%20Orders.png)

*Orders 1001 to 1004 in the records table.*

### 3. Searching
![Search by customer name](screenshots/Search.png)

*Searching for "Francis" by customer name.*

### 4. Updating
![Update order](screenshots/Update.png)

*Changing the quantity of order 1001 from 2 to 5. The total changes from 198.00 to 495.00.*

### 5. Deleting
![Delete order](screenshots/Delete.png)

*Deleting order 1002. It was in the queue, so it is removed from the queue as well.*

### 6. Queue operations
![Enqueue](screenshots/Enqueue.png)

*Enqueue on order 1001, which is already in the queue, so it is refused.*

![Peek at next order](screenshots/Peek.png)

*Peek shows order 1004 as the next order without removing it.*

![Display queue](screenshots/Display%20queue.png)

*Display queue, front to back. Order 1004 is next, followed by 1005.*

![Empty queue](screenshots/Empty%20queue.png)

*Display queue when it is empty.*

### 7. FIFO behavior
![First in](screenshots/FIFO_first%20in.png)

*Orders 1001, 1002, and 1003 are added to the queue in that order.*

![First out](screenshots/FIFO_first%20out.png)

*Dequeue prepares order 1001 first, since it was added first. Order 1002 is next.*

### 8. Bubble Sort
![Bubble Sort ascending](screenshots/Bubble%20sort_Ascending.png)

*Sorted by total amount, ascending. The queue order stays the same.*

![Bubble Sort descending](screenshots/Bubble%20Sort_Descending.png)

*Sorted by total amount, descending. The queue order stays the same.*

### 9. Selection Sort
![Selection Sort ascending](screenshots/Selection%20Sort_Ascending.png)

*Sorted by customer name, A to Z. The queue order stays the same.*

![Selection Sort descending](screenshots/Selection%20Sort_Descending.png)

*Sorted by customer name, Z to A. The queue order stays the same.*

### 10. Input validation
![Input validation](screenshots/Input%20Validation.png)

*Invalid menu entries ("francis", "silva", 23, 16) are rejected and the menu asks again.*

## Project Structure

```
DSA_Java_Sorting_Queue/
├── Main.java
├── README.md
└── screenshots/
```
