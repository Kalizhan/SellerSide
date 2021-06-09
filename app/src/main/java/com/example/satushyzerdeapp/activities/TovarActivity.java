package com.example.satushyzerdeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.adapters.TovarInfoAdapter;
import com.example.satushyzerdeapp.modules.Tovar;

import java.util.ArrayList;

public class TovarActivity extends AppCompatActivity {

    TextView tvTovarName, tvTovarCode, tvTovarPrice, tvTovarQuantity;
    RecyclerView recyclerView;
    ArrayList<Tovar> tovarArrayList;
    TovarInfoAdapter tovarInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);

        tvTovarName = findViewById(R.id.tovarNameTovar);
        tvTovarCode = findViewById(R.id.tovarCodeTovar);
        tvTovarPrice = findViewById(R.id.tovarPriceTovar);
        tvTovarQuantity = findViewById(R.id.tovarQuantityTovar);

        Intent intent = getIntent();
        Tovar curTovar = (Tovar) intent.getSerializableExtra("tovar");

        tvTovarName.setText(curTovar.getName());
        tvTovarCode.setText(curTovar.getCode());
        tvTovarPrice.setText(""+curTovar.getPrice());
        tvTovarQuantity.setText(""+curTovar.getQuantity());

        String[] splitUrls = curTovar.getPhoto().split(",");
//        for (int i=0; i<splitUrls.length; i++){
//
//        }

        recyclerView = findViewById(R.id.recyclerviewTovar);
        tovarArrayList = new ArrayList<>();
        tovarInfoAdapter = new TovarInfoAdapter(getApplicationContext(), splitUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(tovarInfoAdapter);
    }
}