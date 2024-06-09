package com.example.ma02mibu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ma02mibu.R;
import com.example.ma02mibu.model.Guest;
import java.util.List;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.PersonViewHolder> {

    private List<Guest> personList;

    private EditClickListener editClickListener;

    public interface EditClickListener {
        void onEditClick(Guest guest);
    }

    public GuestAdapter(List<Guest> personList, EditClickListener editClickListener) {
        this.personList = personList;
        this.editClickListener = editClickListener;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Guest person = personList.get(position);
        holder.nameTextView.setText(person.getName());
        holder.ageTextView.setText(String.valueOf(person.getAge()));
        holder.invitedTextView.setText(person.getInvited() ? "Invited" : "Not Invited");
        holder.acceptedTextView.setText(person.getHasAccepted() ? "Accepted" : "Not Accepted");
        holder.specialTextView.setText(person.getSpecial());

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    personList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, personList.size());
                }
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(person);
                }
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    personList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, personList.size());
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
        TextView ageTextView;
        TextView invitedTextView;
        TextView acceptedTextView;
        TextView specialTextView;
        Button removeButton;

        Button editButton;



        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            ageTextView = itemView.findViewById(R.id.textViewAge);
            invitedTextView = itemView.findViewById(R.id.textViewInvited);
            acceptedTextView = itemView.findViewById(R.id.textViewAccepted);
            specialTextView = itemView.findViewById(R.id.textViewSpecial);
            removeButton = itemView.findViewById(R.id.buttonRemove);
            editButton = itemView.findViewById(R.id.buttonEdit);
        }
    }
}
