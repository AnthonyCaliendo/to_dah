package com.anthonycaliendo.todah.util;

import android.util.Log;

/**
 * Provides instrumentation support such as logging, tracing, etc.
 */
public class Instrumentation {

    static public boolean isDebugEnabled = true;

    /**
     * Logs the message at DEBUG level.
     * @param message
     *      the message to log
     */
    public static void debug(final Object instrumentedObject, final String message) {
        if (!isDebugEnabled) {
            return;
        }

        Log.d(instrumentedObject.getClass().getSimpleName(), message);
    }

}
