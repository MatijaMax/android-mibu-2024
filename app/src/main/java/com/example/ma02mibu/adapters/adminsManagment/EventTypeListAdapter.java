package com.example.ma02mibu.adapters.adminsManagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.EventType;

import java.util.ArrayList;

public class EventTypeListAdapter extends ArrayAdapter<EventType> {
    private ArrayList<EventType> aEventType;
    public EventTypeListAdapter(Context context, ArrayList<EventType> eventTypes){
        super(context, R.layout.event_type_card, eventTypes);
        aEventType = eventTypes;
    }

    @Override
    public int getCount() {
        return aEventType.size();
    }

    @Nullable
    @Override
    public EventType getItem(int position) {
        return aEventType.get(position);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_type_card,
                    parent, false);
        }
        TextView eventTypeName = convertView.findViewById(R.id.eventTypeName);
        TextView eventTypeDescription = convertView.findViewById(R.id.eventTypeDescription);
        if(eventType != null){
            eventTypeName.setText(eventType.getName());
            eventTypeDescription.setText(eventType.getDescription());
        }
        return convertView;
    }
}
