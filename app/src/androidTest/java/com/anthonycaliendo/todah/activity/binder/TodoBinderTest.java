package com.anthonycaliendo.todah.activity.binder;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.activity.EditTodoActivity;
import com.anthonycaliendo.todah.model.Todo;

import java.util.Calendar;

public class TodoBinderTest extends ActivityInstrumentationTestCase2<EditTodoActivity> {

    View fieldsView;

    public TodoBinderTest() {
        super(EditTodoActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        setActivityInitialTouchMode(true);

        fieldsView = getActivity().findViewById(R.id.edit_todo_view);
    }

    @UiThreadTest
    public void testBindTo_SetsValuesOnTodo() {
        final TodoBinder binder = new TodoBinder(fieldsView);
        final Todo todo         = new Todo();

        final EditText     titleInput       = (EditText)     fieldsView.findViewById(R.id.edit_todo_title_input);
        final EditText     descriptionInput = (EditText)     fieldsView.findViewById(R.id.edit_todo_description_input);
        final DatePicker   dueDateInput     = (DatePicker)   fieldsView.findViewById(R.id.edit_todo_due_date_input);
        final NumberPicker priorityInput    = (NumberPicker) fieldsView.findViewById(R.id.edit_todo_priority_input);

        titleInput.setText("title value");
        descriptionInput.setText("description value");
        dueDateInput.init(2015, 10, 5, null);
        priorityInput.setValue(7);

        final Calendar expectedDueDate = Calendar.getInstance();
        expectedDueDate.set(2015, 10, 5, 23, 59, 59);

        binder.bindTo(todo);

        assertEquals("should bind the title", "title value", todo.getTitle());
        assertEquals("should bind the description", "description value", todo.getDescription());
        assertEquals("should bind the due date", expectedDueDate, todo.getDueDate());
        assertEquals("should bind the priority", 7, todo.getPriority());
    }

    @UiThreadTest
    public void testBindFrom_SetsValuesOnView() {
        final TodoBinder binder = new TodoBinder(fieldsView);
        final Todo todo         = new Todo();

        final EditText     titleInput       = (EditText)     fieldsView.findViewById(R.id.edit_todo_title_input);
        final EditText     descriptionInput = (EditText)     fieldsView.findViewById(R.id.edit_todo_description_input);
        final DatePicker   dueDateInput     = (DatePicker)   fieldsView.findViewById(R.id.edit_todo_due_date_input);
        final NumberPicker priorityInput    = (NumberPicker) fieldsView.findViewById(R.id.edit_todo_priority_input);

        final Calendar expectedDueDate = Calendar.getInstance();
        expectedDueDate.set(2015, 10, 5, 23, 59, 59);

        todo.setTitle("title value");
        todo.setDescription("description value");
        todo.setDueDate(expectedDueDate);
        todo.setPriority(7);

        binder.bindFrom(todo);

        assertEquals("should bind the title", "title value", titleInput.getText().toString());
        assertEquals("should bind the description", "description value", descriptionInput.getText().toString());
        assertEquals("should bind the due date year", 2015, dueDateInput.getYear());
        assertEquals("should bind the due date month", 10, dueDateInput.getMonth());
        assertEquals("should bind the due date day", 5, dueDateInput.getDayOfMonth());
        assertEquals("should bind the priority", 7, priorityInput.getValue());
    }
}
