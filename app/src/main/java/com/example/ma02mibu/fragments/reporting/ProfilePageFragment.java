package com.example.ma02mibu.fragments.reporting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.ProfilePageFragmentBinding;
import com.example.ma02mibu.model.EventOrganizer;
import com.example.ma02mibu.model.EventType;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.ProfilePageData;

public class ProfilePageFragment extends Fragment {
    private ProfilePageFragmentBinding binding;
    private ProfilePageData profileData;
    private Owner owner;
    private EventOrganizer organizer;
    private static final String ARG_PARAM = "param";
    public static ProfilePageFragment newInstance(ProfilePageData data) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, data);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProfilePageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView firstName = binding.firstNameTextView;
        TextView lastName = binding.lastNameTextView;
        TextView address = binding.addressText;
        TextView phoneNumber = binding.phoneNumberText;
        TextView emailText = binding.emailText;
        TextView header = binding.header;
        if(profileData.getRole() == 0){
            CloudStoreUtil.getOwner(profileData.getUserId(), new CloudStoreUtil.OwnerCallback() {
                @Override
                public void onSuccess(Owner myItem) {
                    owner = myItem;
                    firstName.setText(owner.getName());
                    lastName.setText(owner.getSurname());
                    address.setText(owner.getAddressOfResidence());
                    phoneNumber.setText(owner.getPhoneNumber());
                    emailText.setText(owner.getEmail());
                    header.setText("Owner");
                }
                @Override
                public void onFailure(Exception e) {
                }
            });
        }else{
            CloudStoreUtil.getEventOrganizer(profileData.getUserId(), new CloudStoreUtil.EventOrganizerCallback() {
                @Override
                public void onSuccess(EventOrganizer myItem) {
                    organizer = myItem;
                    firstName.setText(organizer.getName());
                    lastName.setText(organizer.getSurname());
                    address.setText(organizer.getAddressOfResidence());
                    phoneNumber.setText(organizer.getPhoneNumber());
                    emailText.setText(organizer.getEmail());
                    header.setText("Organizer");
                }
                @Override
                public void onFailure(Exception e) {

                }
            });
        }
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profileData = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
