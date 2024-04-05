package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.databinding.EditPackageBinding;
import com.example.ma02mibu.databinding.EditProductBinding;
import com.example.ma02mibu.fragments.products.EditProductFragment;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;

public class EditPackageFragment extends Fragment {
    private EditPackageBinding binding;
    private Package mPackage;
    private static final String ARG_PARAM = "param";
    public static EditPackageFragment newInstance(Package aPackage) {
        EditPackageFragment fragment = new EditPackageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, aPackage);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditPackageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText productName = binding.PackageName;
        productName.setText(mPackage.getName());
        EditText productDescription = binding.editTextPackageDescription;
        productDescription.setText(mPackage.getDescription());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPackage = getArguments().getParcelable(ARG_PARAM);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
