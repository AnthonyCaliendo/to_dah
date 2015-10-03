package com.anthonycaliendo.todah.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.model.Settings;
import com.anthonycaliendo.todah.model.Todo;
import com.anthonycaliendo.todah.model.TodoFilter;
import com.anthonycaliendo.todah.widget.TodoAdapter;

/**
 * Activity which displays todos.
 */
public class ViewTodosActivity extends AppCompatActivity {

    private static final int ADD_TODO_REQUEST_CODE  = 1;
    private static final int EDIT_TODO_REQUEST_CODE = 2;

    private TodoFilter todoFilter;
    private Settings   settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_todos);

        settings = initializeSettings();
        todoFilter = new TodoFilter(settings);

        final ListView todosListView   = (ListView) findViewById(R.id.view_todos_list);
        final TodoAdapter todoListAdapter = new TodoAdapter(this, todoFilter.getTodos());
        todosListView.setAdapter(todoListAdapter);

        setupAddAction();
        setupEditAction(todosListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_todos, menu);

        updateMenuItemText(
                menu.findItem(R.id.view_todos_show_pending_action),
                menu.findItem(R.id.view_todos_show_completed_action),
                menu.findItem(R.id.view_todos_show_late_action)
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_todos_show_pending_action:
                settings.toggleShowPending();
                updateMenuItemText(item, null, null);
                refreshTodos();
                return true;
            case R.id.view_todos_show_completed_action:
                settings.toggleShowCompleted();
                updateMenuItemText(null, item, null);
                refreshTodos();
                return true;
            case R.id.view_todos_show_late_action:
                settings.toggleShowLate();
                updateMenuItemText(null, null, item);
                refreshTodos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        debug("method=onActivityResult requestCode=" + requestCode + "resultCode=" + resultCode);

        if (resultCode == RESULT_OK) {
            refreshTodos();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Sets up the action to edit todos.
     * Configures a long-click listener on the list items to transition to the edit acvitity.
     * @param todosListView
     *      the listview which contains the todos
     */
    private void setupEditAction(final ListView todosListView) {
        todosListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Todo todo = (Todo) getTodosListAdapter().getItem(position);
                        debug("action=longPress id=" + todo.getId());
                        final Intent intent = new Intent(ViewTodosActivity.this, EditTodoActivity.class);

                        /* The row id doesn't get serialized even if you make the model serializable, which means
                         * it isn't updated and a new row is inserted.
                         * ActiveAndroid folks recommend not to serialize the object, and instead use gson:
                         *     https://github.com/pardom/ActiveAndroid/issues/60
                         * This feels like a lot of overhead, so just serialize the row id and look it up.
                         */
                        intent.putExtra(Todo.ID_INTENT_KEY, todo.getId());

                        startActivityForResult(intent, EDIT_TODO_REQUEST_CODE);
                    }
                }
        );
    }

    /**
     * Sets up the action to add new todos.
     * Configures an onclick listener on the action to transition to the edit acvitity.
     */
    private void setupAddAction() {
        findViewById(R.id.view_todos_add_todo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ViewTodosActivity.this, EditTodoActivity.class);
                startActivityForResult(intent, ADD_TODO_REQUEST_CODE);
            }
        });
    }

    /**
     * Refreshes the list of todos and updates the display.
     */
    private void refreshTodos() {
        final ArrayAdapter listAdapter = getTodosListAdapter();
        listAdapter.clear();
        listAdapter.addAll(todoFilter.getTodos());
        listAdapter.notifyDataSetChanged();
    }

    /**
     * Updated the menu item text to match the current filtering settings.
     * @param pending
     *      the pending menu item (optional)
     * @param completed
     *      the completed menu item (optional)
     * @param late
     *      the late menu item (optional)
     */
    private void updateMenuItemText(final MenuItem pending, final MenuItem completed, final MenuItem late) {
        if (pending != null && settings.isShowPending()) {
            pending.setTitle(getString(R.string.view_todos_menu_hide_pending));
        } else if (pending != null) {
            pending.setTitle(getString(R.string.view_todos_menu_show_pending));
        }

        if (completed != null && settings.isShowCompleted()) {
            completed.setTitle(getString(R.string.view_todos_menu_hide_completed));
        } else if (completed != null) {
            completed.setTitle(getString(R.string.view_todos_menu_show_completed));
        }

        if (late != null && settings.isShowLate()) {
            late.setTitle(getString(R.string.view_todos_menu_hide_late));
        } else if (late != null) {
            late.setTitle(getString(R.string.view_todos_menu_show_late));
        }
    }

    /**
     * Returns the list adapter used for the todos.
     * @return
     *      the list adapter for the todos
     */
    private ArrayAdapter getTodosListAdapter() {
        return (ArrayAdapter) ((ListView) findViewById(R.id.view_todos_list)).getAdapter();
    }

    /**
     * Load or initialize settings for this app.
     * @return
     *      the settings for this app
     */
    private Settings initializeSettings() {
        Settings settings = new Select()
                .from(Settings.class)
                .limit(1)
                .executeSingle();

        if (settings == null) {
            settings = new Settings();
        }

        return settings;
    }

    /**
     * Logs the message at DEBUG level.
     * @param message
     *      the message to log
     */
    private void debug(final String message) {
        Log.d(this.getClass().getSimpleName(), message);
    }

}
