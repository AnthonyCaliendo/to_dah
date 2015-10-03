package com.anthonycaliendo.todah.widget;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.model.Todo;

import java.util.List;

/**
 * Acts as a ListAdapter which holds To-dos.  Manages the custom rendering for the To-dos.
 */
public class TodoAdapter extends ArrayAdapter<Todo> implements ListAdapter {

    public TodoAdapter(Context context, final List<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final Todo todo = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        final TextView titleView = (TextView) view.findViewById(R.id.item_todo_title);
        titleView.setText(todo.getTitle());

        toggleCompletedIndicator(view, todo);
        toggleLateIndicator(todo, titleView);

        return view;
    }

    /**
     * Toggles the late indication on/off based on the state of the object.
     * @param todo
     *      the object being rendered into the view
     * @param titleView
     *      the title view being rendered
     */
    private void toggleLateIndicator(final Todo todo, final TextView titleView) {
        if (todo.isLate()) {
            titleView.setTextColor(Color.RED);
        } else {
            titleView.setTextColor(Color.BLACK);
        }
    }

    /**
     * Toggles the completed indicator based on the state of the object.
     * @param view
     *      the view being rendered
     * @param todo
     *      the object being rendered into the view
     */
    private void toggleCompletedIndicator(View view, Todo todo) {
        final ImageView completedIndicator = (ImageView) view.findViewById(R.id.item_todo_is_completed);
        if (todo.isCompleted()) {
            completedIndicator.setVisibility(View.VISIBLE);
        } else {
            completedIndicator.setVisibility(View.INVISIBLE);
        }
    }

}
