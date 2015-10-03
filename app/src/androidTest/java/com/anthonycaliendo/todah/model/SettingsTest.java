package com.anthonycaliendo.todah.model;

import android.test.AndroidTestCase;

public class SettingsTest extends AndroidTestCase {
    public void testToggleShowCompleted() {
        final Settings settings = new Settings();

        settings.setShowCompleted(true);
        assertFalse(settings.toggleShowCompleted());
        assertFalse(settings.isShowCompleted());

        assertTrue(settings.toggleShowCompleted());
        assertTrue(settings.isShowCompleted());
    }

    public void testToggleShowPending() {
        final Settings settings = new Settings();

        settings.setShowPending(true);
        assertFalse(settings.toggleShowPending());
        assertFalse(settings.isShowPending());

        assertTrue(settings.toggleShowPending());
        assertTrue(settings.isShowPending());
    }

    public void testToggleShowLate() {
        final Settings settings = new Settings();

        settings.setShowLate(true);
        assertFalse(settings.toggleShowLate());
        assertFalse(settings.isShowLate());

        assertTrue(settings.toggleShowLate());
        assertTrue(settings.isShowLate());
    }
}
