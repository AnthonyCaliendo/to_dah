package com.anthonycaliendo.todah.widget;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthonycaliendo.todah.R;
import com.anthonycaliendo.todah.model.Todo;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    public TodoAdapter(Context context, final List<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        final Todo todo = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        final ImageView completedIndicator = (ImageView) view.findViewById(R.id.item_todo_is_completed);
        if (todo.isCompleted()) {
            completedIndicator.setVisibility(View.VISIBLE);
        } else {
            completedIndicator.setVisibility(View.INVISIBLE);
        }

        final TextView title = (TextView) view.findViewById(R.id.item_todo_title);
        title.setText(todo.getTitle());

        if (todo.isLate()) {
            title.setTextColor(Color.RED);
        } else {
            title.setTextColor(Color.BLACK);
        }

        return view;
    }

}
