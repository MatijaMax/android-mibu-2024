package com.example.ma02mibu.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.packages.ChooseProductsListFragment;
import com.example.ma02mibu.fragments.products.EditProductFragment;
import com.example.ma02mibu.model.Product;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> aProducts;
    private FragmentActivity currFragActivity;
    private ChooseProductsListFragment currFragment;
    private boolean isFromPackage;
    Context context;

    public ProductListAdapter(Context context, ArrayList<Product> products, FragmentActivity fragmentActivity, boolean isFromPackage, ChooseProductsListFragment myFragment){
        super(context, R.layout.product_card, products);
        this.context = context;
        this.isFromPackage = isFromPackage;
        aProducts = products;
        currFragActivity = fragmentActivity;
        currFragment = myFragment;
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
        CheckBox checkBox = convertView.findViewById(R.id.choose_product_cb);
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
        if(product != null){
            if(!product.getImage().isEmpty()) {
                int image = product.getImage().get(product.getCurrentImageIndex());
                imageView.setImageResource(image);
            }
            productName.setText(product.getName());
            productDescription.setText(product.getDescription());
            category.setText(product.getCategory());
            subCategory.setText(product.getSubCategory());
            price.setText(product.getNewPrice());
            if(product.getDiscount() != 0) {
                oldPrice.setVisibility(View.VISIBLE);
                oldPrice.setText(String.valueOf(product.getPrice()));
                oldPrice.setPaintFlags(oldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        LinearLayoutManager layoutManager= new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView mRecyclerView = convertView.findViewById(R.id.event_type_tags);
        mRecyclerView.setLayoutManager(layoutManager);
        StringListAdapter listAdapter = new StringListAdapter(context, product.getEventTypes());
        mRecyclerView.setAdapter(listAdapter);

        if(isFromPackage){
            menuButton.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
            checkIfProductChecked(checkBox, product);
            handleProductCheck(checkBox, product);
        }else {
            handleProductMenuButtonClick(menuButton, product);
        }
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
                        else if(item.getItemId() == R.id.delete){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Delete product");
                            builder.setMessage("Are you sure?");
                            builder.setIcon(R.drawable.warning_icon);
                            builder.setNegativeButton("No", (dialog, id) -> dialog.dismiss());
                            builder.setPositiveButton("Yes", (dialog, id) -> {
                                CloudStoreUtil.deleteProduct(product.getFirestoreId());
                                aProducts.remove(product);
                                notifyDataSetChanged();
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
    });
    }
    private void handleProductCheck(CheckBox cb, Product product){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    currFragment.productChosen(product.getFirestoreId());
                } else {
                    currFragment.productUnChosen(product.getFirestoreId());
                }
            }
        });
    }
    private void checkIfProductChecked(CheckBox cb, Product product){
        for(Product p: currFragment.productsChosen){
            if(product.getFirestoreId().equals(p.getFirestoreId())){
                cb.setChecked(true);
            }
        }
    }
}
