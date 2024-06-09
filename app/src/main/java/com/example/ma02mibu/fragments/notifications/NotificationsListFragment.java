package com.example.ma02mibu.fragments.notifications;

import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.authentication.NotificationsListAdapter;
import com.example.ma02mibu.databinding.NotificationsFragmentBinding;
import com.example.ma02mibu.model.OurNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class NotificationsListFragment extends ListFragment implements SensorEventListener {
//dobra komb je bilo 250-70 sa 600 ms
    private static final int SHAKE_THRESHOLD = 155;
    private NotificationsFragmentBinding binding;
    private String userId;
    private FirebaseAuth auth;
    private NotificationsListAdapter adapterAll;
    private NotificationsListAdapter adapterUnread;
    private NotificationsListAdapter adapterRead;
    private SensorManager sensorManager;
    private int currentBoxChecked = 1;
    private ArrayList<OurNotification> allNotifications;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;
    private ArrayList<OurNotification> readNotifications = new ArrayList<>();
    private ArrayList<OurNotification> unreadNotifications = new ArrayList<>();

    public static NotificationsListFragment newInstance(){
        NotificationsListFragment fragment = new NotificationsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        CloudStoreUtil.getNotifications(user.getEmail(), new CloudStoreUtil.NotificationCallback() {
            @Override
            public void onSuccess(ArrayList<OurNotification> myItems) {
                adapterAll = new NotificationsListAdapter(getActivity(), myItems);
                setListAdapter(adapterAll);
                allNotifications = myItems;
                for(OurNotification notification: allNotifications){
                    if(notification.getStatus().equals("notRead")){
                        unreadNotifications.add(notification);
                    }else{
                        readNotifications.add(notification);
                    }
                }
                adapterAll = new NotificationsListAdapter(getActivity(), myItems);
                adapterRead = new NotificationsListAdapter(getActivity(), readNotifications);
                adapterUnread = new NotificationsListAdapter(getActivity(), unreadNotifications);
            }
            @Override
            public void onFailure(Exception e) {
                System.err.println("Error fetching document: " + e.getMessage());
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = NotificationsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RadioGroup rGroup = binding.notRadioGroup;

        binding.checkBoxAll.setChecked(true);
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (binding.checkBoxAll.isChecked()){
                    setListAdapter(adapterAll);
                    currentBoxChecked = 1;
                } else if (binding.checkBoxRead.isChecked()) {
                    setListAdapter(adapterRead);
                    currentBoxChecked = 3;
                }else {
                    setListAdapter(adapterUnread);
                    currentBoxChecked = 2;
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 1000) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = event.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                Log.i("Shake speed", "Speed: " +speed);
                if(speed > SHAKE_THRESHOLD){
                    if(currentBoxChecked < 3)
                        currentBoxChecked++;
                    else
                        currentBoxChecked = 1;
                    if(currentBoxChecked == 1){
                        binding.checkBoxAll.setChecked(true);
                        binding.checkBoxNotRead.setChecked(false);
                        binding.checkBoxRead.setChecked(false);
                        setListAdapter(adapterAll);
                    }
                    else if(currentBoxChecked == 2){
                        binding.checkBoxAll.setChecked(false);
                        binding.checkBoxNotRead.setChecked(true);
                        binding.checkBoxRead.setChecked(false);
                        setListAdapter(adapterUnread);
                    }
                    else{
                        binding.checkBoxAll.setChecked(false);
                        binding.checkBoxNotRead.setChecked(false);
                        binding.checkBoxRead.setChecked(true);
                        setListAdapter(adapterRead);
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            Log.i("REZ_ACCELEROMETER", String.valueOf(accuracy));
        }
    }
}
