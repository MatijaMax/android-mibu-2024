package com.example.ma02mibu.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;
public class EventListAdapter extends ArrayAdapter<Event>{
    private final ArrayList<Event> aEvents;
    private FragmentActivity currFragActivity;
    public EventListAdapter(Context context, ArrayList<Event> events, FragmentActivity fragmentActivity){
        super(context, R.layout.event_card, events);
        aEvents = events;
        currFragActivity = fragmentActivity;
    }
    @Override
    public int getCount() {
        return aEvents.size();
    }


    @Nullable
    @Override
    public Event getItem(int position) {
        return aEvents.get(position);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_card,
                    parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        if (event!=null){
            name.setText(event.getName());
        }

        return convertView;
    }

}
