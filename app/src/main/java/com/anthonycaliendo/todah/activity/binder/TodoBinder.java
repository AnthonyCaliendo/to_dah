package com.anthonycaliendo.todah.activity.binder;

import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.model.Todo;

import static com.anthonycaliendo.todah.util.Instrumentation.debug;

/**
 * Used to bind the To-Do input fields to and from the To-do model object.
 */
public class TodoBinder extends Binder {

    private static final int MIN_PRIORITY = 0;
    private static final int MAX_PRIORITY = 9;

    private final EditText     titleInput;
    private final EditText     descriptionInput;
    private final DatePicker   dueDateInput;
    private final NumberPicker priorityInput;

    public TodoBinder(final View fieldsView) {
        titleInput       = (EditText)     fieldsView.findViewById(R.id.edit_todo_title_input);
        descriptionInput = (EditText)     fieldsView.findViewById(R.id.edit_todo_description_input);
        dueDateInput     = (DatePicker)   fieldsView.findViewById(R.id.edit_todo_due_date_input);
        priorityInput    = (NumberPicker) fieldsView.findViewById(R.id.edit_todo_priority_input);

        priorityInput.setMinValue(MIN_PRIORITY);
        priorityInput.setMaxValue(MAX_PRIORITY);
    }

    /**
     * Sets the values on the input fields based on the values on the to-do object.
     * @param todo
     *      the to-do object which has the values to be used to populate the input fields
     */
    public void bindFrom(final Todo todo) {
        if (todo == null) {
            return;
        }

        titleInput.setText(todo.getTitle());
        descriptionInput.setText(todo.getDescription());
        priorityInput.setValue(Long.valueOf(todo.getPriority()).intValue());
        applyCalendar(dueDateInput, todo.getDueDate());
    }

    /**
     * Sets the values on the to-do object based on the values on the input fields.
     * @param todo
     *      the to-do object to modify
     */
    public void bindTo(Todo todo) {
        if (todo == null) {
            todo = new Todo();
        }

        todo.setTitle(titleInput.getText().toString());
        todo.setDescription(descriptionInput.getText().toString());
        todo.setDueDate(convertToCalendar(dueDateInput));
        todo.setPriority(priorityInput.getValue());
        debug(this, "action=save id=" + todo.getId());
        todo.save();
    }

}