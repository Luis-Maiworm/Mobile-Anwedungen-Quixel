package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.quizz.R;
import com.example.quizz.data.enums.Categories;
import com.example.quizz.data.playerData.Player;

import com.example.quizz.data.playerData.StatisticsAnalyser;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class StatisticsActivity extends AppCompatActivity {

    //model
    Player currentPlayer;
    StatisticsAnalyser analyser;

    //views
    TextView textView;
    ImageView sortButton;

    //chart
    HorizontalBarChart barChart;
    ArrayList<BarEntry> values;
    BarDataSet barDataSet;
    ArrayList<IBarDataSet> iBarDataSets;
    BarData barData;

    YAxis leftAxis;
    YAxis rightAxis;
    XAxis xAxis;

    boolean flag = true;



    private void initVariables(){
        currentPlayer = (Player) getIntent().getSerializableExtra("player");
        analyser = new StatisticsAnalyser(currentPlayer);

        textView = (TextView) findViewById(R.id.wonStats);

        sortButton = (ImageView) findViewById(R.id.sortButton);
    }

    private void setVariables(){

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag) {
                    RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                    rotate.setDuration(200);
                    rotate.setInterpolator(new LinearInterpolator());
                    rotate.setFillAfter(true);

                    sortButton.startAnimation(rotate);

                    categoryChart(analyser.percentageList(false, analyser.DESC));
                    flag = false;

                } else {
                    RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                    rotate.setDuration(200);
                    rotate.setInterpolator(new LinearInterpolator());
                    rotate.setFillAfter(true);

                    sortButton.startAnimation(rotate);

                    categoryChart(analyser.percentageList(false, analyser.ASC));
                    flag = true;
                }
            }
        });

        textView.setText("\nTotal Ratio" + analyser.totalAnswerRatio() + "%");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //todo maybe checken ob überhaupt schon genug gespielt wurde -> dementsprechend statistiken zeigen
        // an sich: smarte View -> weiß was gespielt wurde was nicht (wurden gamemodes noch nicht gespielt -> wird nicht angezeigt)
        // kategorien, schwierigkeiten, types nicht gespielt -> nicht anzeigen (immer nur die, die schon gezockt wurden)


        initVariables();
        setVariables();



        categoryChart(analyser.percentageList(false, analyser.ASC));    //due to the fact how the graph is being filled, "ASC"
                                                                                    // is visually equal to the actual Descending view of the graph

    }


    private void initChartVariables(){
        barChart = (HorizontalBarChart) findViewById(R.id.barChartCats);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        xAxis = barChart.getXAxis();


        leftAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);

        //  leftAxis.setDrawLabels(false);
        //  rightAxis.setDrawLabels(false);
        //  xAxis.setDrawLabels(false);

        //  leftAxis.setEnabled(false);
        //  rightAxis.setEnabled(false);
        //  xAxis.setEnabled(false);

        barChart.setDrawBorders(false);
        barChart.setDrawGridBackground(false);

        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        barChart.setTouchEnabled(false);

        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);

        barChart.setPinchZoom(false);
        barChart.setAutoScaleMinMaxEnabled(true);

        // rightAxis.setAxisMinimum(0);
        // rightAxis.setAxisMaximum(100);

        leftAxis.setAxisMinimum(0);         //set min and maximum value of the graph (Percentages = 0-100)
        leftAxis.setAxisMaximum(100);
    }

    /**
     *
     * @param sortedMap which contains all the data which will be visualized in the method.
     */
    private void categoryChart(HashMap<Categories, Double> sortedMap) {
        initChartVariables();

        HashMap<Categories, Double> map;
        map = sortedMap;                       //sets a local map equal to the parameter

        values = new ArrayList<>();

        List<String> xAxisValues = new ArrayList<>();

        int d = 0;
        for(Map.Entry<Categories, Double> entry : map.entrySet()){
            values.add(new BarEntry(d, entry.getValue().intValue()));
            xAxisValues.add(entry.getKey().getName());
            System.out.println(d + ".  " + entry.getKey().getName());       //todo raus
            d++;
        }

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));        //sets the Category Strings as values
        xAxis.setLabelCount(xAxisValues.size());                                  //sets how many values are being shown (if this is not the size of the value array, not every category name is being shown)

        barDataSet = new BarDataSet(values, "My description");
        barDataSet.setValues(values);
        barDataSet.setBarBorderWidth(3f);
        barDataSet.setDrawValues(true);
   //     set1.setBarBorderColor(R.color.black);

   //     chart.getXAxis().setSpaceMin(5f);

        iBarDataSets = new ArrayList<>();
        iBarDataSets.add(barDataSet);


        barData = new BarData(iBarDataSets);
        barData.setValueTextSize(0.5f);


        barData.setBarWidth(0.5f);

        barChart.setFitBars(true);     //?

        barChart.setData(barData);

        barChart.getAxisLeft().setAxisMaximum(100);
        barChart.getAxisLeft().setAxisMinimum(0);

        barChart.animateXY(1000, 1000, Easing.EasingOption.EaseInSine, Easing.EasingOption.EaseInSine);
        barChart.invalidate();
    }


















}