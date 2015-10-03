package com.anthonycaliendo.todah.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Represents app settings, such as filtering and other preferences.
 */
@Table(name = "Settings")
public class Settings extends Model {

    /**
     * Whether we should show pending todos.
     */
    @Column
    private boolean showPending;

    /**
     * Whether we should show completed todos.
     */
    @Column
    private boolean showCompleted;

    /**
     * Whether we should show late todos.
     */
    @Column
    private boolean showLate;

    public Settings() {
        showPending = true;
        showLate    = true;
    }

    public boolean isShowPending() {
        return showPending;
    }

    public void setShowPending(boolean showPending) {
        this.showPending = showPending;
    }

    public void setShowCompleted(boolean showCompleted) {
        this.showCompleted = showCompleted;
    }

    public void setShowLate(boolean showLate) {
        this.showLate = showLate;
    }

    /**
     * Toggles the value of showPending, and then returns the new value.
     * @return
     *      the new value of showPending
     */
    public boolean toggleShowPending() {
        showPending = !showPending;
        save();
        return isShowPending();
    }

    public boolean isShowCompleted() {
        return showCompleted;
    }

    /**
     * Toggles the value of showCompleted, and then returns the new value.
     * @return
     *      the new value of showCompleted
     */
    public boolean toggleShowCompleted() {
        showCompleted = !showCompleted;
        save();
        return isShowCompleted();
    }

    public boolean isShowLate() {
        return showLate;
    }

    /**
     * Toggles the value of showLate, and then returns the new value.
     * @return
     *      the new value of showLate
     */
    public boolean toggleShowLate() {
        showLate = !showLate;
        save();
        return isShowLate();
    }
}
