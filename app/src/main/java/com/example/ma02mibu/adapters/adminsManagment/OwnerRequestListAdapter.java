package com.example.ma02mibu.adapters.adminsManagment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.model.OwnerRequest;

import java.util.ArrayList;

public class OwnerRequestListAdapter extends ArrayAdapter<OwnerRequest> {

    private ArrayList<OwnerRequest> requests;
    public OwnerRequestListAdapter(Context context, ArrayList<OwnerRequest> objects) {
        super(context, R.layout.owner_request_card, objects);
        requests = objects;
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Nullable
    @Override
    public OwnerRequest getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OwnerRequest request = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.owner_request_card,
                    parent, false);
        }
        TextView ownerEmail = convertView.findViewById(R.id.ownerEmail);
        TextView ownerName = convertView.findViewById(R.id.ownerName);
        TextView ownerSurname = convertView.findViewById(R.id.ownerSurname);
        TextView companyEmail = convertView.findViewById(R.id.companyEmail);
        TextView companyName = convertView.findViewById(R.id.companyName);

        EditText reason = convertView.findViewById(R.id.ReasonEditText);

        Button rejectButton = convertView.findViewById(R.id.rejectButton);
        Button acceptButton = convertView.findViewById(R.id.AcceptButton);
        rejectButton.setOnClickListener(v -> {
            if(reason.getText().toString().isEmpty()){
                return;
            }
            requests.remove(request);
            this.notifyDataSetChanged();

            CloudStoreUtil.deleteOwnerRequest(request, reason.getText().toString());
        });
        acceptButton.setOnClickListener(v -> {
            requests.remove(request);
            this.notifyDataSetChanged();

            CloudStoreUtil.acceptOwnerRequest(request);
        });

        ownerEmail.setText(request.getOwner().getEmail());
        ownerName.setText(request.getOwner().getName());
        ownerSurname.setText(request.getOwner().getSurname());
        companyEmail.setText(request.getOwner().getMyCompany().getName());
        companyName.setText(request.getOwner().getMyCompany().getName());

        return convertView;
    }
}
