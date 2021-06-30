package com.example.satushyzerdeapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satushyzerdeapp.MainActivity;
import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.adapters.TovarInfoAdapter;
import com.example.satushyzerdeapp.modules.Tovar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TovarActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvTovarName, tvTovarCode, tvTovarPrice, tvShtukSatyldy, tvShtukKaldy, tvDopInfo, tvTovarQuantity;
    CardView cardViewInfo;
    RecyclerView recyclerView;
    ArrayList<Tovar> tovarArrayList;
    TovarInfoAdapter tovarInfoAdapter;
    Button btnAddChanges;
    Tovar curTovar;

    EditText etTovarName, etTovarCode, etAddMinusTovar, etTovarPrice, etDopInfoTovar;
    Button btnChangeInfo;
    SwitchCompat checkMinusAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);

        tvTovarName = findViewById(R.id.tovarNameTovar);
        tvTovarCode = findViewById(R.id.tovarCodeTovar);
        tvTovarPrice = findViewById(R.id.tovarPriceTovar);
        tvShtukSatyldy = findViewById(R.id.shtukSatyldy);
        tvShtukKaldy = findViewById(R.id.shtukKaldy);
        tvDopInfo = findViewById(R.id.tovarTuralyInfo);
        cardViewInfo = findViewById(R.id.cardViewDopInfo);
        btnAddChanges = findViewById(R.id.btnAddChanges);

        Intent intent = getIntent();
        curTovar = (Tovar) intent.getSerializableExtra("tovar");

        tvTovarName.setText(curTovar.getName());
        tvTovarCode.setText("#"+curTovar.getCode());
        tvTovarPrice.setText(curTovar.getPrice()+" тг/дана");
        tvShtukSatyldy.setText(curTovar.getSatyldyShtuk()+" дана сатылды");
        tvShtukKaldy.setText(curTovar.getQuantity()+" дана қалды");

        if (curTovar.getDopInfo().isEmpty()){
            cardViewInfo.setVisibility(View.INVISIBLE);
        }else{
            cardViewInfo.setVisibility(View.VISIBLE);
            tvDopInfo.setText(curTovar.getDopInfo());
        }

        String[] splitUrls = curTovar.getPhoto().split(",");

        recyclerView = findViewById(R.id.recyclerviewTovar);
        tovarArrayList = new ArrayList<>();
        tovarInfoAdapter = new TovarInfoAdapter(getApplicationContext(), splitUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(tovarInfoAdapter);

        btnAddChanges.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddChanges:
                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.dialog_add_changes_tovar, null);

                tvTovarQuantity = dialogView.findViewById(R.id.etTovarQuantit);
                etTovarName = dialogView.findViewById(R.id.etTovarNam);
                etTovarCode = dialogView.findViewById(R.id.etTovarCod);
                etAddMinusTovar = dialogView.findViewById(R.id.etAddMinusTovar);
                etTovarPrice = dialogView.findViewById(R.id.etTovarPric);
                etDopInfoTovar = dialogView.findViewById(R.id.etDopInf);
                btnChangeInfo = dialogView.findViewById(R.id.btnChange);
                checkMinusAdd = dialogView.findViewById(R.id.chechAddMinus);

                etTovarName.setText(curTovar.getName());
                etTovarCode.setText(curTovar.getCode());
                tvTovarQuantity.setText(curTovar.getQuantity() + " дана");
                etTovarPrice.setText(curTovar.getPrice()+"");
                etDopInfoTovar.setText(curTovar.getDopInfo());

                btnChangeInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int lastQuant;

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("TovarInfo").child(curTovar.getCode());

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+6"));
                        Date today = Calendar.getInstance().getTime();

                        if (etTovarName.getText().toString().isEmpty()){
                            etTovarName.setError("Поле бос болмауы қажет!");
                            return;
                        }

                        if (etTovarCode.getText().toString().isEmpty()){
                            etTovarCode.setError("Поле бос болмауы қажет!");
                            return;
                        }

                        if (etTovarPrice.getText().toString().isEmpty()){
                            etTovarPrice.setError("Поле бос болмауы қажет!");
                            return;
                        }

                        if (checkMinusAdd.isChecked()){
                            lastQuant = curTovar.getQuantity() + Integer.parseInt(etAddMinusTovar.getText().toString());
                        }else{
                            lastQuant = curTovar.getQuantity() - Integer.parseInt(etAddMinusTovar.getText().toString());
                        }

                        Log.i("lastQuant", lastQuant + "");

                        ProgressDialog progressDialog1 = new ProgressDialog(TovarActivity.this);
                        progressDialog1.setTitle("Енгізілуде...");
                        progressDialog1.show();

                        Tovar newTovar = new Tovar(etTovarName.getText().toString(), etTovarCode.getText().toString(), curTovar.getPhoto(), Long.parseLong(etTovarPrice.getText().toString()),
                               lastQuant, curTovar.getSatyldyShtuk(), dateFormat.format(today), etDopInfoTovar.getText().toString());

                        mDatabase.setValue(newTovar).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog1.cancel();
                                Toast.makeText(TovarActivity.this, "Тауар туралы мәлімет өзгертілді!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
                    }
                });

                dialog.setView(dialogView);
                dialog.show();
                dialog.setCancelable(true);
                break;
        }
    }
}