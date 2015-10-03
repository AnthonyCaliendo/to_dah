package com.anthonycaliendo.todah.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Calendar;

/**
 * Information about a single To-do item.
 */
@Table(name = "Todos")
public class Todo extends Model implements Comparable<Todo> {

    public static final String ID_INTENT_KEY = "todo.id";

    /**
     * The main title of this task.  Used in the main list view, and the only required piece of info.
     */
    @Column
    private String title;
    /**
     * Extra notes the user can store about this to-do.
     */
    @Column
    private String description;
    /**
     * The priority, which will be used for sorting and prioritization by the user.
     */
    @Column
    private long priority;
    /**
     * Internal field used to manage the state of the to-do (e.g. completed or pending).
     */
    @Column
    private Status status;
    /**
     * The due date for this to-do.  Once the due date elapses, the to-do is considered late.
     */
    @Column
    private Calendar dueDate;
    /**
     * The date the to-do was completed on. Tracked for some future functionality.
     */
    @Column
    private Calendar completedOn;

    public Todo() {
        super();
        this.status = Status.PENDING;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDue) {
        this.dueDate = dueDue;
    }

    public Calendar getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Calendar completedOn) {
        this.completedOn = completedOn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int compareTo(final Todo another) {
        if (another == null || getPriority() > another.getPriority()) {
            return 1;
        } else if (getPriority() == another.getPriority()) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * Completes this to-do, setting the status and completed date.
     */
    public void complete() {
        setCompletedOn(Calendar.getInstance());
        setStatus(Status.COMPLETED);
    }

    public boolean isCompleted() {
        return getStatus() == Status.COMPLETED;
    }

    public boolean isLate() {
        if (getDueDate() == null) {
            return false;
        }
        return !isCompleted() && Calendar.getInstance().compareTo(getDueDate()) >= 0;
    }

    public enum Status {
        PENDING,
        COMPLETED
    }
}
