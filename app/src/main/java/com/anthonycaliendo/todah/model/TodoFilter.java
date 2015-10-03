package com.anthonycaliendo.todah.model;

import android.util.Log;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Retrieves a filtered list of Todos.
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
     * Applies filters and returns all matching todos.
     * @return
     *      the list of filtered todos
     */
    public List<Todo> getTodos() {
        final From query = new Select().from(Todo.class);

        boolean hasDateParam = false;
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
        } else if (settings.isShowLate()) {
            hasDateParam = true;
            whereClause.append("DueDate <= ? AND status != \"");
            whereClause.append(Todo.Status.COMPLETED);
            whereClause.append('"');
        }

        if (!settings.isShowLate()) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            hasDateParam = true;
            whereClause.append("(DueDate > ?)");
        }

        if (hasDateParam) {
            query.where(whereClause.toString(), Calendar.getInstance());
        } else {
            query.where(whereClause.toString());
        }

        debug("sql=" + query.toSql());

        return query.orderBy("Priority ASC").execute();
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
