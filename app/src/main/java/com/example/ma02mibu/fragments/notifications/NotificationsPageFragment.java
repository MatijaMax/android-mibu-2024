package com.example.ma02mibu.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.NotificationPageFragmentBinding;
import com.example.ma02mibu.databinding.ProductsPageFragmentBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class NotificationsPageFragment extends Fragment {
    private NotificationPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = NotificationPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        FragmentTransition.to(NotificationsListFragment.newInstance(), getActivity(),
                false, R.id.notifications_list, "falsh");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
