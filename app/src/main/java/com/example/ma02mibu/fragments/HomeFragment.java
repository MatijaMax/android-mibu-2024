package com.example.ma02mibu.fragments;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.activities.MainActivity;
import com.example.ma02mibu.databinding.HomeFragmentBinding;
import com.example.ma02mibu.fragments.employees.EmployeeListFragment;
import com.example.ma02mibu.model.OurNotification;
import com.example.ma02mibu.model.Owner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeFragmentBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        loadNotification();
        return root;
    }

    private void loadNotification() {
        CloudStoreUtil.getNotifications(user.getEmail(), new CloudStoreUtil.NotificationCallback() {
            @Override
            public void onSuccess(ArrayList<OurNotification> myItems) {
                System.out.println("Retrieved item: " + myItems);
                for(OurNotification n : myItems){
                    if(n.getStatus().equals("notRead")){
                        sendNotification(n);
                        n.setStatus("read");
                        CloudStoreUtil.updateNotification(n, new CloudStoreUtil.UpdateReadCallback() {
                            @Override
                            public void onSuccess() {
                                System.out.println("Item updated!");
                            }
                            @Override
                            public void onFailure(Exception e) {
                                System.err.println("Error updating item: " + e.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the failure (e.g., show an error message)
                System.err.println("Error fetching document: " + e.getMessage());
            }
        });
    }

    private void sendNotification(OurNotification n){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "Kanal1")
                .setContentTitle(n.getTitle())
                .setContentText(n.getText())
                .setSmallIcon(R.drawable.warning_icon);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        int notificationId = n.getText().hashCode();     //ovo bi trebalo unique da bude
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
