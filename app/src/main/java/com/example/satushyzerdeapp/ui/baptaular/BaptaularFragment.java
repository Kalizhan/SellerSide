package com.example.satushyzerdeapp.ui.baptaular;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.activities.LoginActivity;
import com.example.satushyzerdeapp.activities.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class BaptaularFragment extends Fragment implements View.OnClickListener {
    View v;
    TextView btnProfile;
    Button btnLogOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_baptaular, container, false);
        btnProfile = v.findViewById(R.id.btnProfile);
        btnLogOut = v.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            btnLogOut.setVisibility(View.VISIBLE);
        }else {
            btnLogOut.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnProfile:
                if (FirebaseAuth.getInstance().getCurrentUser() != null){

                    startActivity(new Intent(getContext(), ProfileActivity.class));
                }else{
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
            case R.id.btnLogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }
}