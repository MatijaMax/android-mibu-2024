package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.adapters.PackageListAdapter;
import com.example.ma02mibu.databinding.NewPackageBinding;
import com.example.ma02mibu.fragments.services.ChooseServicesListFragment;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class NewPackage extends Fragment {
    NewPackageBinding binding;
    private static final String ARG_PARAM = "param";
    public static ArrayList<Product> products = new ArrayList<Product>();
    public static ArrayList<Service> services = new ArrayList<>();
    public static ArrayList<Product> mChosenProducts = new ArrayList<Product>();
    public static NewPackage newInstance() {
        return new NewPackage();
    }
    public static NewPackage newInstanceFromProducts(ArrayList<Product> chosenProducts) {
        NewPackage fragment = new NewPackage();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, chosenProducts);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = NewPackageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button selectProductsButton = binding.selectProducts;
        Button selectServicesButton = binding.selectServices;
        prepareProductList(products);
        prepareServicesList(services);
        selectProductsButton.setOnClickListener(v -> {
            FragmentTransition.to(ChooseProductsListFragment.newInstance(products), getActivity(),
                    true, R.id.scroll_packages_list, "chooseProductsPage");
        });
        selectServicesButton.setOnClickListener(v -> {
            FragmentTransition.to(ChooseServicesListFragment.newInstance(services), getActivity(),
                    true, R.id.scroll_packages_list, "chooseServicesPage");
        });
        setPackageData();
        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChosenProducts = getArguments().getParcelableArrayList(ARG_PARAM);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void prepareProductList(ArrayList<Product> products){
        products.clear();
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.product1);
        images.add(R.drawable.right_button);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("Svadbe");
        eventTypes.add("rodjendani");
        products.add(new Product(1L, "Proizvod 1", "Opis 1", "kategorija 1", "podkategorija 1", 2000, images, eventTypes, 5));
        products.add(new Product(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", 2000, images, eventTypes, 5));
    }
    private void prepareServicesList(ArrayList<Service> services){
        services.clear();
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.product1);
        images.add(R.drawable.ic_product);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("svadbe");
        eventTypes.add("rodjendani");
        eventTypes.add("vencanje");
        ArrayList<String> persons = new ArrayList<>();
        persons.add("Jovan");
        services.add(new Service(1L, "Usluga 1", "Opis 1", "kategorija 1", "podkategorija 1", "/", 1000, 2, 0, 2, 0, "Novi Sad", "2 meseca pred pocetak", "3 dana pred pocetak", images, eventTypes, persons, true));
        services.add(new Service(2L, "Usluga 2", "Opis 2", "kategorija 2", "podkategorija 2", "specificnost", 1000, 2, 30, 5, 0, "Novi Sad", "1 mesec pred pocetak", "2 dana pred pocetak", images, eventTypes, persons, true));
    }
    private void setPackageData(){
        if(!mChosenProducts.isEmpty()){
            TextView textView = binding.totalPricePackage;
            int totalPrice = 0;
            for (Product p: mChosenProducts){
                totalPrice += 100;
            }
            String s = totalPrice + "din";
            textView.setText(s);
            TextView productsNum = binding.productsNum;
            s = mChosenProducts.size() + "productsChosen";
            productsNum.setText(s);
        }
    }
}
