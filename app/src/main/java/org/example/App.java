package org.example;

public class App {
    public static void main(String[] args) {
        TodoList list = new TodoList();

        // Example usage (mirrors your sample flow):
        list.add("Buy milk");
        list.add("Buy eggs");
        list.add("Prepare a lesson for CSC 122");
        list.add("Sow beet seeds");

        // You can complete by description or by numeric id as a string (e.g., "2")
        list.complete("Buy eggs");

        System.out.println("=== ALL ===");
        System.out.println(list.prettyAll());
        System.out.println();

        System.out.println("=== COMPLETE ===");
        System.out.println(list.prettyComplete());
        System.out.println();

        System.out.println("=== INCOMPLETE ===");
        System.out.println(list.prettyIncomplete());
        System.out.println();

        // Clear and show empty message
        list.clear();
        System.out.println("=== AFTER CLEAR ===");
        System.out.println(list.prettyAll());
    }
}