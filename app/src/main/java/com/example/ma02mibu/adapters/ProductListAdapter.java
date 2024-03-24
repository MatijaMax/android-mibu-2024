package com.example.ma02mibu.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> aProducts;
    public ProductListAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_card, products);
        aProducts = products;
    }
    @Override
    public int getCount() {
        return aProducts.size();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return aProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card,
                    parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.product_image);
        TextView productName = convertView.findViewById(R.id.product_name);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView category = convertView.findViewById(R.id.product_category);
        TextView subCategory = convertView.findViewById(R.id.product_subcategory);
        TextView price = convertView.findViewById(R.id.product_price);
        if(product != null){
            imageView.setImageResource(product.getImage());
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            category.setText(product.getCategory());
            subCategory.setText(product.getSubCategory());
            price.setText(product.getPrice());
        }

        return convertView;
    }
}
