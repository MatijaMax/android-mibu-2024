package com.example.ma02mibu.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.databinding.ProductsPageFragmentBinding;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.EventTypeDTO;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.databinding.FragmentExploreAndFilterBinding;
import com.example.ma02mibu.model.ProductDAO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ExploreAndFilter extends Fragment {
    private static final String ARG_PARAM1 = "event";
    private Event selectedEvent;

    public  ArrayList<ProductDAO> products = new ArrayList<ProductDAO>();
    private FragmentExploreAndFilterBinding binding;

    private FirebaseAuth auth;

    private String suggestions;
    private String eventCreatorId;

    public static ExploreAndFilter newInstance(Event event) {
        ExploreAndFilter fragment = new ExploreAndFilter();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            selectedEvent = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*@Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore_and_filter, container, false);
    } */

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            eventCreatorId = user.getEmail();

        }
        binding = FragmentExploreAndFilterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = FirebaseAuth.getInstance();
        prepareProductList(products, () -> {
            // Callback when prepareProductList completes
            FragmentTransition.to(ProductsFilterFragment.newInstance(products, selectedEvent), getActivity(),
                    true, R.id.scroll_products_list2, "filterAllPage");
        });
        return root;
    }

    private void prepareProductList(ArrayList<ProductDAO> products, Runnable callback) {
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.product1);
        ArrayList<ProductDAO> allProducts = new ArrayList<>();
        ArrayList<ProductDAO> allProductsServices= new ArrayList<>();
        ArrayList<ProductDAO> allProductsPackages = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("services")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                ProductDAO myItem = documentSnapshot.toObject(ProductDAO.class);
                                if(myItem!=null){
                                    myItem.setTypeDAO(0);
                                    myItem.setDocumentRefId(documentSnapshot.getId());
                                    //myItem.setImage(images);
                                    allProductsServices.add(myItem);
                                }
                            }




                        });
        db.collection("products")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ProductDAO myItem = documentSnapshot.toObject(ProductDAO.class);
                        if(myItem!=null){
                            myItem.setTypeDAO(1);
                            myItem.setDocumentRefId(documentSnapshot.getId());
                            allProducts.add(myItem);
                        }
                    }





                });
        db.collection("packages")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Package myPackage = documentSnapshot.toObject(Package.class);
                        ProductDAO myItem = new ProductDAO();
                        if(myPackage!=null){
                            myItem.setId(myPackage.getId());
                            myItem.setPrice(myPackage.getMinPrice());
                            myItem.setCategory(myPackage.getCategory());
                            myItem.setDescription(myPackage.getDescription());
                            myItem.setDiscount(myPackage.getDiscount());
                            myItem.setName(myPackage.getName());
                            myItem.setAvailableToBuy(myPackage.isAvailableToBuy());
                            myItem.setEventTypes(myPackage.getEventTypes());
                            myItem.setSubCategory("nema");
                            myItem.setVisible(myPackage.isVisible());
                            myItem.setTypeDAO(2);
                            allProductsPackages.add(myItem);
                            myItem.setDocumentRefId(documentSnapshot.getId());
                            allProducts.add(myItem);
                        }
                    }

                    for(ProductDAO pp: allProductsServices){
                        this.products.add(pp);
                    }

                    for(ProductDAO pp: allProducts){
                        this.products.add(pp);
                    }

                    /*for(ProductDAO pp: allProductsPackages){
                        this.products.add(pp);
                    }*/
                   // this.products = allProducts;
                   // products.addAll(allProducts);
                    //this.products = allProducts;
                  //  products.addAll(allProducts);
                    for (ProductDAO product : this.products) {
                        Log.d("ProductX", product.toString());
                        product.setImage(images);
                    }
                    ArrayList<String> eventTypes = new ArrayList<>();
                    eventTypes.add("text");
                   //products.add(new ProductDAO(1L, "Proizvod 1", "Opis 1", "kategorija 1", "podkategorija 1", 2000, images, eventTypes , 5, 1));
                   // products.add(new ProductDAO(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", 2000, images, eventTypes, 5, 1));
                    callback.run();
                });


        //ArrayList<String> eventTypes = new ArrayList<>();
        //eventTypes.add("text");
        //products.add(new ProductDAO(1L, "Proizvod 1", "Opis 1", "kategorija 1", "podkategorija 1", 2000, images, eventTypes , 5, 1));
        //products.add(new ProductDAO(1L, "Proizvod 2", "Opis 2", "kategorija 2", "podkategorija 2", 2000, images, eventTypes, 5, 1));
    }





}
