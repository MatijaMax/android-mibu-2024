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
import com.example.ma02mibu.model.EventTypeDTO;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreAndFilter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreAndFilter extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public  ArrayList<ProductDAO> products = new ArrayList<ProductDAO>();
    private FragmentExploreAndFilterBinding binding;

    private FirebaseAuth auth;

    private String suggestions;
    private String eventCreatorId;

    // TODO: Rename and change types and number of parameters
    public static ExploreAndFilter newInstance(String param1, String param2) {
        ExploreAndFilter fragment = new ExploreAndFilter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
            FragmentTransition.to(ProductsFilterFragment.newInstance(products), getActivity(),
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
                            allProducts.add(myItem);
                        }
                    }





                });
        db.collection("packagesFilterTest")
                .get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ProductDAO myItem = documentSnapshot.toObject(ProductDAO.class);
                        if(myItem!=null){
                            myItem.setTypeDAO(2);
                            allProductsPackages.add(myItem);

                        }
                    }

                    for(ProductDAO pp: allProductsServices){
                        this.products.add(pp);
                    }

                    for(ProductDAO pp: allProducts){
                        this.products.add(pp);
                    }

                    for(ProductDAO pp: allProductsPackages){
                        this.products.add(pp);
                    }
                   // this.products = allProducts;
                   // products.addAll(allProducts);
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
