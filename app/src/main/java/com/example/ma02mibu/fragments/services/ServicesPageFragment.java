package com.example.ma02mibu.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.ServicesPageFragmentBinding;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;

import java.util.ArrayList;

public class ServicesPageFragment extends Fragment {
    public static ArrayList<Service> services = new ArrayList<Service>();
    private ServicesPageFragmentBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ServicesPageFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareServicesList(services);
        FragmentTransition.to(ServicesListFragment.newInstance(services), getActivity(),
                true, R.id.scroll_services_list, "servicesPage");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void prepareServicesList(ArrayList<Service> services){
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.product1);
        images.add(R.drawable.ic_product);
        ArrayList<String> eventTypes = new ArrayList<>();
        eventTypes.add("svadbe");
        eventTypes.add("rodjendani");
        eventTypes.add("vencanje");
        ArrayList<String> persons = new ArrayList<>();
        persons.add("Jovan");
        services.add(new Service(1L, "Usluga 1", "Opis 1", "kategorija 1", "podkategorija 1", "/", 1000, 2, 2, "Novi Sad", "2 meseca pred pocetak", "3 dana pred pocetak", images, eventTypes, persons));
        services.add(new Service(2L, "Usluga 2", "Opis 2", "kategorija 2", "podkategorija 2", "specificnost", 1000, 2, 5, "Novi Sad", "1 mesec pred pocetak", "2 dana pred pocetak", images, eventTypes, persons));
    }
}
