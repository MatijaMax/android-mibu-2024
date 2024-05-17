package com.example.ma02mibu.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.ProductDAO;

import java.util.ArrayList;

public class ProductFilterAdapter extends ArrayAdapter<ProductDAO> {
    private ArrayList<ProductDAO> aProducts;
    public ProductFilterAdapter(Context context, ArrayList<ProductDAO> products){
        super(context, R.layout.product_card, products);
        aProducts = products;
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
        if(product != null){
            int image = product.getImage().get(0);
            imageView.setImageResource(image);
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            category.setText(product.getCategory());
            subCategory.setText(product.getSubCategory());
            price.setText(String.valueOf(product.getPrice()));
        }
        Button buyButton = convertView.findViewById(R.id.buyProduct);
        handleBuyProductButton(buyButton, imageView, product);

        return convertView;
    }

    private void handleBuyProductButton(Button buyButton, ImageView imageView, ProductDAO product) {
        buyButton.setOnClickListener(v -> {
            if (!product.isAvailableToBuy()) {
                Toast.makeText(imageView.getContext(), "Product is not available to buy!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(imageView.getContext(), "Product is added to the budget!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

