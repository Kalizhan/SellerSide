package com.example.satushyzerdeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.activities.TovarActivity;
import com.example.satushyzerdeapp.modules.Tovar;

import java.util.ArrayList;
import java.util.List;

public class TovarListAdapter extends RecyclerView.Adapter<TovarListAdapter.viewHolder> implements Filterable{
    List<Tovar> tovarList;
    List<Tovar> tovarListFull;
    Context context;

    public TovarListAdapter(List<Tovar> tovarList, Context context){
        this.tovarList = tovarList;
        this.context = context;
        tovarListFull = new ArrayList<>(tovarList);
    }

    @Override
    public Filter getFilter() {
        return FilterUser;
    }

    private Filter FilterUser = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            List<Tovar> filteredList = new ArrayList<>();
            if (constraint.length() == 0) {
                tovarListFull = tovarList;

            } else {
                for (Tovar row : tovarList) {

                    // name match condition. this might differ depending on your requirement
                    // here we are looking for name or phone number match
                    if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCode().contains(constraint)) {
                        filteredList.add(row);
                    }
                }

                tovarListFull = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = tovarListFull;
            //Log.i("constraint.length()", ""+constraint.length());
            return filterResults;
//            String searchText = constraint.toString().toLowerCase();
//            List<Tovar>templist = new ArrayList<>();
//            if (searchText.length() == 0 || searchText.isEmpty()){
//                templist.addAll(tovarListFull);
//            }else{
//                for (Tovar item:tovarListFull){
//                    if (item.getName().toLowerCase().contains(searchText) || item.getCode().toLowerCase().contains(searchText)){
//                        templist.add(item);
//                    }
//                }
//            }
//            FilterResults filterResults = new FilterResults();
//            filterResults.values = templist;
//            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tovarList = (ArrayList<Tovar>) results.values;
//            tovarList.clear();
//            tovarList.addAll((Collection<? extends Tovar>) results.values);
            notifyDataSetChanged();
        }
    };

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, price, code, quantity;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.imgGood);
            name = itemView.findViewById(R.id.titleOfMeal);
            price = itemView.findViewById(R.id.priceNum);
            code = itemView.findViewById(R.id.idNum);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_tovar, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Tovar tovar = tovarList.get(position);

        Glide
                .with(context)
                .load(tovar.getPhoto())
                .centerCrop()
                .placeholder(R.drawable.box)
                .into(holder.photo);

        holder.name.setText(tovar.getName());
        holder.price.setText(String.valueOf(tovar.getPrice()) + " тг/шт");
        holder.code.setText(tovar.getCode());
        holder.quantity.setText(""+tovar.getQuantity());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage(position);
            }
        });
    }

    private void goToNextPage(int position) {
        Intent intent = new Intent(context, TovarActivity.class);

        intent.putExtra("tovar", tovarList.get(position));

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return tovarList.size();
    }
}
