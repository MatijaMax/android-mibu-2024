package com.example.ma02mibu.adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ma02mibu.R;
import com.example.ma02mibu.model.AgendaItem;
import com.example.ma02mibu.model.Guest;
import java.util.List;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.PersonViewHolder> {

    private List<AgendaItem> personList;





    public AgendaAdapter(List<AgendaItem> personList) {
        this.personList = personList;

    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        AgendaItem person = personList.get(position);
        holder.nameTextView.setText(person.getName());
        holder.descriptionTextView.setText(String.valueOf(person.getDescription()));
        holder.timespanTextView.setText(String.valueOf(person.getTimespan()));
        holder.locationTextView.setText(String.valueOf(person.getLocation()));

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView descriptionTextView;
        TextView timespanTextView;
        TextView locationTextView;


        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            timespanTextView = itemView.findViewById(R.id.textViewTimespan);
            locationTextView = itemView.findViewById(R.id.textViewLocation);
        }
    }
}
