package com.example.satushyzerdeapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satushyzerdeapp.MainActivity;
import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.adapters.BoughtTovarListAdapter;
import com.example.satushyzerdeapp.modules.NewZakaz;
import com.example.satushyzerdeapp.modules.Tovar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ZakazWindowActivity extends AppCompatActivity implements View.OnClickListener {
    TextView enteredDate, fullName, address, phoneNumber, orderPrice, deliveryPrice, fullPrice, paym, paymStyle, zakazSituation, zakazAkshaTolenuKerek, zakazAkshaKaldy;
    CardView delivery, cardViewColor;
    LinearLayout cardLayout;
    Button btnAccept, btnFinish, btnRemove;
    String[] codes, quantities;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Tovar> tovarArrayList;
    BoughtTovarListAdapter boughtTovarListAdapter;

    DatabaseReference mDatabaseReference;
    ProgressDialog progressDialog;
    NewZakaz newZakaz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakaz_window);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        init();


        Intent intent = getIntent();
        newZakaz = (NewZakaz) intent.getSerializableExtra("zakaz");

        enteredDate.setText(newZakaz.getDate());
        fullName.setText(newZakaz.getFullName());
        address.setText(newZakaz.getAddress());
        phoneNumber.setText(newZakaz.getPhoneNum());
        paym.setText(newZakaz.getPayment());
        paymStyle.setText(newZakaz.getPaymentStyle());

        fullPrice.setText(newZakaz.getTovarPrice().toString() + " тг");

        codes = newZakaz.getTovarCode().split(",");
        quantities = newZakaz.getTovarQuantity().split(",");
        long price = newZakaz.getTovarPrice() - 1000;
        if (newZakaz.getCheckDelivery().equals("yes")) {
            delivery.setVisibility(View.VISIBLE);
            cardLayout.setVisibility(View.VISIBLE);
            orderPrice.setText("" + price);
        } else {
            delivery.setVisibility(View.GONE);
            cardLayout.setVisibility(View.INVISIBLE);
            orderPrice.setText(newZakaz.getTovarPrice() + " тг");
        }

        zakazSituation.setText(newZakaz.getPaymentStyle());
        if (newZakaz.getPaymentStyle().equals("Толықтай төлеу")) {
            zakazAkshaTolenuKerek.setText(newZakaz.getTovarPrice() + " тг");
            zakazAkshaKaldy.setText("0 тг");
        } else if (newZakaz.getPaymentStyle().equals("Жартылай төлеу")) {
            zakazAkshaTolenuKerek.setText((int) (newZakaz.getTovarPrice() / 2) + " тг");
            zakazAkshaKaldy.setText((int) (newZakaz.getTovarPrice() / 2) + " тг");
        } else if (newZakaz.getPaymentStyle().equals("Тауарды алған соң төлеу")) {
            zakazAkshaTolenuKerek.setText("0 тг");
            zakazAkshaKaldy.setText(newZakaz.getTovarPrice() + " тг");
        }

        if (newZakaz.getTovarSituation().equals("new order")) {
            btnAccept.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        } else if (newZakaz.getTovarSituation().equals("order accepted")) {
            btnAccept.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        } else if (newZakaz.getTovarSituation().equals("order got")){
            btnAccept.setVisibility(View.GONE);
            btnFinish.setVisibility(View.GONE);
        }

        tovarArrayList = new ArrayList<>();
        boughtTovarListAdapter = new BoughtTovarListAdapter(ZakazWindowActivity.this, tovarArrayList, newZakaz.getTovarQuantity());
        recyclerView.setAdapter(boughtTovarListAdapter);
        linearLayoutManager = new LinearLayoutManager(ZakazWindowActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressDialog = new ProgressDialog(ZakazWindowActivity.this);
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");
        progressDialog.show();

        getData();
        btnAccept.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
    }

    private void init() {
        recyclerView = findViewById(R.id.zakazWindowRecycler);
        enteredDate = findViewById(R.id.tvZakazDate);
        fullName = findViewById(R.id.tvZakazFullName);
        address = findViewById(R.id.tvZakazCity);
        phoneNumber = findViewById(R.id.tvZakazPhoneNumber);
        delivery = findViewById(R.id.linearLayout1);
        orderPrice = findViewById(R.id.tvZakazOrderPrice);
        deliveryPrice = findViewById(R.id.tvZakazDeliverPrice);
        fullPrice = findViewById(R.id.tvZakazFullPrice);
        btnAccept = findViewById(R.id.zakazBtnAccept);
        btnFinish = findViewById(R.id.zakazBtnFinish);
        btnRemove = findViewById(R.id.zakazBtnRemove);
        cardLayout = findViewById(R.id.cardLayout);
        paym = findViewById(R.id.paym);
        paymStyle = findViewById(R.id.paymStyle);
        cardViewColor = findViewById(R.id.cardViewColor);
        zakazSituation = findViewById(R.id.ZakazSituation);
        zakazAkshaTolenuKerek = findViewById(R.id.ZakazPrice);
        zakazAkshaKaldy = findViewById(R.id.ZakazKalganAksha);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void getData() {
        for (int i = 0; i < codes.length; i++) {
            mDatabaseReference.child("TovarInfo").child(codes[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Tovar tovar = snapshot.getValue(Tovar.class);
                        tovarArrayList.add(tovar);
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    int i;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zakazBtnAccept:
                mDatabaseReference.child("TovarInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (codes.length == quantities.length) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    for (i = 0; i < codes.length; i++) {
                                        Tovar tovar = snapshot1.getValue(Tovar.class);
                                        if (codes[i].equals(tovar.getCode())) {
                                            int quant = tovar.getSatyldyShtuk();
                                            quant = quant + Integer.parseInt(quantities[i]);
                                            int q1 = tovar.getQuantity() - Integer.parseInt(quantities[i]);
                                            mDatabaseReference.child("TovarInfo").child(codes[i]).child("satyldyShtuk").setValue(quant);
                                            mDatabaseReference.child("TovarInfo").child(codes[i]).child("quantity").setValue(q1);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                mDatabaseReference.child("Zakazdar").child(newZakaz.getEmail().replace(".", "-")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String tovarSituat = "order accepted";
                            mDatabaseReference.child("Zakazdar").child(newZakaz.getEmail().replace(".", "-")).child(newZakaz.getTovarKey()).child("tovarSituation").setValue(tovarSituat);
                            Toast.makeText(ZakazWindowActivity.this, "Өзгертілді!", Toast.LENGTH_SHORT).show();
                            btnAccept.setVisibility(View.GONE);
                            btnFinish.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.zakazBtnFinish:
                mDatabaseReference.child("Zakazdar").child(newZakaz.getEmail().replace(".", "-")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String tovarSituat = "order got";
                            mDatabaseReference.child("Zakazdar").child(newZakaz.getEmail().replace(".", "-")).child("" + newZakaz.getTovarKey()).child("tovarSituation").setValue(tovarSituat);
                            Toast.makeText(ZakazWindowActivity.this, "Өзгертілді!", Toast.LENGTH_SHORT).show();
                            btnFinish.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.zakazBtnRemove:
                mDatabaseReference.child("Zakazdar").child(newZakaz.getEmail().replace(".", "-")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                NewZakaz newZakaz2 = snap.getValue(NewZakaz.class);
                                if (enteredDate.getText().toString().equals(newZakaz2.getDate())) {
                                    snap.getRef().removeValue();
                                    Toast.makeText(ZakazWindowActivity.this, "Тапсырыс өшірілді!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ZakazWindowActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
