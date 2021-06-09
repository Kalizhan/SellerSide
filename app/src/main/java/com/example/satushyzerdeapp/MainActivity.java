package com.example.satushyzerdeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.satushyzerdeapp.modules.CheckInternetStatus;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationMenuView mbottomNavigationMenuView;
    View view;
    BottomNavigationItemView itemView;
    View cart_badge;

    static TextView cart_badgeTv;
    CheckInternetStatus mCheckInternetStatus;
    boolean is_internet_connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAuth = FirebaseAuth.getInstance();

        //setActionBar(toolbar);

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

    private void badgeDefault() {
    }

    public static void setBagdeCount(int n) {
        cart_badgeTv.setText("" + n);
    }

    public static void increaseBagdeCount() {
        int n = Integer.parseInt("" + cart_badgeTv.getText()) + 1;
        cart_badgeTv.setText("" + n);
    }

    public static void decreaseBagdeCount() {
        int n = Integer.parseInt("" + cart_badgeTv.getText()) - 1;
        cart_badgeTv.setText("" + n);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            reload();
//        }
//    }
//
//    private void reload() {
//    }
}