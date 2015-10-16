package com.anthonycaliendo.todah.model;

import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.activeandroid.serializer.CalendarSerializer;

import java.util.Calendar;
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
        if (!settings.isShowPending() && !settings.isShowCompleted() && !settings.isShowLate()) {
            debug(this, "showPending=false showCompleted=false showLate=false sql=SKIPPED");
            return Collections.emptyList();
        }

        final From query = new Select().from(Todo.class);

        final StringBuilder whereClause = new StringBuilder();
        if (settings.isShowPending()) {
            whereClause.append("(status = \"");
            whereClause.append(Todo.Status.PENDING);
            whereClause.append("\" AND (dueDate > ? OR dueDate IS NULL))");
        }

        if (settings.isShowCompleted()) {
            if (whereClause.length() > 0) {
                whereClause.append(" OR ");
            }
            whereClause.append("(status = \"");
            whereClause.append(Todo.Status.COMPLETED);
            whereClause.append("\")");
        }

        if (settings.isShowLate()) {
            if (whereClause.length() > 0) {
                whereClause.append(" OR ");
            }
            whereClause.append("(status != \"");
            whereClause.append(Todo.Status.COMPLETED);
            whereClause.append("\" AND dueDate <= ?)");
        }

        final Long currentTime = new CalendarSerializer().serialize(Calendar.getInstance());
        if (settings.isShowLate() && settings.isShowPending()) {
            query.where(whereClause.toString(), currentTime, currentTime);
        } else
        if (settings.isShowLate() || settings.isShowPending()) {
            query.where(whereClause.toString(), currentTime);
        } else {
            query.where(whereClause.toString());
        }

        query.orderBy("priority ASC");

        debug(this, "showPending=" + settings.isShowPending() + " showCompleted=" + settings.isShowCompleted() + " showLate=" + settings.isShowLate() + " sql=" + query.toSql());

        return query.execute();
    }

}
