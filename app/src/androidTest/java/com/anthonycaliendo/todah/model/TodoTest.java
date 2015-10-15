package com.anthonycaliendo.todah.model;

import com.anthonycaliendo.todah.ActiveAndroidTestCase;

import java.util.Calendar;

public class TodoTest extends ActiveAndroidTestCase {

    public void testComareTo_SortsBasedOnPriority() {
        final Todo topPriority = new Todo();
        topPriority.setPriority(0);
        final Todo middlePriority = new Todo();
        middlePriority.setPriority(1);
        final Todo lowestPriority = new Todo();
        lowestPriority.setPriority(2);
        final Todo samePriorityAsMiddle = new Todo();
        samePriorityAsMiddle.setPriority(1);

        assertEquals(-1, topPriority.compareTo(middlePriority));
        assertEquals(0, samePriorityAsMiddle.compareTo(middlePriority));
        assertEquals(1, lowestPriority.compareTo(topPriority));
        assertEquals(1, lowestPriority.compareTo(middlePriority));
        assertEquals("null should be highest priority", 1, topPriority.compareTo(null));
    }

    public void testComplete_SetsCompletedOn() {
        final Todo todo = new Todo();
        todo.complete();

        assertNotNull("should set completedOn", todo.getCompletedOn());
        assertEquals("should set completed status", Todo.Status.COMPLETED, todo.getStatus());
    }

    public void testIsCompleted_BasedOffCompletedStatus() {
        final Todo completed = new Todo();
        final Todo pending   = new Todo();

        completed.complete();

        assertFalse("should not be completed when pending", pending.isCompleted());
        assertTrue("should be completed", completed.isCompleted());
    }

    public void testIsLate_BasedOffCurrentDate() {
        final Todo late                  = new Todo();
        final Todo noDueDate             = new Todo();
        final Todo dueInTheFuture        = new Todo();
        final Todo dueInPastButCompleted = new Todo();

        late.setDueDate(Calendar.getInstance());

        dueInPastButCompleted.setDueDate(Calendar.getInstance());
        dueInPastButCompleted.complete();

        final Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.YEAR, 10);
        dueInTheFuture.setDueDate(futureDate);

        assertFalse("should not be late when no due date", noDueDate.isLate());
        assertFalse("should not be late when due date in the future", dueInTheFuture.isLate());
        assertFalse("should not be late when completed", dueInPastButCompleted.isLate());
        assertTrue("should be late when pending and past due date", late.isLate());
    }

    public void testIsPending_BasedOffOfPendingStatus() {
        final Todo pending   = new Todo();
        final Todo completed = new Todo();
        final Todo late      = new Todo();

        completed.complete();
        late.setDueDate(Calendar.getInstance());

        assertTrue("should be pending", pending.isPending());
        assertFalse("should not be pending when completed", pending.isCompleted());
        assertTrue("should be pending when late", late.isPending());
    }

    public void testToggleCompleted_Completed_SwitchesToPendingAndClearsCompletedOn() {
        final Todo todo = new Todo();
        todo.complete();

        todo.toggleCompleted();

        assertNull("should not have completedOn", todo.getCompletedOn());
        assertTrue("should be pending", todo.isPending());
    }

    public void testToggleCompleted_Pending_SwitchesToCompletedAndSetsCompletedOn() {
        final Todo todo = new Todo();

        todo.toggleCompleted();

        assertNotNull("should have completedOn set", todo.getCompletedOn());
        assertTrue("should be completed", todo.isCompleted());
    }
}
