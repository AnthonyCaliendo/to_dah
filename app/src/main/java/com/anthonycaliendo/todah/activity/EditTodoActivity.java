package com.anthonycaliendo.todah.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.activity.binder.TodoBinder;
import com.anthonycaliendo.todah.model.Todo;

import static com.anthonycaliendo.todah.util.Instrumentation.debug;

/**
 * Activity which edits to-dos.  This will create new to-dos, update existing to-dos, mark to-dos as completed, or delete them altogether.
 */
public class EditTodoActivity extends AppCompatActivity {

    private Todo       todo;
    private TodoBinder inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        todo   = initializeTodo();
        inputs = new TodoBinder(findViewById(R.id.edit_todo_view));

        inputs.bindFrom(todo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_edit_todo, menu);
        toggleMenuItems(menu);
        return result;
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
     * Hides menu items based on if this is a editing a new or existing to-do.
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

    /**
     * Binds the fields to the to-do and persists those changes, then finishes the activity.
     * @param todo
     *      the to-do object being saved
     * @param inputs
     *      the input fields which contain the values to be applied to the to-do
     */
    private void saveAndFinish(final Todo todo, final TodoBinder inputs) {
        inputs.bindTo(todo);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    /**
     * Binds the fields to the to-do and persists those changes, completes the to-do, then finishes the activity.
     * This means that we will apply any pending edits to the to-do before marking it as completed!
     * @param todo
     *      the to-do object being completed
     * @param inputs
     *      the input fields which contain the values to be applied to the to-do
     */
    private void completeAndFinish(final Todo todo, final TodoBinder inputs) {
        debug(this, "action=complete id=" + todo.getId());
        todo.toggleCompleted();
        saveAndFinish(todo, inputs);
    }

    /**
     * Prompts the user for whether they are sure they want to delete.
     * @param todo
     *      the to-do to delete
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
     * Deletes this to-do and finishes this activity.
     * @param todo
     *      the to-do to delete
     */
    private void deleteAndFinish(final Todo todo) {
        debug(this, "action=delete id=" + todo.getId());
        todo.delete();
        setResult(RESULT_OK, getIntent());
        finish();
    }

    /**
     * Gets the to-do object being edited.
     * @return
     *      the existing or new to-do object which is being edited
     */
    private Todo initializeTodo() {
        final Intent intent = getIntent();
        final long   todoId = intent.getLongExtra(Todo.ID_INTENT_KEY, -1);
              Todo   todo   = Todo.load(Todo.class, todoId);

        debug(this, "method=initializeTodo id=" + todoId);

        if (todo == null) {
            todo = new Todo();
        }

        return todo;
    }

}
