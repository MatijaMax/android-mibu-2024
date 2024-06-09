package com.example.ma02mibu.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.example.ma02mibu.FragmentTransition;
import com.example.ma02mibu.R;
import com.example.ma02mibu.activities.CloudStoreUtil;
import com.example.ma02mibu.fragments.events.InfoMaxFragment;
import com.example.ma02mibu.model.ProductDAO;

import java.util.List;

public class FavItemsAdapter extends RecyclerView.Adapter<FavItemsAdapter.PersonViewHolder> implements InfoMaxFragment.OnFragmentClosedListener  {

    private List<ProductDAO> personList;

    private FragmentActivity activityMax;

    private RelativeLayout favInfoLayout;



    public void onFragmentClosed() {

        if (favInfoLayout != null) {
            favInfoLayout.setVisibility(View.GONE);
        }
    }

    public FavItemsAdapter(List<ProductDAO> personList, FragmentActivity activityMax) {
        this.personList = personList;
        this.activityMax = activityMax;

    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_max_card, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        ProductDAO person = personList.get(position);
        holder.nameTextView.setText(person.getName());
        RelativeLayout favInfoLayout = activityMax.findViewById(R.id.fav_info_layout);


        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    personList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, personList.size());
                    CloudStoreUtil.updateFav(person);
                }
            }
        });

        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    favInfoLayout.setVisibility(View.VISIBLE);
                    InfoMaxFragment fragment = InfoMaxFragment.newInstance(person, person.getOwnerUuid(), true);
                    fragment.setOnFragmentClosedListener(FavItemsAdapter.this);
                    FragmentTransition.to(fragment, activityMax, true, R.id.fav_info_layout, "FavInfoPage");
                    Log.e("FIREBASE MONKEY", "UPSIC");
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return personList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        Button removeButton;

        Button infoButton;



        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            removeButton = itemView.findViewById(R.id.buttonRemoveFav);
            infoButton = itemView.findViewById(R.id.buttonInfoFav);
        }
    }
}

