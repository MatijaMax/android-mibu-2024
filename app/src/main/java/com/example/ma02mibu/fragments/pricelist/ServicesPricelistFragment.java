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
import com.example.ma02mibu.adapters.ProductPricelistAdapter;
import com.example.ma02mibu.adapters.ServiceListAdapter;
import com.example.ma02mibu.adapters.ServicesPricelistAdapter;
import com.example.ma02mibu.databinding.ProductsPricelistFragmentBinding;
import com.example.ma02mibu.model.Employee;
import com.example.ma02mibu.model.Owner;
import com.example.ma02mibu.model.Product;
import com.example.ma02mibu.model.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ServicesPricelistFragment extends ListFragment {
    private ProductsPricelistFragmentBinding binding;
    private ArrayList<Service> mServices;
    private boolean isOwner = false;
    private String userId;
    private FirebaseAuth auth;
    private ServicesPricelistAdapter adapter;

    public static ServicesPricelistFragment newInstance(){
        ServicesPricelistFragment fragment = new ServicesPricelistFragment();
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
        CloudStoreUtil.selectServices(new CloudStoreUtil.ServiceCallback() {
            @Override
            public void onCallbackService(ArrayList<Service> retrievedServices) {
                if (retrievedServices != null) {
                    mServices = retrievedServices;
                } else {
                    mServices = new ArrayList<>();
                }
                CloudStoreUtil.getOwner(userId, new CloudStoreUtil.OwnerCallback() {
                    @Override
                    public void onSuccess(Owner myItem) {
                        isOwner = true;
                        mServices.removeIf(s -> !s.getOwnerUuid().equals(userId));
                        mServices.removeIf(s -> s.isPending());
                        adapter = new ServicesPricelistAdapter(getActivity(), mServices, getActivity(), isOwner);
                        setListAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Exception e) {
                        isOwner = false;
                        getEmployeesServices();
                    }
                });

            }
        });
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = ProductsPricelistFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView header = binding.pricelistHeader;
        String headerText = "Services pricelist";
        header.setText(headerText);
        Button pdfBtn = binding.buttonProductsPricelistPdf;
        pdfBtn.setOnClickListener(v -> generateServicesPricelistPDF(getContext(), mServices));
        return root;
    }

    private void getEmployeesServices(){
        CloudStoreUtil.getEmployee(userId, new CloudStoreUtil.EmployeeCallback() {
            @Override
            public void onSuccess(Employee myItem) {
                mServices.removeIf(s -> !s.getOwnerUuid().equals(myItem.getOwnerRefId()));
                mServices.removeIf(s -> s.isPending());
                adapter = new ServicesPricelistAdapter(getActivity(), mServices, getActivity(), isOwner);
                setListAdapter(adapter);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void generateServicesPricelistPDF(Context context, ArrayList<Service> services) {
        android.graphics.pdf.PdfDocument document = new android.graphics.pdf.PdfDocument();
        android.graphics.pdf.PdfDocument.PageInfo pageInfo = new android.graphics.pdf.PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(36);

        canvas.drawText("Services pricelist", 50, 100, paint);

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
        for (int i = 0; i < services.size(); i++) {
            canvas.drawText(String.valueOf(i+1), startX, currentY, paint);
            canvas.drawText(services.get(i).getName(), startX + columnWidth, currentY, paint);
            canvas.drawText(services.get(i).getPriceByHour() + " din", startX + 2 * columnWidth, currentY, paint);
            canvas.drawText(services.get(i).getDiscount() + "%", startX + 3 * columnWidth, currentY, paint);
            canvas.drawText(services.get(i).getDiscountedHourPrice() + " din", startX + 4 * columnWidth, currentY, paint);
            currentY += rowHeight;
        }
        document.finishPage(page);
        String fileName = "services_pricelist.pdf";
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
