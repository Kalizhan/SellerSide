package com.example.satushyzerdeapp.ui.tapsyrystar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.activities.LoginActivity;
import com.example.satushyzerdeapp.activities.ProfileActivity;
import com.example.satushyzerdeapp.adapters.ZakazListAdapter;
import com.example.satushyzerdeapp.modules.NewZakaz;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TapsyrystarFragment extends Fragment implements View.OnClickListener {
    View view;
    ImageView imgAccountBtn1;
    LinearLayout tapsyrystarZhok;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ArrayList<NewZakaz> newZakazArrayList;
    ZakazListAdapter zakazListAdapter;

    DatabaseReference mDatabaseReference;
    FirebaseAuth mAuth;
    String user;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tapsyrystar, container, false);
        imgAccountBtn1 = view.findViewById(R.id.accountImgView1);
        tapsyrystarZhok = view.findViewById(R.id.TapsyrystarZhok);

        imgAccountBtn1.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recyclerTapsyrys);
        newZakazArrayList = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Zakazdar");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser().getEmail().replace(".", "-");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Мәліметтер оқылуда...");
        progressDialog.setMessage("Күте тұрыңыз");

        getData();

        zakazListAdapter = new ZakazListAdapter(getActivity(), newZakazArrayList);
        recyclerView.setAdapter(zakazListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, final int pos) {
//                        Intent intent = new Intent(getContext(), ZakazWindowActivity.class);
//                        intent.putExtra("zakaz", newZakazArrayList.get(pos));
//
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
////                        Toast.makeText(getContext(), "Uzak baspa!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//        );

        return view;
    }

    private void getData() {
        progressDialog.show();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tapsyrystarZhok.setVisibility(View.GONE);
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        for (DataSnapshot snap1 : snap.getChildren()){
                            NewZakaz newZakaz = snap1.getValue(NewZakaz.class);

                            newZakazArrayList.add(newZakaz);
                            progressDialog.cancel();
                        }
                    }
                    zakazListAdapter.notifyDataSetChanged();
                } else {
                    newZakazArrayList.clear();
                    tapsyrystarZhok.setVisibility(View.VISIBLE);
                    progressDialog.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accountImgView1:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getContext(), ProfileActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
        }
    }
}