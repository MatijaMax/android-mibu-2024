package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.adapters.StringListAdapter;
import com.example.ma02mibu.databinding.PackageDetailsBinding;
import com.example.ma02mibu.databinding.ServiceDetailsBinding;
import com.example.ma02mibu.fragments.services.ServiceDetailsFragment;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class PackageDetailsFragment extends Fragment {

    private PackageDetailsBinding binding;
    private static final String ARG_PARAM1 = "param1";

    private Package mPackage;
    public PackageDetailsFragment() {
    }

    public static PackageDetailsFragment newInstance(Package pack) {
        PackageDetailsFragment fragment = new PackageDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, pack);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPackage = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = PackageDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView packageName = binding.packageNameDetail;
        TextView packageDescription = binding.packageDescriptionDetail;
        TextView reservationPackage = binding.reservationServiceDetail;
        TextView cancellationPackage = binding.cancellationServiceDetail;
        TextView packageTotalPrice = binding.totalPriceServiceDetail;
        ImageButton rightButton = binding.rightButtonServiceDetail;
        ImageButton leftButton = binding.leftButtonServiceDetail;
        ImageView imageView = binding.serviceImageDetail;
        handleRightButtonClick(rightButton, imageView, mPackage);
        handleLeftButtonClick(leftButton, imageView, mPackage);
        packageName.setText(mPackage.getName());
        packageDescription.setText(mPackage.getDescription());
        reservationPackage.setText("2 meseca pred dogadjaj");
        cancellationPackage.setText("2 dana pred dogadjaj");
        packageTotalPrice.setText(mPackage.getPrice());
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mRecyclerView = binding.eventTypeTagsPackageDetail;
        mRecyclerView.setLayoutManager(layoutManager);

        StringListAdapter listAdapter = new StringListAdapter(getActivity(), new ArrayList<String>(mPackage.getEventTypes()));
        mRecyclerView.setAdapter(listAdapter);

        return root;
    }
    private void handleRightButtonClick(ImageButton rightButton, ImageView imageView, Package mPackage){
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPackage.setCurrentImageIndex(1);
                int image = mPackage.getImages().get(mPackage.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }

    private void handleLeftButtonClick(ImageButton leftButton, ImageView imageView, Package mPackage){
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPackage.setCurrentImageIndex(0);
                int image = mPackage.getImages().get(mPackage.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }


}
