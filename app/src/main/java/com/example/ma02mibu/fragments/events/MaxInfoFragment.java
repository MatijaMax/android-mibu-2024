package com.example.ma02mibu.fragments.events;


import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.model.ProductDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MaxInfoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3= "param3";

    private ProductDAO product;

    private String ownerId;

    private Boolean isFav;


    private FirebaseAuth auth;

    private FirebaseUser currentUser;

    public static MaxInfoFragment newInstance(ProductDAO product, String param2, Boolean param3) {
        MaxInfoFragment fragment = new MaxInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, product);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable(ARG_PARAM1);
            ownerId = getArguments().getString(ARG_PARAM2);
            isFav = getArguments().getBoolean(ARG_PARAM3);
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_max_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textViewProductName = view.findViewById(R.id.textViewProductName);
        TextView textViewProductDescription = view.findViewById(R.id.textViewProductDescription);
        TextView textViewProductCategory = view.findViewById(R.id.textViewProductCategory);
        TextView textViewProductSubCategory = view.findViewById(R.id.textViewProductSubCategory);
        TextView textViewProductPrice = view.findViewById(R.id.textViewProductPrice);
        TextView textViewProductNewPrice = view.findViewById(R.id.textViewProductNewPrice);
        TextView textViewProductEventTypes = view.findViewById(R.id.textViewProductEventTypes);
        TextView textViewAvailableToBuy = view.findViewById(R.id.textViewAvailableToBuy);
        Button buttonFavourite = view.findViewById(R.id.buttonFavourite);
        Button buttonReserve = view.findViewById(R.id.buttonReserve);
        Button buttonBuy = view.findViewById(R.id.buttonBuy);
        Button buttonCompany = view.findViewById(R.id.buttonCompany);
        Button buttonChat = view.findViewById(R.id.buttonChat);

        // Assuming you have a method to load images from the product
        // imageViewProduct.setImageResource(product.getImage().get(0)); // Set the first image for simplicity

        textViewProductName.setText(product.getName());
        textViewProductDescription.setText(product.getDescription());
        textViewProductCategory.setText(product.getCategory());
        textViewProductSubCategory.setText(product.getSubCategory());
        textViewProductPrice.setText(String.valueOf(product.getPrice()));
        textViewProductNewPrice.setText(product.getNewPrice());
        textViewProductEventTypes.setText(String.join(", ", product.getEventTypes()));
        textViewAvailableToBuy.setText(product.isAvailableToBuy() ? "Yes" : "No");

        buttonFavourite.setOnClickListener(v -> {


                product.setEmail(currentUser.getEmail());
                Log.d(TAG, "createProduct:" + product.getName());
                CloudStoreUtil.insertFavNew(product);
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Fav created successfully!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can add actions here if needed
                    }
                });
                alertDialog.show();



        });

        buttonReserve.setOnClickListener(v -> {
            // Handle reserve action
        });

        buttonBuy.setOnClickListener(v -> {
            // Handle buy action
        });

        buttonCompany.setOnClickListener(v -> {

            Log.d("FIREBASE MONKEY", String.valueOf(isFav));
            if(isFav){
                FragmentTransition.to(MaxCompanyFragment.newInstance(product, ownerId, isFav), getActivity(),
                        true, R.id.fav_info_layout, "COMPANY");
            }
            else{
                FragmentTransition.to(MaxCompanyFragment.newInstance(product, ownerId, isFav), getActivity(),
                        true, R.id.scroll_products_list2, "COMPANY");
            }

        });

        buttonChat.setOnClickListener(v -> {


            Log.d("FIREBASE MONKEY", String.valueOf(isFav));
            if(isFav){
                FragmentTransition.to(ChatFragment.newInstance(), getActivity(),
                        true, R.id.fav_info_layout, "CHAT");
            }
            else{
                FragmentTransition.to(ChatFragment.newInstance(), getActivity(),
                        true, R.id.scroll_products_list2, "CHAT");
            }

        });

        buttonReserve.setEnabled(product.isAvailableToBuy());
        buttonBuy.setEnabled(product.isAvailableToBuy());
    }
}


