package com.example.satushyzerdeapp.adapters;

import android.content.Context;
import android.content.res.Resources;
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
    int i = 0;

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
        int pos = position + 1;
        NewZakaz newZakaz = newZakazList.get(position);

        holder.zakazOrder.setText("Тапсырыс " + pos);
        holder.enteredDate.setText(newZakaz.getDate());
        holder.zakazSituation.setText(newZakaz.getPaymentStyle());

        if (newZakaz.getPaymentStyle().equals("Толықтай төлеу")) {
            holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.yesil));
            holder.zakazAkshaTolenuKerek.setText(newZakaz.getTovarPrice() + " тг");
            holder.zakazAkshaKaldy.setText("0 тг");
        } else if (newZakaz.getPaymentStyle().equals("Жартылай төлеу")) {
            holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.sari));
            holder.zakazAkshaTolenuKerek.setText((int) (newZakaz.getTovarPrice() / 2) + " тг");
            holder.zakazAkshaKaldy.setText((int) (newZakaz.getTovarPrice() / 2) + " тг");
        } else if (newZakaz.getPaymentStyle().equals("Тауарды алған соң төлеу")) {
            holder.zakazSituation.setTextColor(context.getResources().getColor(R.color.kyzyl));
            holder.zakazAkshaTolenuKerek.setText("0 тг");
            holder.zakazAkshaKaldy.setText(newZakaz.getTovarPrice() + " тг");
        }

        if (newZakaz.getTovarSituation().equals("new order")) {
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.neworder));
        } else if (newZakaz.getTovarSituation().equals("order accepted")) {
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.accept));
        } else if (newZakaz.getTovarSituation().equals("order got")) {
            holder.zakazViewColor.setBackgroundColor(context.getResources().getColor(R.color.finished));
        }

        if (!newZakaz.getComment().isEmpty()) {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(newZakaz.getComment());
        } else {
            holder.comment.setVisibility(View.GONE);
        }
//
//        String user = holder.zakazEmail.getText().toString().replace(".", "-");
//        Log.i("user", "Email: " + user);


//        holder.btnAcceptZakaz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Log.i("codes", codes.length + "/" + quantities.length);
////                    Log.i("quantites1", String.valueOf(quantities[i]));
//                mDatabase.child("TovarInfo").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            if (codes.length == quantities.length) {
//                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                                    for (i = 0; i < codes.length; i++) {
//                                        Tovar tovar = snapshot1.getValue(Tovar.class);
//                                        if (codes[i].equals(tovar.getCode())) {
//                                            int quant = tovar.getSatyldyShtuk();
////                                            Log.i("quantites", String.valueOf(quant));
//                                            quant = quant + Integer.parseInt(quantities[i]);
//                                            int q1 = tovar.getQuantity() - Integer.parseInt(quantities[i]);
//                                            mDatabase.child("TovarInfo").child(codes[i]).child("satyldyShtuk").setValue(quant);
//                                            mDatabase.child("TovarInfo").child(codes[i]).child("quantity").setValue(q1);
//
//                                        }
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                mDatabase.child("Zakazdar").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snap : snapshot.getChildren()) {
//
//                            String tovarSituat = "order accepted";
//                            mDatabase.child("Zakazdar").child(user).child("" + holder.tovarKey.getText()).child("tovarSituation").setValue(tovarSituat);
//                            Toast.makeText(context, "Өзгертілді!", Toast.LENGTH_SHORT).show();
//                            notifyItemChanged(position);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

//        holder.btnRemoveZakaz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, "btn Otmena " + holder.zakazOrder.getText().toString(), Toast.LENGTH_SHORT).show();
//                mDatabase.child("Zakazdar").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot snap : snapshot.getChildren()) {
//                                NewZakaz newZakaz2 = snap.getValue(NewZakaz.class);
//                                if (holder.enteredDate.getText().toString().equals(newZakaz2.getDate())) {
//                                    snap.getRef().removeValue();
//                                    Toast.makeText(context, holder.zakazOrder.getText() + " өшірілді!", Toast.LENGTH_SHORT).show();
//                                    notifyItemRemoved(position);
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

//        holder.btnLastAcceptance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatabase.child("Zakazdar").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snap : snapshot.getChildren()) {
//                            String tovarSituat = "order got";
//                            mDatabase.child("Zakazdar").child(user).child("" + holder.tovarKey.getText()).child("tovarSituation").setValue(tovarSituat);
//                            Toast.makeText(context, "Өзгертілді!", Toast.LENGTH_SHORT).show();
//                            notifyItemChanged(position);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return newZakazList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView zakazOrder, enteredDate, zakazSituation, zakazAkshaTolenuKerek, zakazAkshaKaldy, comment;
        View zakazViewColor;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            zakazOrder = itemView.findViewById(R.id.tvzakaz);
            enteredDate = itemView.findViewById(R.id.tventereddate);
            zakazSituation = itemView.findViewById(R.id.tvZakazSituation);
            zakazAkshaTolenuKerek = itemView.findViewById(R.id.tvZakazPrice);
            zakazAkshaKaldy = itemView.findViewById(R.id.tvZakazKalganAksha);
            comment = itemView.findViewById(R.id.tvcomment);
            zakazViewColor = itemView.findViewById(R.id.view);
        }
    }
}
