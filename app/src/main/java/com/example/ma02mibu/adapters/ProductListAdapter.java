package com.example.ma02mibu.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.fragments.products.EditProductFragment;
import com.example.ma02mibu.fragments.products.NewProduct;
import com.example.ma02mibu.fragments.products.ProductsListFragment;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> aProducts;
    private FragmentActivity currFragActivity;
    Context context;
    public ProductListAdapter(Context context, ArrayList<Product> products, FragmentActivity fragmentActivity){
        super(context, R.layout.product_card, products);
        this.context = context;
        aProducts = products;
        currFragActivity = fragmentActivity;
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
        TextView oldPrice = convertView.findViewById(R.id.old_price);
        ImageButton rightButton = convertView.findViewById(R.id.right_button);
        ImageButton leftButton = convertView.findViewById(R.id.left_button);
        ImageButton menuButton = convertView.findViewById(R.id.more_button);
        handleRightButtonClick(rightButton, imageView, product);
        handleLeftButtonClick(leftButton, imageView, product);
        handleProductMenuButtonClick(menuButton, product);
        if(product != null){
            int image = product.getImage().get(product.getCurrentImageIndex());
            imageView.setImageResource(image);
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            category.setText(product.getCategory());
            subCategory.setText(product.getSubCategory());
            oldPrice.setText(product.getPrice());
            price.setText(product.getNewPrice());
            oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mRecyclerView = convertView.findViewById(R.id.event_type_tags);
        mRecyclerView.setLayoutManager(layoutManager);
        StringListAdapter listAdapter = new StringListAdapter(context, product.getEventTypes());
        mRecyclerView.setAdapter(listAdapter);
        return convertView;
    }
    private void handleRightButtonClick(ImageButton rightButton, ImageView imageView, Product product){
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setCurrentImageIndex(1);
                int image = product.getImage().get(product.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }

    private void handleLeftButtonClick(ImageButton leftButton, ImageView imageView, Product product){
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setCurrentImageIndex(0);
                int image = product.getImage().get(product.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
        });
    }
    private void handleProductMenuButtonClick(ImageButton menuButton, Product product){
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), menuButton);
                popup.getMenuInflater()
                        .inflate(R.menu.pup_pop_up_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.edit){
                            FragmentTransition.to(EditProductFragment.newInstance(product), currFragActivity,
                                    true, R.id.scroll_products_list, "editProductPage");
                        }
                        return true;
                    }
                });
                popup.show();
            }
    });
    }
}
