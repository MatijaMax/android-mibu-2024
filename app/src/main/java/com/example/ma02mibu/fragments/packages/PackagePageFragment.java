package com.example.ma02mibu.fragments.packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.PackagesPageFragmentBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class PackagePageFragment extends Fragment {
    public static ArrayList<Package> packages = new ArrayList<Package>();
    private PackagesPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PackagesPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        preparePackagesList(packages);
        FragmentTransition.to(PackageListFragment.newInstance(packages), getActivity(),
                true, R.id.scroll_packages_list, "packagesPage");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void preparePackagesList(ArrayList<Package> packages){
        packages.clear();
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.product1);
        images.add(R.drawable.right_button);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("svadbe");
        eventTypes.add("rodjendani");
        products.add(new Product(1L, "Proizvod 1", "Opis 1", "kategorija 1", "podkategorija 1", 2000, images, eventTypes, 5));
        products.add(new Product(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", 2000, images, eventTypes, 5));
        ArrayList<Integer> imagesS = new ArrayList<>();
        images.add(R.drawable.product1);
        images.add(R.drawable.ic_product);
        ArrayList<String> eventTypesS = new ArrayList<>();
        eventTypes.add("svadbe");
        eventTypes.add("rodjendani");
        eventTypes.add("vencanje");
        ArrayList<String> persons = new ArrayList<>();
        ArrayList<Service> services = new ArrayList<Service>();
        persons.add("Jovan");
        services.add(new Service(1L, "Usluga 1", "Opis 1", "kategorija 1", "podkategorija 1", "/", 1000, 2, 0, 2, 0, "Novi Sad", "2 meseca pred pocetak", "3 dana pred pocetak", imagesS, eventTypesS, persons, true));
        services.add(new Service(2L, "Usluga 2", "Opis 2", "kategorija 2", "podkategorija 2", "specificnost", 1000, 2, 30, 5, 0, "Novi Sad", "1 mesec pred pocetak", "2 dana pred pocetak", imagesS, eventTypesS, persons, true));
        packages.add(new Package(1L, "Paket 1", "Opis 1", "Kategorija 1", 0, services, products));
        services.clear();
        services.add(new Service(2L, "Usluga 2", "Opis 2", "kategorija 2", "podkategorija 2", "specificnost", 1000, 2, 30, 5, 0, "Novi Sad", "1 mesec pred pocetak", "2 dana pred pocetak", imagesS, eventTypesS, persons, true));
        products.clear();
        products.add(new Product(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", 2000, images, eventTypes, 5));
        packages.add(new Package(1L, "Paket 2", "Opis 2", "Kategorija 2", 0, services, products));
    }
}
