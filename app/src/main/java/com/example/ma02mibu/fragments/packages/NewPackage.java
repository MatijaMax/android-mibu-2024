package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.PackageListAdapter;
import com.example.ma02mibu.databinding.NewPackageBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.fragments.services.ChooseServicesListFragment;
import com.example.ma02mibu.model.Category;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.PackageCreateDto;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.example.ma02mibu.model.Subcategory;
import com.example.ma02mibu.viewmodels.CategorySharedViewModel;
import com.example.ma02mibu.viewmodels.PackageViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class NewPackage extends Fragment {
    NewPackageBinding binding;
    private static final String ARG_PARAM = "param";
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Service> services = new ArrayList<>();
    private ArrayList<String> categories;
    public ArrayList<Product> mChosenProducts = new ArrayList<>();
    public ArrayList<Service> mChosenServices = new ArrayList<>();
    public PackageCreateDto packageCreateDto = new PackageCreateDto();
    private CategorySharedViewModel categorySharedViewModel;
    ArrayList<Category> categoriesDB = new ArrayList<>();
    private PackageViewModel viewModel;
    private FirebaseAuth auth;
    private String ownerId;
    public static NewPackage newInstance() {
        return new NewPackage();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        ownerId = user.getUid();
        binding = NewPackageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button selectProductsButton = binding.selectProducts;
        Button selectServicesButton = binding.selectServices;
        selectProductsButton.setOnClickListener(v -> {
            savePackageData();
            FragmentTransition.to(ChooseProductsListFragment.newInstance(), getActivity(),
                    true, R.id.scroll_packages_list, "chooseProductsPage");
        });
        selectServicesButton.setOnClickListener(v -> {
            savePackageData();
            FragmentTransition.to(ChooseServicesListFragment.newInstance(), getActivity(),
                    true, R.id.scroll_packages_list, "chooseServicesPage");
        });
        setCategoriesSpinnerAdapter();
        categorySharedViewModel = new ViewModelProvider(requireActivity()).get(CategorySharedViewModel.class);
        viewModel = new ViewModelProvider(requireActivity()).get(PackageViewModel.class);
        if(viewModel.getProducts().getValue() != null)
            mChosenProducts = viewModel.getProducts().getValue();
        if(viewModel.getServices().getValue() != null)
            mChosenServices = viewModel.getServices().getValue();
        if(viewModel.getPackage().getValue() != null) {
            packageCreateDto = viewModel.getPackage().getValue();
            setPackageData();
        }
        setPackageCollections();
        Button submitBtn = binding.submitButton;
        submitBtn.setOnClickListener(v -> savePackageDB());
        //setCategoryChangeListener();
        return root;
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
    private void setPackageCollections(){
        int totalPrice = 0;
        if(!mChosenProducts.isEmpty()){
            for (Product p: mChosenProducts){
                totalPrice += p.getPrice();
            }
            TextView productsNum = binding.productsNum;
            String s = mChosenProducts.size() + " products chosen";
            productsNum.setText(s);
        }
        if(!mChosenServices.isEmpty()){
            for (Service s: mChosenServices){
                totalPrice += s.getPriceByHour();
            }
            TextView servicesNum = binding.servicesNum;
            String s = mChosenServices.size() + " services chosen";
            servicesNum.setText(s);
        }
        TextView textView = binding.totalPricePackage;
        String s = totalPrice + "din";
        textView.setText(s);
    }
    private void savePackageData(){
        String name = binding.PackageName.getText().toString();
        String description = binding.packageDescription.getText().toString();
        boolean visible = binding.checkBoxODAvailable.isChecked();
        boolean availableToBuy = binding.checkBoxBuyAvailable.isChecked();
        int categoryPosition = binding.PackageCategory.getSelectedItemPosition();
        packageCreateDto = new PackageCreateDto(name, description, availableToBuy, visible, categoryPosition);
        viewModel.setPackage(packageCreateDto);
        categorySharedViewModel.setCategory(categories.get(categoryPosition));
    }

    private void setPackageData() {
        binding.PackageName.setText(packageCreateDto.getName());
        binding.packageDescription.setText(packageCreateDto.getDescription());
        binding.checkBoxODAvailable.setChecked(packageCreateDto.isVisible());
        binding.checkBoxBuyAvailable.setChecked(packageCreateDto.isAvailableToBuy());
        int x = packageCreateDto.getCategoryPosition();
        binding.PackageCategory.setSelection(x);
    }

    private void savePackageDB(){
        binding.validation.setVisibility(View.GONE);
        savePackageData();
        if(packageCreateDto.getName().equals("") || packageCreateDto.getDescription().equals("")){
            binding.validation.setText("Fill all the fields, please");
            binding.validation.setVisibility(View.VISIBLE);
            return;
        }
        if(mChosenServices.isEmpty() && mChosenProducts.isEmpty()){
            binding.validation.setText("Choose at least one product or service, please");
            binding.validation.setVisibility(View.VISIBLE);
            return;
        }
        String category = binding.PackageCategory.getSelectedItem().toString();
        Package newPackage = new Package(0L, packageCreateDto.getName(), packageCreateDto.getDescription(), category,
                0, mChosenServices, mChosenProducts);
        newPackage.setOwnerUuid(ownerId);
        newPackage.setVisible(binding.checkBoxODAvailable.isChecked());
        newPackage.setAvailableToBuy(binding.checkBoxBuyAvailable.isChecked());
        CloudStoreUtil.insertPackage(newPackage);
        FragmentTransition.to(PackageListFragment.newInstance(), getActivity(),
                false, R.id.scroll_packages_list, "falsh");
    }

    private void setCategoriesSpinnerAdapter(){
        CloudStoreUtil.selectCategories(result -> {
            categoriesDB = result;
            categories = new ArrayList<>();
            for(Category c: categoriesDB)
                categories.add(c.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.PackageCategory.setAdapter(adapter);
            if(categorySharedViewModel.getCategory().getValue() == null)
                categorySharedViewModel.setCategory(categories.get(0));
            if(viewModel.getPackage().getValue() != null) {
                binding.PackageCategory.setSelection(viewModel.getPackage().getValue().getCategoryPosition());
            }
        });
    }

    private void setCategoryChangeListener(){
        binding.PackageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySharedViewModel.setCategory(categories.get(position));
                mChosenProducts = new ArrayList<>();
                mChosenServices = new ArrayList<>();
                viewModel.setProducts(mChosenProducts);
                viewModel.setServices(mChosenServices);
                String s = "0 services chosen";
                binding.servicesNum.setText(s);
                s = "0 products chosen";
                binding.productsNum.setText(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
