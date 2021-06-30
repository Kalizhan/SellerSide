package com.example.satushyzerdeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satushyzerdeapp.modules.CheckInternetStatus;
import com.example.satushyzerdeapp.modules.NewZakaz;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    BottomNavigationMenuView mbottomNavigationMenuView;
    View view;
    BottomNavigationItemView itemView;
    View cart_badge;

    DatabaseReference databaseReference;

    static TextView cart_badgeTv;
    CheckInternetStatus mCheckInternetStatus;
    boolean is_internet_connected = false;

    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("Zakazdar");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        for (DataSnapshot snap1 : snap.getChildren()){
                            NewZakaz newZakaz = snap1.getValue(NewZakaz.class);
                            if (newZakaz.getTovarSituation().equals("new order")){
                                i = i+1;
                            }
                            Log.i("cart_badge", "i= "+i);

                            setBagdeCount(i);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mbottomNavigationMenuView = (BottomNavigationMenuView) navView.getChildAt(0);
        view = mbottomNavigationMenuView.getChildAt(1);
        itemView = (BottomNavigationItemView) view;

        cart_badge = LayoutInflater.from(this).inflate(R.layout.custom_cart_item_layout, mbottomNavigationMenuView, false);
        cart_badgeTv = cart_badge.findViewById(R.id.cart_badge);

        itemView.addView(cart_badge);

        mCheckInternetStatus = new CheckInternetStatus(MainActivity.this);
        is_internet_connected = mCheckInternetStatus.isInternetConnected();
        if (!is_internet_connected){
            Toast.makeText(this, "Интернетті тексеріңіз", Toast.LENGTH_LONG).show();
        }
//        accountImgView.setOnClickListener(this);
//        filterImgView.setOnClickListener(this);
        //badgeDefault();
    }

    public static void setBagdeCount(int n) {
        cart_badgeTv.setText("" + n);
    }
}