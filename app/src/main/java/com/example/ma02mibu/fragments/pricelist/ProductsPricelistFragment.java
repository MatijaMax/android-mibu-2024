package com.example.ma02mibu.fragments.pricelist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.ProductListAdapter;
import com.example.ma02mibu.adapters.ProductPricelistAdapter;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.model.AgendaItem;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Event;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ProductsPricelistFragment extends ListFragment {
    private ProductsPricelistFragmentBinding binding;
    private ArrayList<Product> mProducts;
    private boolean isOwner = false;
    private String userId;
    private FirebaseAuth auth;
    private ProductPricelistAdapter adapter;

    public static ProductsPricelistFragment newInstance(){
        ProductsPricelistFragment fragment = new ProductsPricelistFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            userId = user.getUid();
        }
        super.onCreate(savedInstanceState);
        CloudStoreUtil.selectProducts(new CloudStoreUtil.ProductCallback() {
            @Override
            public void onCallback(ArrayList<Product> retrievedProducts) {
                if (retrievedProducts != null) {
                    mProducts = retrievedProducts;
                } else {
                    mProducts = new ArrayList<>();
                }
                CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        isOwner = true;
                        mProducts.removeIf(p -> !p.getOwnerUuid().equals(userId));
                        mProducts.removeIf(p -> p.isPending());
                        adapter = new ProductPricelistAdapter(getActivity(), mProducts, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                        getEmployeesProducts();

                    }
                });
            }
        });
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPricelistFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button pdfBtn = binding.buttonProductsPricelistPdf;
        pdfBtn.setOnClickListener(v -> generateProductsPricelistPDF(getContext(), mProducts));
        return root;
    }

    private void getEmployeesProducts(){
        CloudStoreUtil.getEmployee(userId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                mProducts.removeIf(s -> !s.getOwnerUuid().equals(myItem.getOwnerRefId()));
                mProducts.removeIf(p -> p.isPending());
                adapter = new ProductPricelistAdapter(getActivity(), mProducts, getActivity(), isOwner);
                setListAdapter(adapter);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void generateProductsPricelistPDF(Context context, ArrayList<Product> products) {
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);

        canvas.drawText("Products pricelist", 50, 100, paint);

        paint.setTextSize(24);
        int startX = 50;
        int startY = 200;
        int rowHeight = 50;
        int columnWidth = 200;
        canvas.drawText("Num", startX, startY, paint);
        canvas.drawText("Name", startX + columnWidth, startY, paint);
        canvas.drawText("Price", startX + 2 * columnWidth, startY, paint);
        canvas.drawText("Discount", startX + 3 * columnWidth, startY, paint);
        canvas.drawText("Discounted price", startX + 4 * columnWidth, startY, paint);
        canvas.drawLine(startX, startY + 10, startX + 5 * columnWidth, startY + 10, paint);

        int currentY = startY + rowHeight;
        for (int i = 0; i < products.size(); i++) {
            canvas.drawText(String.valueOf(i+1), startX, currentY, paint);
            canvas.drawText(products.get(i).getName(), startX + columnWidth, currentY, paint);
            canvas.drawText(products.get(i).getPrice() + " din", startX + 2 * columnWidth, currentY, paint);
            canvas.drawText(products.get(i).getDiscount() + "%", startX + 3 * columnWidth, currentY, paint);
            canvas.drawText(products.get(i).getNewPrice(), startX + 4 * columnWidth, currentY, paint);
            currentY += rowHeight;
        }
        document.finishPage(page);
        String fileName = "products_pricelist.pdf";
        download(context, document, fileName);
        document.close();
    }


    private void download(Context context, PdfDocument document, String fileName) {
        String directoryPath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
        File file = new File(directoryPath, fileName);
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Report "+fileName+" downloaded", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Report download failed", Toast.LENGTH_LONG).show();
        }
    }
}