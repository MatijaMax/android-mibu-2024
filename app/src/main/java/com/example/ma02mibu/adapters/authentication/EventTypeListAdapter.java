package com.example.ma02mibu.adapters.authentication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.EventType;

import java.util.ArrayList;

public class EventTypeListAdapter extends ArrayAdapter<EventType> {
    private ArrayList<EventType> aEventTypes;
    public EventTypeListAdapter(Context context, ArrayList<EventType> eventTypes){
        super(context, R.layout.category_card, eventTypes);
        aEventTypes = eventTypes;
    }

    @Override
    public int getCount() {
        return aEventTypes.size();
    }

    @Nullable
    @Override
    public EventType getItem(int position) {
        return aEventTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventType eventType = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
                    parent, false);
        }
        TextView categoryName = convertView.findViewById(R.id.categoryName);
        TextView categoryDescription = convertView.findViewById(R.id.categoryDescription);
        if(eventType != null){
            categoryName.setText(eventType.getName());
            categoryDescription.setText(eventType.getDescription());
        }

        return convertView;
    }

    public void toggleCheckedWithView(int position, View view, ViewGroup parent){
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.category_card,
                    parent, false);
        }

        CheckBox checkBox = view.findViewById(R.id.checkbox);
        checkBox.setChecked(!checkBox.isChecked());
    }
}
