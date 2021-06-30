package com.example.satushyzerdeapp.ui.report;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.satushyzerdeapp.R;
import com.example.satushyzerdeapp.modules.Tovar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    View view;
    BarChart barChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsName;

    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_report, container, false);

        barChart = view.findViewById(R.id.barCharts);

        databaseReference = FirebaseDatabase.getInstance().getReference("TovarInfo");

        ArrayList<Tovar> tovarArrayList = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Tovar tovar = snapshot1.getValue(Tovar.class);
                    tovarArrayList.add(tovar);
                }


                barEntryArrayList = new ArrayList<>();
                labelsName = new ArrayList<>();

                for (int i = 0; i < tovarArrayList.size(); i++) {
                    int sales = tovarArrayList.get(i).getSatyldyShtuk();
                    String code = tovarArrayList.get(i).getCode();
                    barEntryArrayList.add(new BarEntry(i, sales));
                    labelsName.add(code);
                }

                BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Түстер");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                BarData barData = new BarData(barDataSet);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawGridLines(false);
                xAxis.setDrawAxisLine(false);
                xAxis.setLabelRotationAngle(270);
                xAxis.setLabelCount(labelsName.size());

                barChart.setFitBars(true);
                barChart.setData(barData);
                barChart.getDescription().setText("Сатылған тауарлар");
                barChart.animateY(1500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}