package com.example.ma02mibu.fragments.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.adapters.authentication.NotificationsListAdapter;
import com.example.ma02mibu.databinding.NotificationsFragmentBinding;
import com.example.ma02mibu.model.OurNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class NotificationsListFragment extends ListFragment {

    private NotificationsFragmentBinding binding;
    private String userId;
    private FirebaseAuth auth;
    private NotificationsListAdapter adapter;
    private ArrayList<OurNotification> allNotifications;
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
                adapter = new NotificationsListAdapter(getActivity(), myItems);
                setListAdapter(adapter);
                allNotifications = myItems;
                for(OurNotification notification: allNotifications){
                    if(notification.getStatus().equals("notRead")){
                        unreadNotifications.add(notification);
                    }else{
                        readNotifications.add(notification);
                    }
                }
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
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (checkedId == 1){
                    adapter = new NotificationsListAdapter(getActivity(), allNotifications);
                    setListAdapter(adapter);
                } else if (checkedId == 3) {
                    adapter = new NotificationsListAdapter(getActivity(), readNotifications);
                    setListAdapter(adapter);
                }else {
                    adapter = new NotificationsListAdapter(getActivity(), unreadNotifications);
                    setListAdapter(adapter);
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
