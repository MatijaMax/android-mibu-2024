package com.example.ma02mibu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Event;

import java.util.ArrayList;

public class EventListInfoAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    public EventListInfoAdapter(Context context, ArrayList<Event> events){
        super(context, R.layout.category_card, events);
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Nullable
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
                    parent, false);
        }
        convertView.findViewById(R.id.checkbox).setVisibility(View.INVISIBLE);
        TextView categoryName = convertView.findViewById(R.id.categoryName);
        TextView categoryDescription = convertView.findViewById(R.id.categoryDescription);
        if(event != null){
            categoryName.setText(event.getName());
            categoryDescription.setText(event.getDescription());
        }

        return convertView;
    }
}
