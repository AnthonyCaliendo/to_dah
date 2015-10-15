package com.anthonycaliendo.todah.activity.binder;

import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Base class for binders.  Provides utility functionality for handling input binding for specific model objects.
 */
abstract class Binder {

    /**
     * Sets the values in the DatePicker based on the values in the Calendar.
     * @param datePicker
     *      the date picker to set the values on
     * @param calendar
     *      the calendar to provide the values to use
     */
    void applyCalendar(final DatePicker datePicker, final Calendar calendar) {
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
    Calendar convertToCalendar(final DatePicker datePicker) {
        final Calendar convertedCalendar = Calendar.getInstance();
        // default to the end of the day
        convertedCalendar.set(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                23,
                59,
                59
        );

        return convertedCalendar;
    }
}
