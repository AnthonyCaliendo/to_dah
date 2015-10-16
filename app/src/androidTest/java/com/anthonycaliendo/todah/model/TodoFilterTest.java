package com.anthonycaliendo.todah.model;

import com.activeandroid.query.Delete;
import com.anthonycaliendo.todah.ActiveAndroidTestCase;

import java.util.Calendar;
import java.util.List;

public class TodoFilterTest extends ActiveAndroidTestCase {

    Todo pending;
    Todo completed;
    Todo late;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        new Delete().from(Todo.class).execute();

        pending = createTodo();

        completed = createTodo();
        completed.complete();

        final Calendar lateDueDate = Calendar.getInstance();
        lateDueDate.add(Calendar.DAY_OF_MONTH, -1);
        late = createTodo(lateDueDate);
    }

    private Todo createTodo() {
        return createTodo(null);
    }

    private Todo createTodo(final Calendar dueDate) {
        final Todo todo = new Todo();
        todo.setDueDate(dueDate);

        assertNotNull(
                "precondition failure: could not save todo!",
                todo.save()
        );

        return todo;
    }

    public void testGetTodos_OnlyShowPending_ReturnsOnlyPendingTodos() {
        final Settings settings = new Settings();
        settings.setShowPending(true);
        settings.setShowCompleted(false);
        settings.setShowLate(false);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should only have a single todo", 1, todos.size());
        assertTrue("should contain pending todo", todos.contains(pending));
    }

    public void testGetTodos_OnlyShowCompleted_ReturnsOnlyCompletedTodos() {
        final Settings settings = new Settings();
        settings.setShowPending(false);
        settings.setShowCompleted(true);
        settings.setShowLate(false);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should only have a single todo", 1, todos.size());
        assertTrue("should contain completed todo", todos.contains(completed));
    }

    public void testGetTodos_OnlyShowLate_ReturnsOnlyLateTodos() {
        final Settings settings = new Settings();
        settings.setShowPending(false);
        settings.setShowCompleted(false);
        settings.setShowLate(true);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should only have a single todo", 1, todos.size());
        assertTrue("should contain late todo", todos.contains(late));
    }

    public void testGetTodos_OnlyShowLateAndCompleted_ReturnsOnlyCompletedAndLate() {
        final Settings settings = new Settings();
        settings.setShowPending(false);
        settings.setShowCompleted(true);
        settings.setShowLate(true);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should have 2 todos", 2, todos.size());
        assertTrue("should contain completed todo", todos.contains(completed));
        assertTrue("should contain late todo", todos.contains(late));
    }

    public void testGetTodos_OnlyShowPendingAndCompleted_ReturnsOnlyPendingAndCompleted() {
        final Settings settings = new Settings();
        settings.setShowPending(true);
        settings.setShowCompleted(true);
        settings.setShowLate(false);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should have 2 todos", 2, todos.size());
        assertTrue("should contain pending todo", todos.contains(pending));
        assertTrue("should contain completed todo", todos.contains(completed));
    }

    public void testGetTodos_ShowAll_ReturnsAllTodos() {
        final Settings settings = new Settings();
        settings.setShowPending(true);
        settings.setShowCompleted(true);
        settings.setShowLate(true);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should have 3 todos", 3, todos.size());
        assertTrue("should contain pending todo", todos.contains(pending));
        assertTrue("should contain completed todo", todos.contains(completed));
        assertTrue("should contain late todo", todos.contains(late));
    }

    public void testGetTodos_ShowNone_ReturnsNoTodos() {
        final Settings settings = new Settings();
        settings.setShowPending(false);
        settings.setShowCompleted(false);
        settings.setShowLate(false);

        final TodoFilter todoFilter = new TodoFilter(settings);

        final List<Todo> todos = todoFilter.getTodos();
        assertEquals("should not have any todos", 0, todos.size());
    }
}
