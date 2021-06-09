package com.example.satushyzerdeapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.modules.NewZakaz;
import com.example.satushyzerdeapp.modules.Tovar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ZakazListAdapter extends RecyclerView.Adapter<ZakazListAdapter.viewHolder> {
    Context context;
    ArrayList<NewZakaz> newZakazList;
    String[] codes, quantities, tovarName;

    public ZakazListAdapter(Context context, ArrayList<NewZakaz> newZakazList) {
        this.context = context;
        this.newZakazList = newZakazList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_zakaz_recycler, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        int pos = position + 1;
        NewZakaz newZakaz = newZakazList.get(position);

        holder.zakazOrder.setText("Тапсырыс " + pos);
        holder.enteredDate.setText(newZakaz.getDate());
        holder.zakazFullName.setText(newZakaz.getFullName());
        holder.zakazEmail.setText(newZakaz.getEmail());
        holder.zakazPhone.setText(newZakaz.getPhoneNum());
        holder.address.setText(newZakaz.getAddress());
        holder.zakazPrice.setText("" + newZakaz.getTovarPrice() + " тг");
        //Log.i("situation", newZakaz.getTovarSituation());
        if (newZakaz.getTovarSituation().equals("new order")) {
            //holder.zakazSituation.setText("Тапсырыс қабылдануда...");
            holder.zakazOrder.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else if (newZakaz.getTovarSituation().equals("order accepted")) {
            //holder.zakazSituation.setText("Тапсырыс қабылданды. Күтіңіз!");
            holder.zakazOrder.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            //holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.yellow));
//            Glide
//                    .with(context)
//                    .load(R.drawable.waiting)
//                    .centerCrop()
//                    .placeholder(R.drawable.timer)
//                    .into(holder.zakazSitImage);
        } else {
            //holder.zakazSituation.setText("Тапсырыс аяқталды!");
            holder.zakazOrder.setTextColor(context.getResources().getColor(R.color.green));
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.green));
            //holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.green));
//            Glide
//                    .with(context)
//                    .load(R.drawable.checked)
//                    .centerCrop()
//                    .placeholder(R.drawable.timer)
//                    .into(holder.zakazSitImage);
        }
        codes = newZakaz.getTovarCode().split(",");
        quantities = newZakaz.getTovarQuantity().split(",");
        tovarName = newZakaz.getTovarName().split(",");

//        for (i = 0; i < codes.length; i++) {
//            mDatabase.child(codes[i]).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        Tovar tovar = snapshot.getValue(Tovar.class);
//                        basicInfo = basicInfo + tovar.getName() + " - " + quantities[i-1] + ", ";
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }
        String basicInfo = "";
        for (int i = 0; i < tovarName.length; i++) {
            basicInfo = basicInfo + tovarName[i] + " - " + quantities[i] + " штук, ";
        }

        holder.tovarAttary.setText("Тапсырыс: " + basicInfo);
        holder.tovarCode.setText("Тауар кодтары: " + newZakaz.getTovarCode());

        if (!newZakaz.getComment().isEmpty()) {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(newZakaz.getComment());
        } else {
            holder.comment.setVisibility(View.GONE);
        }

        String user = holder.zakazEmail.getText().toString().replace(".", "-");
        Log.i("user", "Email: " + user);

        holder.btnAcceptZakaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0; i<codes.length; i++){
                    mDatabase.child("TovarInfo").child(codes[i]).orderByChild("satyldyShtuk").equalTo(quantities[i]);
                    Toast.makeText(context, "Өзгертілді!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnRemoveZakaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "btn Otmena " + holder.zakazOrder.getText().toString(), Toast.LENGTH_SHORT).show();
                mDatabase.child("Zakazdar").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                NewZakaz newZakaz2 = snap.getValue(NewZakaz.class);
                                if (holder.enteredDate.getText().toString().equals(newZakaz2.getDate())) {
                                    snap.getRef().removeValue();
                                    Toast.makeText(context, holder.zakazOrder.getText() + " өшірілді!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return newZakazList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView zakazOrder, enteredDate, address, zakazPrice, comment, zakazFullName, zakazEmail, zakazPhone, tovarAttary, tovarCode;
        View zakazViewColor;
        Button btnAcceptZakaz, btnRemoveZakaz;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            zakazFullName = itemView.findViewById(R.id.tvzakazFullName);
            zakazEmail = itemView.findViewById(R.id.tvzakazEmail);
            zakazPhone = itemView.findViewById(R.id.tvzakazPhoneNumber);
            tovarAttary = itemView.findViewById(R.id.tvTovarAttary);
            tovarCode = itemView.findViewById(R.id.tvTovarCode);
            zakazOrder = itemView.findViewById(R.id.tvzakaz);
            enteredDate = itemView.findViewById(R.id.tventereddate);
            address = itemView.findViewById(R.id.tvaddress);
            zakazPrice = itemView.findViewById(R.id.tvzakazprice);
            comment = itemView.findViewById(R.id.tvcomment);
            zakazViewColor = itemView.findViewById(R.id.view);
            btnAcceptZakaz = itemView.findViewById(R.id.btnAccept);
            btnRemoveZakaz = itemView.findViewById(R.id.btnRemove);
        }
    }
}
