package com.example.ma02mibu.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.R;
import com.example.ma02mibu.fragments.packages.ChooseProductsListFragment;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class ProductPricelistAdapter extends ArrayAdapter<Product> {
    Context context;
    private ArrayList<Product> aProducts;
    public ProductPricelistAdapter(Context context, ArrayList<Product> products){
        super(context, R.layout.product_pricelist_item, products);
        this.context = context;
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
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productSerialNumber = convertView.findViewById(R.id.serialNumber);
        TextView oldPrice = convertView.findViewById(R.id.price);
        TextView newPrice = convertView.findViewById(R.id.priceNew);
        TextView discount = convertView.findViewById(R.id.discount);
        if(product != null){
            productName.setText(product.getName());
            productSerialNumber.setText(String.valueOf(position));
            oldPrice.setText(String.valueOf(product.getPrice()));
            discount.setText(String.valueOf(product.getDiscount()));
            newPrice.setText(product.getNewPrice());
        }
        return convertView;
    }

}
