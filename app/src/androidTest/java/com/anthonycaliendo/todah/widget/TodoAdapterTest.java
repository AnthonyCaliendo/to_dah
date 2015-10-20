package com.anthonycaliendo.todah.widget;

import android.graphics.Color;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.TextView;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.activity.ViewTodosActivity;
import com.anthonycaliendo.todah.model.Todo;

import java.util.Arrays;
import java.util.Calendar;

public class TodoAdapterTest extends ActivityInstrumentationTestCase2<ViewTodosActivity> {

    public TodoAdapterTest() {
        super(ViewTodosActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);
    }

    @UiThreadTest
    public void testGetView_LateTodo_SetsRedText() {
        final Todo todo        = new Todo();
        final Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, -1);
        todo.setDueDate(dueDate);

        final TodoAdapter adapter = new TodoAdapter(getInstrumentation().getTargetContext(), Arrays.asList(todo));
        final View todoItemView   = adapter.getView(0, null, null);
        final TextView titleView  = (TextView) todoItemView.findViewById(R.id.item_todo_title);

        assertEquals("text color should be red for late todos", Color.RED, titleView.getCurrentTextColor());
    }

    @UiThreadTest
    public void testGetView_PendingTodo_SetsBlackText() {
        final Todo todo           = new Todo();
        final TodoAdapter adapter = new TodoAdapter(getInstrumentation().getTargetContext(), Arrays.asList(todo));
        final View todoItemView   = adapter.getView(0, null, null);
        final TextView titleView  = (TextView) todoItemView.findViewById(R.id.item_todo_title);

        assertEquals("text color should be black for pending todo", Color.BLACK, titleView.getCurrentTextColor());
    }

    @UiThreadTest
    public void testGetView_CompletedTodo_SetsBlackText() {
        final Todo todo = new Todo();
        todo.complete();

        final TodoAdapter adapter = new TodoAdapter(getInstrumentation().getTargetContext(), Arrays.asList(todo));
        final View todoItemView   = adapter.getView(0, null, null);
        final TextView titleView  = (TextView) todoItemView.findViewById(R.id.item_todo_title);

        assertEquals("text color should be black for pending todo", Color.BLACK, titleView.getCurrentTextColor());
    }

    @UiThreadTest
    public void testGetView_PendingTodo_HidesCompletedIndicator() {
        final Todo todo = new Todo();

        final TodoAdapter adapter     = new TodoAdapter(getInstrumentation().getTargetContext(), Arrays.asList(todo));
        final View todoItemView       = adapter.getView(0, null, null);
        final View completedView = todoItemView.findViewById(R.id.item_todo_is_completed);

        assertEquals("completed indicator should not be visible", View.INVISIBLE, completedView.getVisibility());
    }

    @UiThreadTest
    public void testGetView_LateTodo_HidesCompletedIndicator() {
        final Todo todo        = new Todo();
        final Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, -1);
        todo.setDueDate(dueDate);

        final TodoAdapter adapter     = new TodoAdapter(getInstrumentation().getTargetContext(), Arrays.asList(todo));
        final View todoItemView       = adapter.getView(0, null, null);
        final View completedView = todoItemView.findViewById(R.id.item_todo_is_completed);

        assertEquals("completed indicator should not be visible", View.INVISIBLE, completedView.getVisibility());
    }

    @UiThreadTest
    public void testGetView_CompletedTodo_HidesCompletedIndicator() {
        final Todo todo = new Todo();
        todo.complete();

        final TodoAdapter adapter     = new TodoAdapter(getInstrumentation().getTargetContext(), Arrays.asList(todo));
        final View todoItemView       = adapter.getView(0, null, null);
        final View completedView = todoItemView.findViewById(R.id.item_todo_is_completed);

        assertEquals("completed indicator should be visible", View.VISIBLE, completedView.getVisibility());
    }
}
