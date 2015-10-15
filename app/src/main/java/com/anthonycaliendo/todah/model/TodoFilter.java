package com.anthonycaliendo.todah.model;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.Collections;
import java.util.List;

import static com.anthonycaliendo.todah.util.Instrumentation.debug;

/**
 * Retrieves a filtered list of To-dos.
 */

public class TodoFilter {

    /**
     * Contains the settings we'll use for filtering
     */
    private final Settings settings;

    public TodoFilter(final Settings settings) {
        this.settings = settings;
    }

    /**
     * Applies filters and returns all matching to-dos.
     * @return
     *      the list of filtered to-dos
     */
    public List<Todo> getTodos() {
        final From query = new Select().from(Todo.class);

        final StringBuilder whereClause = new StringBuilder();
        if (settings.isShowPending() && settings.isShowCompleted()) {
            whereClause.append("status IN (\"");
            whereClause.append(Todo.Status.PENDING);
            whereClause.append("\",\"");
            whereClause.append(Todo.Status.COMPLETED);
            whereClause.append("\")");
        } else if (settings.isShowPending()) {
            whereClause.append("status = \"");
            whereClause.append(Todo.Status.PENDING);
            whereClause.append('"');
        } else if (settings.isShowCompleted()) {
            whereClause.append("status = \"");
            whereClause.append(Todo.Status.COMPLETED);
            whereClause.append('"');
        }

        query.where(whereClause.toString()).orderBy("Priority ASC");

        debug(this, "sql=" + query.toSql());

        return query.execute();
    }

}
