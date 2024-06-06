package com.example.ma02mibu.fragments.pricelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.EditPackagePriceFragmentBinding;
import com.example.ma02mibu.databinding.EditProductPriceFragmentBinding;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Service;

public class EditPackagePriceFragment extends Fragment {
    private EditPackagePriceFragmentBinding binding;
    private Package mPackage;
    private static final String ARG_PARAM = "param";
    public static EditPackagePriceFragment newInstance(Package aPackage) {
        EditPackagePriceFragment fragment = new EditPackagePriceFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, aPackage);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditPackagePriceFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView productName = binding.ProductName;
        productName.setText(mPackage.getName());
        EditText productDiscount = binding.ProductDiscountEdit;
        productDiscount.setText(String.valueOf(mPackage.getDiscount()));
        Button submitBtn = binding.submitButton;
        submitBtn.setOnClickListener(v -> editPackage());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPackage = getArguments().getParcelable(ARG_PARAM);
        }
    }

    private void editPackage(){
        String discount = binding.ProductDiscountEdit.getText().toString();
        int discountInt = Integer.parseInt(discount);
        mPackage.setDiscount(discountInt);
        CloudStoreUtil.updatePackage(mPackage);
        FragmentTransition.to(PackagesPricelistFragment.newInstance(), getActivity(),
                false, R.id.scroll_packages_pricelist, "falsh");
    }
}
