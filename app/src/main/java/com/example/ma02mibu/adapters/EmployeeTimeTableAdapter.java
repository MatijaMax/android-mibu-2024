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

import java.util.ArrayList;

public class EmployeeTimeTableAdapter extends ArrayAdapter {
    private ArrayList<String> times;
    public EmployeeTimeTableAdapter(Context context, ArrayList<String> times){
        super(context, R.layout.category_managment_card, times);
        this.times = times;
    }
    @Override
    public int getCount() {
        return times.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return times.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String time = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_managment_card,
                    parent, false);
        }
        TextView name = convertView.findViewById(R.id.categoryName);
        TextView description = convertView.findViewById(R.id.categoryDescription);
        description.setText("");
        name.setText(time);

        return convertView;
    }
}
