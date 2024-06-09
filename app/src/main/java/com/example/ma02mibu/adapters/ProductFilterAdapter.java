package com.example.ma02mibu.adapters;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.events.InfoMaxFragment;
import com.example.ma02mibu.model.ProductDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ProductFilterAdapter extends ArrayAdapter<ProductDAO> {
    private ArrayList<ProductDAO> aProducts;

    private FirebaseAuth auth;

    private Context contextMax;

    private FragmentActivity activityMax;

    private FirebaseUser currentUser;
    public ProductFilterAdapter(Context context, FragmentActivity fragmentActivity, ArrayList<ProductDAO> products) {
        super(context, R.layout.product_card, products);
        contextMax = context;
        activityMax = fragmentActivity; // Assigning the activity context
        aProducts = products;
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }
    @Override
    public int getCount() {
        return aProducts.size();
    }

    @Nullable
    @Override
    public ProductDAO getItem(int position) {
        return aProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProductDAO product = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card_fav,
                    parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView category = convertView.findViewById(R.id.product_category);
        TextView subCategory = convertView.findViewById(R.id.product_subcategory);
        TextView price = convertView.findViewById(R.id.product_price);

        Button favButton = convertView.findViewById(R.id.buttonFavMax);
        handleFavButtonClick(favButton, product);
        Button infoButton = convertView.findViewById(R.id.buttonInfoMax);
        handleInfoButtonClick(infoButton, product);


        if(product != null){
            int image = product.getImage().get(0);
            imageView.setImageResource(image);
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            category.setText(product.getCategory());
            subCategory.setText(product.getSubCategory());
            price.setText(String.valueOf(product.getPrice()));
        }

        return convertView;
    }

    private void handleFavButtonClick(Button detailsButton, ProductDAO product){
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               product.setEmail(currentUser.getEmail());
                createFav(product);

            }
        });
    }

    private void handleInfoButtonClick(Button detailsButton, ProductDAO product){


        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(InfoMaxFragment.newInstance(product, product.getOwnerUuid(), false), activityMax,
                        true, R.id.scroll_products_list2, "MaxEAF");
            }
        });
    }

    private void createFav(ProductDAO item) {
        Log.d(TAG, "createProduct:" + item.getName());
        CloudStoreUtil.insertFavNew(item);
        AlertDialog alertDialog = new AlertDialog.Builder(contextMax).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Fav created successfully!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // You can add actions here if needed
            }
        });
        alertDialog.show();
    }
}

