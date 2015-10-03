package com.anthonycaliendo.todah.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.model.Todo;

import java.util.Calendar;

/**
 * Activity which edits Todos.  This will create new Todos, or update existing Todos.
 */
public class EditTodoActivity extends AppCompatActivity {

    private Todo   todo;
    private Inputs inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        todo   = initializeTodo();
        inputs = new Inputs();

        inputs.bindFrom(todo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_todo, menu);
        toggleMenuItems(menu);
        return result;
    }

    /**
     * Hides menu items based on if this is a editing a new or existing object.
     * @param menu
     *      the menu to update
     */
    private void toggleMenuItems(Menu menu) {
        final boolean isNewTodo = todo.getId() == null;

        if (isNewTodo || todo.isCompleted()) {
            menu.findItem(R.id.edit_todo_complete_todo_action).setVisible(false);
        }

        if (isNewTodo) {
            menu.findItem(R.id.edit_todo_delete_todo_action).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_todo_save_todo_action:
                saveAndFinish(todo, inputs);
                return true;
            case R.id.edit_todo_complete_todo_action:
                completeAndFinish(todo, inputs);
                return true;
            case R.id.edit_todo_delete_todo_action:
                promptForDelete(todo);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Binds the fields to the model and persists those changes, then finishes the activity.
     * @param todo
     *      the model object being saved
     * @param inputs
     *      the input fields which contain the values to be applied
     */
    private void saveAndFinish(final Todo todo, final Inputs inputs) {
        inputs.bindTo(todo);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    /**
     * Binds the fields to the model and persists those changes, completes the todo, then finishes the activity.
     * @param todo
     *      the model object being completed
     * @param inputs
     *      the input fields which contain the values to be applied
     */
    private void completeAndFinish(final Todo todo, final Inputs inputs) {
        debug("action=complete id=" + todo.getId());
        todo.complete();
        saveAndFinish(todo, inputs);
    }

    /**
     * Prompts the user whether they are sure they want to delete.
     * @param todo
     *      the object to delete
     */
    private void promptForDelete(final Todo todo) {
        AlertDialog deleteConfirmation = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.edit_todo_delete_confirm_title))
                .setMessage(getString(R.string.edit_todo_delete_confirm_message))
                .setIcon(R.drawable.ic_delete_white_24dp)
                .setPositiveButton(getString(R.string.edit_todo_delete_confirm_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteAndFinish(todo);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.edit_todo_delete_confirm_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();
        deleteConfirmation.show();
    }

    /**
     * Deletes this object and finishes this activity.
     * @param todo
     *  the object to delete
     */
    private void deleteAndFinish(final Todo todo) {
        debug("action=delete id=" + todo.getId());
        todo.delete();
        setResult(RESULT_OK, getIntent());
        finish();
    }

    /**
     * Gets the model object being edited.
     * @return
     *      the existing or new model object which is being edited
     */
    private Todo initializeTodo() {
        final Intent intent = getIntent();
        final long   todoId = intent.getLongExtra(Todo.ID_INTENT_KEY, -1);
              Todo   todo   = Todo.load(Todo.class, todoId);

        debug("method=initializeTodo id=" + todoId);

        if (todo == null) {
            todo = new Todo();
        }

        return todo;
    }

    /**
     * Logs the message at DEBUG level.
     * @param message
     *      the message to log
     */
    private void debug(final String message) {
        Log.d(this.getClass().getSimpleName(), message);
    }

    /**
     * Used to bind the EditTodoActivity input fields to and from the model.
     */
    private class Inputs {
        final EditText     titleInput;
        final EditText     descriptionInput;
        final DatePicker   dueDateInput;
        final NumberPicker priorityInput;

        Inputs() {
            titleInput       = (EditText)     findViewById(R.id.edit_todo_title_input);
            descriptionInput = (EditText)     findViewById(R.id.edit_todo_description_input);
            dueDateInput     = (DatePicker)   findViewById(R.id.edit_todo_due_date_input);
            priorityInput    = (NumberPicker) findViewById(R.id.edit_todo_priority_input);

            priorityInput.setMinValue(0);
            priorityInput.setMaxValue(9);
        }

        /**
         * Sets the values in the input fields based on the values on the model object.
         * @param todo
         *      the model which has the values to be used to populate the input fields
         */
        void bindFrom(final Todo todo) {
            if (todo == null) {
                return;
            }

            titleInput.setText(todo.getTitle());
            descriptionInput.setText(todo.getDescription());
            priorityInput.setValue(Long.valueOf(todo.getPriority()).intValue());
            applyCalendar(dueDateInput, todo.getDueDate());
        }

        /**
         * Sets the values on the model object based on the values in the input fields.
         * @param todo
         *      the model to set the values on
         */
        void bindTo(Todo todo) {
            if (todo == null) {
                todo = new Todo();
            }

            todo.setTitle(titleInput.getText().toString());
            todo.setDescription(descriptionInput.getText().toString());
            todo.setDueDate(convertToCalendar(dueDateInput));
            todo.setPriority(priorityInput.getValue());
            debug("action=save id=" + todo.getId());
            todo.save();
        }

        /**
         * Sets the values in the DatePicker based on the values in the Calendar.
         * @param datePicker
         *      the date picker to set the values on
         * @param calendar
         *      the calendar to provide the values to use
         */
        private void applyCalendar(final DatePicker datePicker, final Calendar calendar) {
            if (calendar == null) {
                return;
            }
            datePicker.init(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    null
            );
        }

        /**
         * Converts the DatePicker into a Calendar instance, using the values from the DatePicker.
         * @param datePicker
         *      the date picker whose values will be used to populate the calendar
         * @return
         *      the populated Calendar instance
         */
        private Calendar convertToCalendar(final DatePicker datePicker) {
            final Calendar convertedCalendar = Calendar.getInstance();
            convertedCalendar.set(
                    datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth()
            );

            return convertedCalendar;
        }
    }
}
