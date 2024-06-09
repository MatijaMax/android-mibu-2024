package com.example.ma02mibu.adapters.authentication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.OurNotification;

import java.util.ArrayList;

public class NotificationsListAdapter extends ArrayAdapter<OurNotification> {
    private final ArrayList<OurNotification> notifications;
    public NotificationsListAdapter(Context context, ArrayList<OurNotification> notifications){
        super(context, R.layout.notification_item, notifications);
        this.notifications = notifications;
    }
    @Override
    public int getCount() {
        return notifications.size();
    }


    @Nullable
    @Override
    public OurNotification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OurNotification notification = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_item,
                    parent, false);
        }

        TextView title = convertView.findViewById(R.id.titleNot);
        TextView textNot = convertView.findViewById(R.id.textNot);
        if (notification!=null){
            title.setText(notification.getTitle());
            textNot.setText(notification.getText());
        }

        return convertView;
    }
}
