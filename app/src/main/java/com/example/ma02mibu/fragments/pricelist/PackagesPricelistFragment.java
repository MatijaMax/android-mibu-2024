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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.adapters.PackageListAdapter;
import com.example.ma02mibu.adapters.PackagesPricelistAdapter;
import com.example.ma02mibu.adapters.ServicesPricelistAdapter;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Package;
import com.example.ma02mibu.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PackagesPricelistFragment extends ListFragment {
    private ProductsPricelistFragmentBinding binding;
    private ArrayList<Package> mPackages;
    private boolean isOwner = false;
    private String userId;
    private FirebaseAuth auth;
    private PackagesPricelistAdapter adapter;

    public static PackagesPricelistFragment newInstance(){
        PackagesPricelistFragment fragment = new PackagesPricelistFragment();
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
        CloudStoreUtil.selectPackages(new CloudStoreUtil.PackageCallback(){
            @Override
            public void onCallbackPackage(ArrayList<Package> retrievedPackages) {
                if (retrievedPackages != null) {
                    mPackages = retrievedPackages;
                } else {
                    mPackages = new ArrayList<>();
                }
                CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        isOwner = true;
                        mPackages.removeIf(p -> !p.getOwnerUuid().equals(userId));
                        adapter = new PackagesPricelistAdapter(getActivity(), mPackages, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                        getEmployeesPackages();
                        adapter = new PackagesPricelistAdapter(getActivity(), mPackages, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                });

            }
        });
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPricelistFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView header = binding.pricelistHeader;
        String headerText = "Packages pricelist";
        header.setText(headerText);
        Button pdfBtn = binding.buttonProductsPricelistPdf;
        pdfBtn.setOnClickListener(v -> generatePackagesPricelistPDF(getContext(), mPackages));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getEmployeesPackages(){
        CloudStoreUtil.getEmployee(userId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                mPackages.removeIf(s -> !s.getOwnerUuid().equals(myItem.getOwnerRefId()));
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void generatePackagesPricelistPDF(Context context, ArrayList<Package> aPackages) {
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);

        canvas.drawText("Packages pricelist", 50, 100, paint);

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
        for (int i = 0; i < aPackages.size(); i++) {
            canvas.drawText(String.valueOf(i+1), startX, currentY, paint);
            canvas.drawText(aPackages.get(i).getName(), startX + columnWidth, currentY, paint);
            canvas.drawText(aPackages.get(i).getMinPrice() + " - "+aPackages.get(i).getMaxPrice()+" din", startX + 2 * columnWidth, currentY, paint);
            canvas.drawText(aPackages.get(i).getDiscount() + "%", startX + 3 * columnWidth, currentY, paint);
            canvas.drawText(aPackages.get(i).getPrice(),
                    startX + 4 * columnWidth, currentY, paint);
            currentY += rowHeight;
        }
        document.finishPage(page);
        String fileName = "packages_pricelist.pdf";
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
