package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.databinding.EditPackageBinding;
import com.example.ma02mibu.databinding.EditProductBinding;
import com.example.ma02mibu.fragments.products.EditProductFragment;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.fragments.services.ChooseServicesListFragment;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.PackageEditDto;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.viewmodels.PackageEditViewModel;
import com.example.ma02mibu.viewmodels.PackageViewModel;

import java.util.ArrayList;

public class EditPackageFragment extends Fragment {
    private EditPackageBinding binding;
    private Package mPackage;
    private PackageEditDto packageDto;
    private PackageEditViewModel viewModel;
    private boolean readFromViewModel;
    private static final String ARG_PARAM = "param";
    public static EditPackageFragment newInstance(Package aPackage) {
        EditPackageFragment fragment = new EditPackageFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, aPackage);
        fragment.setArguments(args);
        return fragment;
    }
    public static EditPackageFragment newInstance() {
        EditPackageFragment fragment = new EditPackageFragment();
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditPackageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(PackageEditViewModel.class);
        if(readFromViewModel){
            setPackageData();
            if(viewModel.getProducts().getValue() != null)
                mPackage.setProducts(viewModel.getProducts().getValue());
            if(viewModel.getServices().getValue() != null)
                mPackage.setServices(viewModel.getServices().getValue());
        }
        EditText productName = binding.PackageName;
        productName.setText(mPackage.getName());
        EditText productDescription = binding.editTextPackageDescription;
        productDescription.setText(mPackage.getDescription());
        CheckBox visibleCB = binding.checkBoxODAvailable;
        visibleCB.setChecked(mPackage.isVisible());
        CheckBox availableCB = binding.checkBoxBuyAvailable;
        availableCB.setChecked(mPackage.isAvailableToBuy());
        String productsNum = mPackage.getProducts().size() + " chosen";
        String servicesNum = mPackage.getServices().size() + " chosen";
        binding.productsNum.setText(productsNum);
        binding.servicesNum.setText(servicesNum);
        Button selectProductsButton = binding.selectProducts;
        Button selectServicesButton = binding.selectServices;
        selectProductsButton.setOnClickListener(v -> {
            savePackageData();
            FragmentTransition.to(ChooseProductsListFragment.newInstance(mPackage.getProducts()), getActivity(),
                    false, R.id.scroll_packages_list, "falsh");
        });
        selectServicesButton.setOnClickListener(v -> {
            savePackageData();
            FragmentTransition.to(ChooseServicesListFragment.newInstance(mPackage.getServices()), getActivity(),
                    false, R.id.scroll_packages_list, "falsh");
        });
        Button submitBtn = binding.submitButton;
        submitBtn.setOnClickListener(v -> submitPackageEdit());
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readFromViewModel = true;
        if (getArguments() != null) {
            readFromViewModel = false;
            mPackage = getArguments().getParcelable(ARG_PARAM);
        }
    }

    private void savePackageData(){
        String name = binding.PackageName.getText().toString();
        String description = binding.editTextPackageDescription.getText().toString();
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean availableToBuy = binding.checkBoxBuyAvailable.isChecked();
        packageDto = new PackageEditDto(name, description, availableToBuy, visible, mPackage.getFirestoreId(), "category1");
        viewModel.setPackage(packageDto);
        viewModel.setServices(mPackage.getServices());
        viewModel.setProducts(mPackage.getProducts());
    }

    private void setPackageData(){
        packageDto = viewModel.getPackage().getValue();
        mPackage = new Package(0L, packageDto.getName(), packageDto.getDescription(), "category 1", 0, new ArrayList<Service>(), new ArrayList<Product>());
        mPackage.setAvailableToBuy(packageDto.isAvailableToBuy());
        mPackage.setVisible(packageDto.isVisible());
        mPackage.setFirestoreId(packageDto.getFirestoreId());
        mPackage.setCategory(packageDto.getCategory());
        binding.PackageName.setText(packageDto.getName());
        binding.editTextPackageDescription.setText(packageDto.getDescription());
        binding.checkBoxODAvailable.setChecked(packageDto.isVisible());
        binding.checkBoxBuyAvailable.setChecked(packageDto.isAvailableToBuy());

    }

    private void submitPackageEdit(){
        CloudStoreUtil.updatePackage(mPackage);
        FragmentTransition.to(PackageListFragment.newInstance(), getActivity(),
                false, R.id.scroll_packages_list, "falsh");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
