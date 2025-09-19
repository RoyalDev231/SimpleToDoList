package org.example;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void addCreatesTaskAndAssignsIncreasingIds() {
        TodoList list = new TodoList();
        TodoList.Task t1 = list.add("First");
        TodoList.Task t2 = list.add("Second");
        assertEquals(1, t1.id());
        assertEquals(2, t2.id());
        assertEquals("First", t1.description());
        assertFalse(t1.isCompleted());
    }

    @Test
    void addRejectsNullOrBlank() {
        TodoList list = new TodoList();
        assertThrows(IllegalArgumentException.class, () -> list.add(null));
        assertThrows(IllegalArgumentException.class, () -> list.add("  "));
    }

    @Test
    void completeByIdWorks() {
        TodoList list = new TodoList();
        list.add("A"); // id 1
        list.add("B"); // id 2
        assertTrue(list.complete("2")); // completes "B"
        List<TodoList.Task> completed = list.complete();
        assertEquals(1, completed.size());
        assertEquals("B", completed.get(0).description());
    }

    @Test
    void completeByDescriptionWorksCaseInsensitive() {
        TodoList list = new TodoList();
        list.add("Buy Milk");
        assertTrue(list.complete("buy milk"));
        assertEquals(1, list.complete().size());
        assertEquals(0, list.incomplete().size());
    }

    @Test
    void completeHandlesNullBlankOrNotFoundGracefully() {
        TodoList list = new TodoList();
        list.add("X");
        assertFalse(list.complete(null));
        assertFalse(list.complete("  "));
        assertFalse(list.complete("Y")); // not found
        assertEquals(0, list.complete().size());
        assertEquals(1, list.incomplete().size());
    }

    @Test
    void completeSkipsAlreadyCompleted() {
        TodoList list = new TodoList();
        list.add("X");
        assertTrue(list.complete("X"));
        assertFalse(list.complete("X")); // already completed
        assertEquals(1, list.complete().size());
        assertEquals(0, list.incomplete().size());
    }

    @Test
    void listsAreSnapshotsAndUnmodifiable() {
        TodoList list = new TodoList();
        list.add("One");
        List<TodoList.Task> all = list.all();
        assertThrows(UnsupportedOperationException.class, () -> all.add(list.add("Two")));
    }

    @Test
    void duplicatesCompleteFirstIncompleteMatch() {
        TodoList list = new TodoList();
        list.add("Same");
        list.add("Same");
        assertTrue(list.complete("Same")); // completes the first incomplete "Same"
        assertEquals(1, list.complete().size());
        assertEquals(1, list.incomplete().size());
    }

    @Test
    void clearEmptiesEverything() {
        TodoList list = new TodoList();
        list.add("A");
        list.add("B");
        list.complete("A");
        list.clear();
        assertTrue(list.all().isEmpty());
        assertEquals("(No tasks — your list is empty)", list.prettyAll());
    }
}
